package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.yks.bigdata.common.ExceptionEnum;
import com.yks.bigdata.common.LoopProcessConstant;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dao.TrackmoreAccountMapper;
import com.yks.bigdata.dao.TrackmoreFetchChannelMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.*;
import com.yks.bigdata.service.trackmore.*;
import com.yks.bigdata.util.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxing on 2017/7/16.
 */
@Service
@Scope(value = "singleton")
public class LoopSearchServiceNewImpl implements LoopSearchService {

    private static Logger log = LoggerFactory.getLogger(LoopSearchServiceNewImpl.class);

    @Autowired
    IRequestTaskService requestTaskService;

    @Autowired
    ITrackmoreAccountService trackmoreAccountService;

    @Autowired
    Tracker trackerSite;

    @Autowired
    IStatusInformationService informactionService;

    @Autowired
    ITrackSourceService trackSourceService;

    @Autowired
    ITrackInfoService infoService;

    @Autowired
    IErpOrdersService erpOrdersService;

    @Autowired
    SystemExceptionMapper systemExceptionMapper;

    @Override
    public void registerTrackmore() {
        systemExceptionMapper.insert(new SystemException("trackmore register data begin","" + DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")));
        try{
            //0.1获取所有任务池中的所有需要注册的requst_task数据 (request_task 表 查询条件task_status 为1 )
            List<RequestTask> registerTasks = requestTaskService.getAllNeddRegisterRequestTask();
            //注册
            if(!CollectionUtils.isEmpty(registerTasks))
                registerTraceMore40(registerTasks);
        }catch (Exception ex){
            log.error("注册异常" + ex.getLocalizedMessage());
            systemExceptionMapper.insert(new SystemException("注册异常",ex.getLocalizedMessage()));
        }
    }
    @Autowired
    private TrackmoreFetchChannelMapper trackmoreFetchChannelMapper;
    @Override
    public void queryTrackmore() {
        if (!LoopProcessConstant.bo) {
            LoopProcessConstant.started();
            systemExceptionMapper.insert(new SystemException("trackmore fetch data begin", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
            List<TrackmoreFetchChannel> trackmoreFetchChannels = trackmoreFetchChannelMapper.selectByStatus(1);
            if(CollectionUtils.isEmpty(trackmoreFetchChannels)){
                LoopProcessConstant.stoped();
                systemExceptionMapper.insert(new SystemException("trackmore fetch data end", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
                return;
            }
            for (TrackmoreFetchChannel trackmoreFetchChannel:trackmoreFetchChannels){
                //0.3获取所有任务池中的所有需要查询的requst_task数据 (request_task 表 查询条件task_status 为2 )
                RequestTask requestTask = new RequestTask();
                requestTask.setChannel("'" + trackmoreFetchChannel.getChannel() + "'");
                requestTask.setTaskStatus(2);
                Integer integer = requestTaskService.selectRequestTasksCount(requestTask);
                int count = integer%10000==0?integer/10000:integer/10000 + 1;
                for(int i=1;i<=count;i++){
                    PageHelper.startPage(i, 10000, true);
                    List<RequestTask> queryTasks = requestTaskService.selectRequestTasks(requestTask);
                    //List<RequestTask> queryTasks = requestTaskService.selectRequestTaskByPickup();
                    //查询
                    if (!CollectionUtils.isEmpty(queryTasks)){
                        queryTrackMoreInfo(queryTasks);
                    }
                }
                trackmoreFetchChannel.setFetchStatus(10);
                trackmoreFetchChannelMapper.updateByPrimaryKeySelective(trackmoreFetchChannel);
            }

            LoopProcessConstant.stoped();
            systemExceptionMapper.insert(new SystemException("trackmore fetch data end", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        }
    }

    @Override
    public void loop() {
        //注册
        /*registerTrackmore();
        try{Thread.sleep(1000);}catch(Exception ex){}*/
        //查询
        queryTrackmore();
    }

    /**
     * 接口一次性只能注册40个物流单
     * @param registerTasks
     */
    private synchronized void registerTraceMore40(List<RequestTask> registerTasks)throws Exception{
        List<List<RequestTask>> lists = splitTraceMore(registerTasks);
        for (List<RequestTask> list:lists) {
            TrackmoreAccount account = trackmoreAccountService.selectAvailableAccount();
            if(account == null){
                systemExceptionMapper.insert(new SystemException("trackmore注册失败","没有可用额度账号注册"));
                return;
            }
            String requestData = createRequestData(list);
            if(trackerSite.registerLogisticsProjectBatch(requestData, account.getApiKey())){
                //创建成功 更新状态为2 标记是那个账号创建成功
                requestTaskService.updateStatusRegister1(setRequestTaskAccount(list,account.getAccount()));
                //减少额度
                trackmoreAccountService.reduceDataCount(account.getId(),account.getDataCount() - list.size());
                systemExceptionMapper.insert(new SystemException("trackmore注册成功","账号：" + account.getAccount() + "额度：" + (account.getDataCount() - list.size())));
            }else{
                //创建物流项目失败 更新状态为4
                requestTaskService.updateStatusRegisterFailed(list);
                systemExceptionMapper.insert(new SystemException("trackmore注册失败","账号：" + account.getAccount()));
            }
        }
    }
    private List<RequestTask> setRequestTaskAccount(List<RequestTask> list, String account){
        for (RequestTask requestTask:list) {
            requestTask.setTrackAccount(account);
        }
        return list;
    }

    /**
     * 查询物流信息
     * @param requestTasks
     * @throws Exception
     */
    private synchronized void queryTrackMoreInfo(List<RequestTask> requestTasks){
        for (RequestTask task:requestTasks) {
            try{Thread.sleep(1000);}catch (Exception ex){};
            String requestData = "{\"tracking_number\": \"" + task.getTrackingNumber() + "\",\"carrier_code\":\"" + task.getCarrierCode() + "\"}";
            TrackmoreAccount trackmoreAccount = trackmoreAccountService.selectByAccount(task.getTrackAccount());
            try{
                String responseData = trackerSite.queryInfo(requestData, trackmoreAccount.getApiKey());
                if(!StringUtils.isEmpty(responseData)){//抓取成功 标记状态
                    RequestTask task1 = new RequestTask();
                    task1.setId(task.getId());
                    task1.setFetchTime(new Date());
                    requestTaskService.updateByPrimaryKeySelective(task1);//更新抓取时间
                    if(isFinish(responseData)){
                        processFinish(task,responseData);
                    }else{
                        processUnFinish(task,responseData);
                    }
                }//抓取失败不做任何动作，下次继续抓取
            }catch (Exception ex){
                log.error("trackmore查询异常" + ex.getLocalizedMessage());
                systemExceptionMapper.insert(new SystemException("trackmore查询异常:" + task.getTrackingNumber(), ex.getLocalizedMessage()));
            }
        }
    }

    /**
     * 判断是否完结
     * 判断标准 返回的json串中的status为delivered
     * @param response
     * @return
     */
    private boolean isFinish(String response) {
        JSONObject rootObject = JSON.parseObject(response);
        JSONArray items = rootObject.getJSONObject("data").getJSONArray("items");
        String status = items.getJSONObject(0).getString("status");
        if (org.apache.commons.lang.StringUtils.equals(status, "delivered")) {
            return true;
        } else {
            return false;
        }
    }
    @Transactional(value = "transactionManager")
    private void processUnFinish(RequestTask task, String response) {
        //未完结
        //2.2.1 将结果写入或者更新 track_info 和 track_info_detail
        infoService.saveOrUpdateInfoAndDetail(task, response);
        //2.2.2 将结果写入track_source中
        trackSourceService.add(task, response);
        //2.2.3.依据状态表logistics_status中的状态标记,进行标记数据,erp_orders字段`track_status`标记对应的值
        signTrackStatusOnErpOrders(task, response);
    }

    @Transactional(value = "transactionManager")
    private void processFinish(RequestTask task, String response) {
        //已完结
        //2.1.1 request_task标记该条数据task_status=5 表示已经完结
        requestTaskService.updateStatusFinish(task);//任务标记为已完结
        task.setTaskStatus(5);
        //2.1.2 将结果写入或者更新 track_info 和 track_info_detail
        infoService.saveOrUpdateInfoAndDetail(task, response);
        //2.1.3 将结果写入track_source中
        trackSourceService.add(task, response);
        //标记状态
        signTrackStatusOnErpOrders(task, response);
    }

    /**
     * 依据状态表logistics_status中的状态标记,进行标记数据,erp_orders字段`track_status`标记对应的值;
     */
    private void signTrackStatusOnErpOrders(RequestTask task, String response) {
        ErpOrders erpOrders = new ErpOrders();
        erpOrders.setErpOrdersId(task.getErpOrdersId());
        List<LogisticsStatus> logisticsStatuses = informactionService.selectByChannel(task.getChannel());
        if (CollectionUtils.isEmpty(logisticsStatuses)){
            log.info("该request task 没有查找到合适的状态 (上网 交航等) : request task id为:" + task.getId() + "\n 返回的json串为:" + response);
            erpOrders.setTrackStatus(ExceptionEnum.NOT_FOUND_LOGISTICS_STATUS.getValue());
            erpOrdersService.updateTrackmoreStatus(erpOrders);
            return;
        }
        try {
            ErpOrders erpOrders1 = judeStateException(logisticsStatuses, task, response);
            //标记状态 和时间
            erpOrdersService.updateErpOrders(erpOrders1);
        } catch (Exception e) {
            log.error("标记物流状态异常", e);
        }
    }

    /**
     * 判断订单异常
     * 标准 :
     * state=10 未上网 判断response 是否含有Pickup shipment checked in at时间为 48
     * state=11 未封发 判断response 是否含有Shipment designated to时间为 72
     * state=12 未交航 判断response 是否含有Shipment arrived at airport时间为 96
     * state=13 未到目的国 判断response 是否含有Custom cleared and arrived at时间为 120
     * state=14 未妥投 判断response 是否含有DELIVERE DSHIPMENT DELIVERED时间为 144
     *
     * @param task
     * @param response
     */
    private ErpOrders judeStateException(List<LogisticsStatus> statusList, RequestTask task, String response) throws Exception {
        ErpOrders erpOrders = erpOrdersService.selectOrderByOrderId(task.getErpOrdersId());
        Long minus = System.currentTimeMillis() - erpOrders.getOrdersExportTime().getTime();
        JSONObject origin_info = JSON.parseObject(response).getJSONObject("data").getJSONArray("items").getJSONObject(0).getJSONObject("origin_info");
        JSONObject destination_info = JSON.parseObject(response).getJSONObject("data").getJSONArray("items").getJSONObject(0).getJSONObject("destination_info");
        Integer status = erpOrders.getTrackStatus();
        if(origin_info == null && destination_info == null){//无信息
            if(minus >= 48 * 3600 * 1000){
                status = ExceptionEnum.TRACKMORE_STATUS10.getValue();//未上网
            }else{
                status = ExceptionEnum.TRACKMORE_NOT_FOUND_DATA.getValue();//占无物流信息
            }
            ErpOrders erpOrders1 = new ErpOrders();
            erpOrders1.setErpOrdersId(erpOrders.getErpOrdersId());
            erpOrders1.setTrackStatus(status);
            erpOrdersService.updateTrackmoreStatus(erpOrders1);
        }else{
            //先判断发起国
            if(origin_info != null){
                JSONArray trackinfo = origin_info.getJSONArray("trackinfo");
                if(trackinfo == null){
                    return erpOrders;
                }
                for (int i=trackinfo.size()-1;i>=0;i--){
                    status = getStatus(status,statusList, trackinfo.getJSONObject(i),erpOrders);
                }
            }
            if(destination_info != null){
                //再判断目的国  发起国的状态会被目的国覆盖
                JSONArray destinationinfo = destination_info.getJSONArray("trackinfo");
                if(destinationinfo == null){
                    return erpOrders;
                }
                for (int i=destinationinfo.size()-1;i>=0;i--){
                    status = getStatus(status,statusList, destinationinfo.getJSONObject(i),erpOrders);
                }
            }
        }
        if(task.getTaskStatus() == 5 && status != 5){//判断当任务请求标记为结束时，但是根据渠道状态匹配不到物流结束的状态
            status = 5;
            try{
                String string = "";
                String string1 = "";
                if(destination_info != null && destination_info.getJSONArray("trackinfo") != null)
                    string = destination_info.getJSONArray("trackinfo").getJSONObject(0).getString("Date");
                if(origin_info != null && origin_info.getJSONArray("trackinfo") != null)
                    string1 = origin_info.getJSONArray("trackinfo").getJSONObject(0).getString("Date");

                if(!StringUtils.isEmpty(string) && !StringUtils.isEmpty(string1)){//计算时间取最大的时间
                    long time1 = DateUtils.formmatStr(string1, "yyyy-MM-dd HH:mm").getTime();
                    long time = DateUtils.formmatStr(string, "yyyy-MM-dd HH:mm").getTime();
                    erpOrders.setDeliveredDate(
                            time1 > time?
                            DateUtils.formmatStr(string1,"yyyy-MM-dd HH:mm")
                            :DateUtils.formmatStr(string,"yyyy-MM-dd HH:mm"));
                }else if(!StringUtils.isEmpty(string)){
                    erpOrders.setDeliveredDate(DateUtils.formmatStr(string,"yyyy-MM-dd HH:mm"));
                }else if(!StringUtils.isEmpty(string1)){
                    erpOrders.setDeliveredDate(DateUtils.formmatStr(string1,"yyyy-MM-dd HH:mm"));
                }else{
                    erpOrders.setDeliveredDate(DateUtils.formmatStr(string1,"yyyy-MM-dd HH:mm"));
                }
            }catch (Exception ex){
                log.error("任务请求标记为结束时，但是未匹配妥投时间，计算异常");
            }
        }
        erpOrders.setTrackStatus(status);
        erpOrders.setTrackStatus(getExceptionStatus(status, minus));
        return erpOrders;
    }

    private Integer getStatus(Integer status, List<LogisticsStatus> statusList, JSONObject jsonObject, ErpOrders erpOrders){
        String statusDescription = jsonObject.getString("StatusDescription") + "," +jsonObject.getString("Details");

        //state=14 未妥投 判断response 是否含有DELIVERE DSHIPMENT DELIVERED时间为 144
        if(status != 5 && splitStatus(statusList.get(4),statusDescription)){//144
            status = ExceptionEnum.TRACKMORE_STATUS5.getValue();//已妥投
            try{erpOrders.setDeliveredDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if(status != 4 && splitStatus(statusList.get(3),statusDescription)){//120
            status = ExceptionEnum.TRACKMORE_STATUS4.getValue();//落地
            try{erpOrders.setLandingDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if(status != 3 && splitStatus(statusList.get(2),statusDescription) ){//96
            status = ExceptionEnum.TRACKMORE_STATUS3.getValue();//交航
            try{ erpOrders.setTrafficDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if(status != 2 && splitStatus(statusList.get(1),statusDescription)){//72
            status = ExceptionEnum.TRACKMORE_STATUS2.getValue();//封发
            try{erpOrders.setBaleDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if(status != 1 && splitStatus(statusList.get(0),statusDescription)){//48
            status = ExceptionEnum.TRACKMORE_STATUS1.getValue();//上网
            try{erpOrders.setInternetDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }
        return status;
    }

    private boolean splitStatus(LogisticsStatus logisticsStatus,String desc){
        String excludeKeyword = logisticsStatus.getExcludeKeyword();
        if(!StringUtils.isEmpty(excludeKeyword)){
            String[] excludeKeywords = excludeKeyword.split("/");
            for(int i=0;i<excludeKeywords.length;i++){
                if(desc.contains(excludeKeywords[i])){//如果这条记录包含了要排除的某个关键字就 排除这条记录
                    return false;
                }
            }
        }
        String keyword = logisticsStatus.getKeyword();
        if(!StringUtils.isEmpty(keyword)){
            String[] split = keyword.split("/");
            for(int i=0;i<split.length;i++){
                if(desc.contains(split[i])){//如果包含了某个关键字，就确定该状态
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断状态异常
     * @param status
     * @param minus
     * @return
     */
    private Integer getExceptionStatus(Integer status, Long minus){
        /*if(status == 1 && minus >= 72 * 3600 * 1000){
            status = 11;//上网了 未封发
        }else if(status == 2 && minus >= 96 * 3600 * 1000){
            status = 12;//封发了 未交航
        }else if(status == 3 && minus >= 120 * 3600 * 1000){
            status = 13;//交航 未落地
        }else if(status == 4 && minus >= 144 * 3600 * 1000){
            status = 14;//落地 未妥投
        }else if(status == 803 && minus >= 48 * 3600 * 1000){
            status = 14;//上网
        }*/
        return status;
    }

    /**
     * 切分注册数据
     * 40个账号切分一次
     * @param registerTasks
     * @return
     */
    private List<List<RequestTask>> splitTraceMore(List<RequestTask> registerTasks) {
        if (registerTasks == null || registerTasks.size() == 0) {
            return null;
        }
        ArrayList<List<RequestTask>> result = new ArrayList<List<RequestTask>>();
        ArrayList<RequestTask> ordersList = new ArrayList<RequestTask>();
        for (int i = 0; i < registerTasks.size(); i++) {
            RequestTask task = registerTasks.get(i);
            ordersList.add(task);
            //满40个使用另外一个ordersList
            if (ordersList.size() == 40) {
                result.add(ordersList);
                ordersList = new ArrayList<RequestTask>();
            }
        }
        if (ordersList.size() != 0) {
            result.add(ordersList);
        }
        return result;
    }

    /**
     * 生成 track more接口的请求json
     *
     * @param registerTasks
     * @return
     */
    private String createRequestData(List<RequestTask> registerTasks) {
        if(CollectionUtils.isEmpty(registerTasks))
            return "";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < registerTasks.size(); i++) {
            RequestTask task = registerTasks.get(i);
            sb.append("{\"tracking_number\": \"" + task.getTrackingNumber() + "\",\"carrier_code\":\"" + task.getCarrierCode() + "\"}");
            if(i != registerTasks.size() - 1)
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

}
