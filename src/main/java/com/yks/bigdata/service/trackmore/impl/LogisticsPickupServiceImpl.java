package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.common.ExceptionEnum;
import com.yks.bigdata.dao.Erp_ordersMapper;
import com.yks.bigdata.dao.Logistics_pickupMapper;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticsPickup;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.ILogisticsPickupService;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import com.yks.bigdata.util.DateUtils;
import com.yks.bigdata.util.ExecutorsUtils;
import com.yks.bigdata.vo.UpdateErpOrderVo;
import com.yks.bigdata.vo.UpdatePickUp;
import com.yks.bigdata.web.vo.MessageVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by zh on 2017/6/30.
 */
@Service
public class LogisticsPickupServiceImpl implements ILogisticsPickupService {

    private static Logger log = LoggerFactory.getLogger(LogisticsPickupServiceImpl.class);

    @Autowired
    Logistics_pickupMapper pickupMapper;

    @Autowired
    Erp_ordersMapper ordersMapper;

    @Autowired
    IErpOrdersService ordersService;

    @Autowired
    IRequestTaskService requestTaskService;

    /**
     * 批量捡货数据录入
     *
     * @param pickupList
     */
    public Integer insertBatch(List<LogisticsPickup> pickupList) {
        if (pickupList.size() == 0) {
            log.error("pickupList size为0");
        }
        List<LogisticsPickup> messageVo = null;
        try {
            //检查合法性
            /**
             * 1、检查是否存在erp中，没有存在则不能获取物流信息
             */
            messageVo = checkIllegal(pickupList);
        } catch (Exception e) {
            log.error("检查合法性异常", e);
        }
        List<List<LogisticsPickup>> splitPickupLists = splitOrders(messageVo);
        for (List<LogisticsPickup> splitPickupList : splitPickupLists) {
            insertBath1000(splitPickupList);
        }
        return messageVo.size();
    }


    /**
     * 模糊查询
     * 分页
     *
     * @param pickup
     * @return
     */
    @Override
    public List<LogisticsPickup> selectLogisticsPickup(LogisticsPickup pickup) {
        return pickupMapper.selectLogisticsPickup(pickup);
    }

    @Override
    public void updatePickupToAggred(String orderId) {
        pickupMapper.updatePickupToAggred(orderId);
    }

    @Override
    public void updatePickupToAggredBatch(List<String> orderIds) {
        pickupMapper.updatePickupToAggredBatch(orderIds);
    }

    /**
     * pickup表中register_status更新为1 ,新增 register_time
     *
     * @param registerTasks
     */
    @Override
    public void updateStatusRegistered(List<RequestTask> registerTasks) {
        List<String> erpOrderIds = mkErpOrdersId(registerTasks);
        UpdatePickUp updatePickUp = new UpdatePickUp(DateUtils.getCurrentDateStr(), erpOrderIds);
        pickupMapper.updateStatusRegistered(updatePickUp);
    }

    @Override
    public LogisticsPickup selectLogisticsPickupByOrderId(String orderId) {
        return pickupMapper.selectLogisticsPickupByOrderId(orderId);
    }

    private List<String> mkErpOrdersId(List<RequestTask> registerTasks) {
        ArrayList<String> erpOrderIds = new ArrayList<>();
        for (RequestTask task : registerTasks) {
            erpOrderIds.add(String.valueOf(task.getErpOrdersId()));
        }
        return erpOrderIds;
    }

    /**
     * 检查数据是否合法
     *
     * @param pickupList
     * @return
     */
    private List<LogisticsPickup> checkIllegal(List<LogisticsPickup> pickupList) throws Exception {
        //1.检查order_id在erp_orders表中是否存在
        List<List<LogisticsPickup>> pickupLists = new ArrayList<>();
        ArrayList<LogisticsPickup> idsList = new ArrayList<>();
        for (int i = 0; i < pickupList.size(); i++) {
            idsList.add(pickupList.get(i));
            if (i % 10000 == 0 && i != 0) {
                pickupLists.add(idsList);
                idsList = new ArrayList<>();
            }
        }
        if (!CollectionUtils.isEmpty(idsList)) {
            pickupLists.add(idsList);
        }
        //开启线程进行检测这堆ids
        ExecutorService es = ExecutorsUtils.getExecutors();
        Collection tasks = new ArrayList();
        for (List<LogisticsPickup> ids : pickupLists) {
            tasks.add(new Check(ordersService, pickupMapper, ids, requestTaskService));
        }
        List<Future<List<LogisticsPickup>>> resultList = (List<Future<List<LogisticsPickup>>>) es.invokeAll(tasks);
        return processMessageVo(resultList);
    }

    private List<LogisticsPickup> processMessageVo(List<Future<List<LogisticsPickup>>> resultList) throws Exception {
        List<LogisticsPickup> logisticsPickups = new ArrayList<>();
        for (Future<List<LogisticsPickup>> f : resultList) {
            for (LogisticsPickup logisticsPickup:f.get()) {
                logisticsPickups.add(logisticsPickup);
            }
        }
        return logisticsPickups;
    }


    /**
     * 1000条记录提交一回
     *
     * @param pickupList
     */
    @Transactional(value = "transactionManager")
    private void insertBath1000(List<LogisticsPickup> pickupList) {
        log.info("插入pickup数据1000条");
        pickupMapper.addBatch(pickupList);
        ArrayList<UpdateErpOrderVo> vos = new ArrayList<>();
        // TODO: 2017/7/8 更新erp_orders表中的物流渠道信息
        //ordersMapper.updateBatch(vos);
    }

    /**
     * 数据拆分
     *
     * @param list
     * @return
     */
    private List<List<LogisticsPickup>> splitOrders(List<LogisticsPickup> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        ArrayList<List<LogisticsPickup>> result = new ArrayList<List<LogisticsPickup>>();
        int size = list.size();
        ArrayList<LogisticsPickup> ordersList = new ArrayList<LogisticsPickup>();
        for (int i = 0; i < size; i++) {
            LogisticsPickup pickup = list.get(i);
            pickup.setCreateTime(new Date());
            ordersList.add(pickup);
            //满1000个使用另外一个ordersList
            if (ordersList.size() == 1000) {
                result.add(ordersList);
                ordersList = new ArrayList<LogisticsPickup>();
            }
        }
        if (ordersList.size() != 0) {
            result.add(ordersList);
        }
        return result;
    }


}

class Check implements Callable<List<LogisticsPickup>> {
    private static Logger log = LoggerFactory.getLogger(Check.class);
    IErpOrdersService ordersService;
    List<LogisticsPickup> ids;
    Logistics_pickupMapper pickupMapper;
    IRequestTaskService requestTaskService;

    public Check(IErpOrdersService ordersService, Logistics_pickupMapper pickupMapper, List<LogisticsPickup> ids, IRequestTaskService requestTaskService) {
        this.ordersService = ordersService;
        this.ids = ids;
        this.pickupMapper = pickupMapper;
        this.requestTaskService = requestTaskService;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public List<LogisticsPickup> call() throws Exception {
        log.info("check线程开启 " + Thread.currentThread().getName());
        List<LogisticsPickup> messageVo = checkOrders(ids);
        log.info("check线程结束 " + Thread.currentThread().getName());
        return messageVo;
    }

    /**
     * //1.检查order_id在erp_orders表中是否存在
     * @return
     * @throws Exception
     */
    private List<LogisticsPickup> checkOrders(List<LogisticsPickup> ids) throws Exception {
        List<LogisticsPickup> logisticsPickups = new ArrayList<>();
        //1.检查order_id在erp_orders表中是否存在,只上传已在erp orders中的
        for (LogisticsPickup lp:ids) {
            if(ordersService.selectOrderByOrderId(Long.parseLong(lp.getOrderId())) == null){
                lp.setStatus(ExceptionEnum.IMPORT_ERP_NOT_FOUND.getValue());
            }
            logisticsPickups.add(lp);
        }
        return  logisticsPickups;
    }

}