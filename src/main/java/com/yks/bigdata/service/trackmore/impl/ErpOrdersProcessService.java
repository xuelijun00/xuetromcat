package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.ErpEOrdersProcessMapper;
import com.yks.bigdata.dao.Erp_ordersMapper;
import com.yks.bigdata.dao.Logistics_statusMapper;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dto.report.NodeDto;
import com.yks.bigdata.dto.trackmore.ErpEOrdersProcess;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticReport;
import com.yks.bigdata.dto.trackmore.LogisticsStatus;
import com.yks.bigdata.util.DateUtils;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * Created by liuxing on 2017/10/11.
 */
@Service
public class ErpOrdersProcessService {

    private static Logger log = LoggerFactory.getLogger(ErpOrdersProcessService.class);

    @Autowired
    ErpEOrdersProcessMapper processMapper;

    @Autowired
    Erp_ordersMapper erpordersMapper;

    @Autowired
    Logistics_statusMapper logistics_statusMapper;

    @Autowired
    ValueOperations<String, Object> valueOperations;

    @Autowired
    SystemExceptionMapper systemExceptionMapper;

    public List<LogisticReport> getRedisAllData() {
        List<LogisticReport> logisticReports = new ArrayList<>();
        Iterator<String> iterator = valueOperations.getOperations().keys("*").iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (Pattern.matches("^.*[0-9]{4}-[0-9]{2}-[0-9]{2}$", key)) {
                LogisticReport logisticReport = (LogisticReport) valueOperations.get(key);
                if (logisticReport != null) logisticReports.add(logisticReport);
            }
        }
        return logisticReports;
    }

    public List<LogisticReport> getRedisReportData(String channel, String startTime, String endTime) {
        List<LogisticReport> logisticReports = new ArrayList<>();
        if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return logisticReports;
        }
        try {
            Date startDate = DateUtils.formmatStr(startTime, "yyyy-MM-dd");
            Date endDate = DateUtils.formmatStr(endTime, "yyyy-MM-dd");
            long l = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
            List<String> keys = new ArrayList<>();
            keys.add(channel + startTime);
            for (int i = 1; i <= l; i++) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(startDate);
                instance.add(Calendar.DAY_OF_MONTH, i);
                keys.add(channel + DateFormatUtils.format(instance.getTime(), "yyyy-MM-dd"));
            }

            for (String key : keys) {
                LogisticReport logisticReport = (LogisticReport) valueOperations.get(key);
                if (logisticReport != null) logisticReports.add(logisticReport);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("获取redis数据出错：" + ex.getLocalizedMessage());
        }
        return logisticReports;
    }

    public Map<String, Integer> getChannelCount(){
        List<NodeDto> nodeDtos = processMapper.selectNodeCount1();
        Map<String, Integer> map = new HashMap<>();
        for (NodeDto nodeDto:nodeDtos){
            map.put(nodeDto.getKeystr(), nodeDto.getCount());
        }
        return map;
    }

    public void forData(List<LogisticReport> logisticReports, final Map<String, Integer> map){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<List<NodeDto>> submit = null;
        for (final LogisticReport logisticReport : logisticReports) {
            submit = executorService.submit(new Callable<List<NodeDto>>() {
                @Override
                public List<NodeDto> call() throws Exception {
                    processNodeData(logisticReport, map);
                    return null;
                }
            });
            //processNodeData(logisticReport, map);
        }
        try{submit.get();}catch (Exception ex){}
        executorService.shutdownNow();
        while (true){
            if(executorService.isTerminated()){
                break;
            }
        }
    }

    /**
     * 计算数据
     */
    public void processData() {
        try {
            systemExceptionMapper.insert(new SystemException("redis计算任务begin", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
            List<LogisticReport> logisticReports = processMapper.selectReportCountGyChannelAndOrderOutDate(null);
            final Map<String, Integer> map = getChannelCount();
            forData(logisticReports, map);
        } catch (Exception e) {
            log.error("redis计算任务任务失败!", e);
            systemExceptionMapper.insert(new SystemException("redis计算任务任务失败", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + e.getLocalizedMessage()));
        }
        systemExceptionMapper.insert(new SystemException("redis计算任务end", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }
    public void processData(String channel,String startTime,String endTime){
        try {
            systemExceptionMapper.insert(new SystemException("redis计算任务begin:" + channel, "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
            LogisticReport logisticReport2 = new LogisticReport();
            logisticReport2.setChannelName(channel);
            logisticReport2.setStartTime(startTime);
            logisticReport2.setEndTime(endTime);
            List<LogisticReport> logisticReports = processMapper.selectReportCountGyChannelAndOrderOutDate(logisticReport2);
            final Map<String, Integer> map = getChannelCount();
            forData(logisticReports, map);
        } catch (Exception e) {
            log.error("redis计算任务任务失败!", e);
            systemExceptionMapper.insert(new SystemException("redis计算任务任务失败" + channel, "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + e.getLocalizedMessage()));
        }
        systemExceptionMapper.insert(new SystemException("redis计算任务end:" + channel, "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 处理节点计算
     *
     * @return
     */
    public void processNodeData(LogisticReport logisticReport, Map<String, Integer> map) {
        String channel = logisticReport.getChannelName();
        String ordersOutTime = logisticReport.getOrderOutTime();
        int count = logisticReport.getShipcount();
        List<LogisticsStatus> logisticsStatuses = logistics_statusMapper.selectByChannel(channel);
        if (CollectionUtils.isEmpty(logisticsStatuses)) {
            return;
        }
        List<NodeDto> list = new ArrayList<>();
        for (int i = 0; i < logisticsStatuses.size(); i++) {
            //汇总数据
            Integer integer = map.get(channel + ordersOutTime + (i+1));
            if (integer == null) {//没有数据时
                NodeDto nodeDto = new NodeDto();
                nodeDto.setNode(i + 1);
                nodeDto.setCount(0);
                nodeDto.setRate(0d);
                nodeDto.setDifferDay(0d);
                list.add(nodeDto);
                continue;
            }
            //要计算得时间节点数据
            List<NodeDto> nodeDtos = processMapper.selectNodesByChannel(i + 1, channel, ordersOutTime);
            Double differDay = 0d;
            for (NodeDto nodeDto : nodeDtos) {
                Double d = 0d;
                if (nodeDto.getProcess_time() != null) {
                    d = (nodeDto.getProcess_time().getTime() - nodeDto.getOrders_out_time().getTime()) / (24 * 60 * 60 * 1000d);
                }
                nodeDto.setDifferDay(d);
                BigDecimal a = new BigDecimal(d);
                BigDecimal b = new BigDecimal(differDay);
                differDay = a.add(b).doubleValue();
            }
            NodeDto nodeDto = new NodeDto();
            nodeDto.setNode(i + 1);
            nodeDto.setCount(integer);
            nodeDto.setRate(integer / Double.parseDouble(count + ""));
            nodeDto.setDifferDay(differDay / integer);
            list.add(nodeDto);
        }
        logisticReport.setNodeDtoList(list);
        logisticReport.setEndTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        valueOperations.set(channel + ordersOutTime, logisticReport);
    }

    /**
     * Dec:更新订单物流状态对应的时间节点
     *
     * @param
     * @return
     */
    public Map<String, Object> updateProcessTimeByOrderIdAndNodeStatus(List<CSVRecord> records) throws Exception {
        Map<String, Object> map = new HashMap<>(); //返回结果
        List<ErpEOrdersProcess> updateList = new ArrayList<>(); //用于存放更新
        /*List<ErpEOrdersProcess> insertList = new ArrayList<>(); //用于存放新增*/
        List<String> orderIdList = new ArrayList<>();   //用于存在重复的ID
        ErpEOrdersProcess process;
        StringBuilder nonOrder = new StringBuilder();   //保存不存在的订单
        StringBuilder repeatOrder = new StringBuilder();    //保存同一表格中，重复的订单
        int updateResult;
        int insertResult;
        try {
            for (CSVRecord record : records) {
                if (erpordersMapper.selectCountByOrderId(Long.valueOf(record.get(0))) > 0) {  //订单存在才修改状态
                    int recordSize = record.size();
                    if (!orderIdList.contains(record.get(0))) { //判断同一个表格中，订单信息是否会有重复
                        orderIdList.add(record.get(0));
                        for (int i = 1; i < recordSize; i++) {
                            if (record.get(i) != null && record.get(i).length() > 1) {

                                process = new ErpEOrdersProcess();
                                process.setNodeStatus(i);
                                process.setErpOrdersId(Long.valueOf(record.get(0)));
                                process.setProcessTime(DateUtils.formmatStr(record.get(i),"yyyy/m/d h:mm"));
                                if (processMapper.selectCountByOrderIdAndStatus(process.getErpOrdersId(), process.getNodeStatus()) > 0) {
                                    updateList.add(process);
                                }/* else {
                                    process.setCreateTime(new Date());
                                    insertList.add(process);
                                }*/
                            }
                        }
                    } else {    //表格中已经存在一条数据
                        repeatOrder.append(record.get(0)).append(",");
                    }
                } else {    //如果订单不存在
                    nonOrder.append(record.get(0)).append(",");
                }
            }
            if (!updateList.isEmpty()) {    //数组不为空才进行更新
                processMapper.updateProcessTimeByOrderIdAndNodeStatus(updateList);
                updateResult = updateList.size();
            } else {
                updateResult = 0;
            }
            /*if (!insertList.isEmpty()) {    //数组不为空才进行新增
                processMapper.insertOrdersProcess(insertList);
                insertResult = insertList.size();
            } else {
                insertResult = 0;
            }
            map.put("insertResult", insertResult);*/
            map.put("updateResult", updateResult);
            if (nonOrder != null && nonOrder.length() > 0) {    //有不存在的订单
                map.put("nonOrder", nonOrder.toString());
            }
            if (repeatOrder != null && repeatOrder.length() > 0) {  //有重复的订单
                map.put("repeatOrder", repeatOrder.toString());
            }
        } catch (ParseException e) {
            throw new ParseException("时间格式错误", 197);
        }
        return map;
    }

    /**
     * 根据ID获取订单，并关联物流状态节点
     *
     * @param
     * @return
     */
    public ErpOrders selectOrderDetailByOrdersMailCode(String ordersMailCode) {
        ErpOrders orders = erpordersMapper.selectByOrdersMailCode(ordersMailCode);
        List<ErpEOrdersProcess> processList = processMapper.selectOrdersProcess(orders.getErpOrdersId());
        for (ErpEOrdersProcess process : processList) {
            switch (process.getNodeStatus()) {
                case 1:
                    orders.setInternetDate(process.getProcessTime());
                    break;
                case 2:
                    orders.setBaleDate(process.getProcessTime());
                    break;
                case 3:
                    orders.setTrafficDate(process.getProcessTime());
                    break;
                case 4:
                    orders.setLandingDate(process.getProcessTime());
                    break;
                case 5:
                    orders.setDeliveredDate(process.getProcessTime());
                    break;
            }
        }
        return orders;
    }

    public void updateOrderTrackStatus(ErpOrders orders) {
        List<ErpEOrdersProcess> insertList = new ArrayList<>();
        List<ErpEOrdersProcess> updateList = new ArrayList<>();
        this.updateOrInsertProcess(orders.getInternetDate(), 1, orders.getErpOrdersId(), insertList, updateList);
        this.updateOrInsertProcess(orders.getBaleDate(), 2, orders.getErpOrdersId(), insertList, updateList);
        this.updateOrInsertProcess(orders.getTrafficDate(), 3, orders.getErpOrdersId(), insertList, updateList);
        this.updateOrInsertProcess(orders.getLandingDate(), 4, orders.getErpOrdersId(), insertList, updateList);
        this.updateOrInsertProcess(orders.getDeliveredDate(), 5, orders.getErpOrdersId(), insertList, updateList);
        if (updateList.size() > 0) {
            processMapper.updateProcessTimeByOrderIdAndNodeStatus(updateList);
        }
        if (insertList.size() > 0) {
            processMapper.insertOrdersProcess(insertList);
        }
        erpordersMapper.updateOrderOutTime(orders);
    }

    /**
     * 封装新增或更新Process方法
     *
     * @param trackTime
     * @param statusLength
     * @param orderId
     * @param insertList
     * @param updateList
     */
    public void updateOrInsertProcess(Date trackTime, int statusLength, Long orderId, List<ErpEOrdersProcess> insertList, List<ErpEOrdersProcess> updateList) {
        if (trackTime != null) {
            ErpEOrdersProcess process = new ErpEOrdersProcess();
            if (processMapper.selectCountByOrderIdAndStatus(orderId, statusLength) > 0) {
                process.setNodeStatus(statusLength);
                process.setErpOrdersId(orderId);
                process.setProcessTime(trackTime);
                updateList.add(process);
            } else {
                process = new ErpEOrdersProcess();
                process.setCreateTime(new Date());
                process.setNodeStatus(statusLength);
                process.setErpOrdersId(orderId);
                process.setProcessTime(trackTime);
                insertList.add(process);
            }
        }
    }

}
