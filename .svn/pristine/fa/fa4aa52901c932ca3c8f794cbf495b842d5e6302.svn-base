package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.yks.bigdata.common.ExceptionEnum;
import com.yks.bigdata.common.LoopProcessConstant;
import com.yks.bigdata.dao.Erp_ordersMapper;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dao.TrackmoreFetchChannelMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.*;
import com.yks.bigdata.service.trackmore.*;
import com.yks.bigdata.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @Autowired
    Erp_ordersMapper erp_ordersMapper;

    @Autowired
    CalculaImpl calcula;

    @Autowired
    private TrackmoreFetchChannelMapper trackmoreFetchChannelMapper;

    @Value("${fetch.thread.size}")
    private int threadSize;//线程池大小
    @Value("${fetch.type}")
    private int fetchType;//抓取方式
    @Value("${fetch.calcula.type}")
    private int calculaType;//计算方式

    public int getFetchType() {
        return fetchType;
    }

    public void setFetchType(int fetchType) {
        this.fetchType = fetchType;
    }

    public int getThreadSize() {
        return threadSize;
    }

    public void setThreadSize(int threadSize) {
        this.threadSize = threadSize;
    }

    @Override
    public void queryTrackmore() {
        if (!LoopProcessConstant.bo) {
            LoopProcessConstant.started();
            if(fetchType == 1){//POST /trackings/realtime	POST	获取运单号的实时跟踪结果
                queryTrackmore1();
            }else if(fetchType == 2){//GET /trackings/get	GET	获取多个运单号的物流信息
                queryTrackmoreBatchAccount();
            }else if(fetchType == 3){//根据物流号批量查询
                queryTrackmoreBatchOrders();
            }
            LoopProcessConstant.stoped();
            systemExceptionMapper.insert(new SystemException("trackmore fetch data end", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        }
    }

    /**
     * 调用的实时查询接口
     * 查询trackmore_fetch_channel表的渠道来获取数据，去请求trackmore
     */
    private void queryTrackmore1() {
        systemExceptionMapper.insert(new SystemException("trackmore fetch data begin", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        //LoopSearchServiceNewImpl.getStartParm().getStatus()
        List<TrackmoreFetchChannel> trackmoreFetchChannels = trackmoreFetchChannelMapper.selectByStatus(0);
        if(CollectionUtils.isEmpty(trackmoreFetchChannels)){
            LoopProcessConstant.stoped();
            systemExceptionMapper.insert(new SystemException("trackmore fetch data end", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
            return;
        }
        //1、更新正在抓取状态
        TrackmoreFetchChannel trackmoreFetchChannel = trackmoreFetchChannels.get(0);
        trackmoreFetchChannel.setFetchStatus(1);
        trackmoreFetchChannelMapper.updateByPrimaryKey(trackmoreFetchChannel);

        //0.3获取所有任务池中的所有需要查询的requst_task数据 (request_task 表 查询条件task_status 为2 )
        RequestTask requestTask = new RequestTask();
        requestTask.setChannel("'" + trackmoreFetchChannel.getChannel() + "'");
        requestTask.setTaskStatus(2);
        Integer integer = requestTaskService.selectRequestTasksCount(requestTask);
        int count = integer%10000==0?integer/10000:integer/10000 + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        for(int i=1;i<=count;i++){
            PageHelper.startPage(i, 10000, true);
            List<RequestTask> queryTasks = requestTaskService.selectRequestTasks(requestTask);
            //List<RequestTask> queryTasks = requestTaskService.selectRequestTaskByPickup();
            //查询
            for (int j = 0;j<queryTasks.size();j++) {
                try{Thread.sleep(100);}catch (Exception ex){}
                final RequestTask task1 = queryTasks.get(j);
                Future<Object> submit = executorService.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        queryTrackMoreInfo(task1);
                        return null;
                    }
                });
                if(j%threadSize==0 || i==count)
                    try{submit.get();}catch (Exception ex){}
            }
        }
        executorService.shutdownNow();
        trackmoreFetchChannel.setFetchStatus(10);
        trackmoreFetchChannelMapper.updateByPrimaryKey(trackmoreFetchChannel);
    }
    /**
     * 查询实时物流信息
     * @param task
     * @throws Exception
     */
    private void queryTrackMoreInfo(RequestTask task){
        String requestData = "{\"tracking_number\":\"" + task.getTrackingNumber() + "\",\"carrier_code\":\"" + task.getCarrierCode() + "\",\"lang\":\"cn\"}";
        TrackmoreAccount trackmoreAccount = trackmoreAccountService.selectByAccount(task.getTrackAccount());
        String responseData = null;
        try{
            //处理trackmore查询异常，如果异常则处理下一条记录
            responseData = trackerSite.queryInfo(requestData, trackmoreAccount.getApiKey());
        }catch (Exception ex){
            log.error("trackmore查询异常" + ex.getLocalizedMessage());
            systemExceptionMapper.insert(new SystemException("trackmore查询异常:" + task.getTrackingNumber(), ex.getLocalizedMessage() + "请求数据：" + requestData));
            return;
        }
        if(StringUtils.isEmpty(responseData)){
            systemExceptionMapper.insert(new SystemException("trackmore查询异常:" + task.getTrackingNumber(), "查询数据为空"));
            return;
        }
        try{
            JSONObject item = JSON.parseObject(responseData).getJSONObject("data").getJSONArray("items").getJSONObject(0);
            processTrackmoreJson(item, task);
        }catch (Exception ex){
            log.error("trackmore查询异常" + ex.getLocalizedMessage());
            systemExceptionMapper.insert(new SystemException("解析trackmore异常:" + task.getTrackingNumber(), ex.getLocalizedMessage() + "数据：" + responseData));
            return;
        }
    }

    /**
     * 批量查询账号 翻页查询数据
     */
    private void queryTrackmoreBatchAccount() {
        systemExceptionMapper.insert(new SystemException("trackmore fetch batch data begin", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        TrackmoreAccount account = new TrackmoreAccount();
        account.setStatus(1);
        PageHelper.orderBy("data_count");
        List<TrackmoreAccount> trackmoreAccounts = trackmoreAccountService.selectTrackmoreAccount(account);//查询已启用的账号
        for (int i=0;i<trackmoreAccounts.size();i++){
            String requestData1 =  "?page=1&limit=1&lang=cn";
            String responseData1 = null;
            //trackmore 接口批量查询一次2000 这次查询是为了获取分页总数
            try{responseData1 = trackerSite.queryInfoBatch(requestData1,trackmoreAccounts.get(i).getApiKey());}catch (Exception ex){}
            if(StringUtils.isEmpty(responseData1)){
                systemExceptionMapper.insert(new SystemException("trackmore fetch batch data page", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + requestData1));
                return;
            }
            //该账户的分页总数
            int pagetotal = JSON.parseObject(responseData1).getJSONObject("data").getIntValue("total");
            pagetotal = pagetotal%2000==0?pagetotal/2000:pagetotal/2000+1;
            for (int j=1;j<=pagetotal;j++){
                String requestData =  "?lang=cn&page="+ j +"&limit=" + 2000;
                try{
                    processBathDataByAccount(requestData, trackmoreAccounts.get(i));
                }catch (IOException ioEx){
                    try{Thread.sleep(1000);}catch (Exception exa){}
                    //如果异常，再处理一遍
                    try{processBathDataByAccount(requestData, trackmoreAccounts.get(i));}catch (Exception ex){}
                }catch (Exception ex){
                    log.error("trackmore批量查询异常" + ex.getLocalizedMessage());
                    systemExceptionMapper.insert(new SystemException("trackmore批量查询异常:", ex.getLocalizedMessage()));
                    try{Thread.sleep(1000);}catch (Exception exa){}
                }
            }
        }
        systemExceptionMapper.insert(new SystemException("trackmore fetch batch data end", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }
    private void processBathDataByAccount(String requestData, TrackmoreAccount trackmoreAccount) throws Exception{
        String responseData = trackerSite.queryInfoBatch(requestData,trackmoreAccount.getApiKey());
        if(StringUtils.isEmpty(responseData)){
            Thread.sleep(1000);
            return;
        }
        JSONObject rootObject = JSON.parseObject(responseData);
        JSONArray jsonArray = rootObject.getJSONObject("data").getJSONArray("items");
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        Future<Object> submit = null;
        for(int z=0;z<jsonArray.size();z++){
            RequestTask task1 = new RequestTask();
            final JSONObject jsonObject = jsonArray.getJSONObject(z);
            String tracking_number = jsonObject.getString("tracking_number");
            task1.setTrackingNumber(tracking_number);
            List<RequestTask> requestTasks = requestTaskService.selectRequestTasks(task1);//检查物流单号是否在系统中
            if(CollectionUtils.isEmpty(requestTasks)){
                continue;
            }
            final RequestTask task2 = requestTasks.get(0);
            submit = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    processTrackmoreJson(jsonObject, task2);
                    return null;
                }
            });
        }
        submit.get();
        executorService.shutdownNow();
    }
    /**
     * 根据账号查询trackmore
     */
    private void queryTrackmoreBatchOrders() {
        systemExceptionMapper.insert(new SystemException("trackmore fetch batch data begin", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        TrackmoreAccount account1 = new TrackmoreAccount();
        account1.setStatus(1);
        List<TrackmoreAccount> trackmoreAccounts = trackmoreAccountService.selectTrackmoreAccount(account1);
        for (TrackmoreAccount account:trackmoreAccounts){
            processBatchOrders(account);
        }
        systemExceptionMapper.insert(new SystemException("trackmore fetch batch data end", "" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }
    public void processBatchOrders(TrackmoreAccount account){
        RequestTask requestTask = new RequestTask();
        requestTask.setTaskStatus(2);
        requestTask.setTrackAccount(account.getAccount());
        //查询这个账号下有多少记录需要去trackmore查询
        Integer integer = requestTaskService.selectRequestTasksCount(requestTask);
        int count = integer%40==0?integer/40:integer/40 + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        for(int i=1;i<=count;i++){
            PageHelper.startPage(i, 40, false);
            final List<RequestTask> queryTasks = requestTaskService.selectRequestTasks(requestTask);//每次查询40条记录
            final String accountKey = account.getApiKey();
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    queryTrackmore40(queryTasks, accountKey);
                    return null;
                }
            });
        }
    }
    public void queryTrackmore40(List<RequestTask> queryTasks,String accountKey){
        try{
            String requestData = "?lang=cn";
            StringBuffer stringBuffer = new StringBuffer();
            Map<String, RequestTask> map = new HashMap<>();
            for(RequestTask requestTask : queryTasks){
                stringBuffer.append(requestTask.getTrackingNumber()).append(",");
                map.put(requestTask.getTrackingNumber(), requestTask);
            }
            if(StringUtils.isEmpty(stringBuffer)){
                return;
            }
            requestData += "&numbers=" + stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
            String responseData = trackerSite.queryInfoBatch(requestData, accountKey);
            if(StringUtils.isEmpty(responseData)){
                return;
            }
            JSONArray jsonArray = JSON.parseObject(responseData).getJSONObject("data").getJSONArray("items");
            for (int i = 0;i<jsonArray.size();i++){
                String trackNum = jsonArray.getJSONObject(i).getString("tracking_number");
                if(map.get(trackNum) == null){continue;}
                processTrackmoreJson(jsonArray.getJSONObject(i), map.get(trackNum));
            }
        }catch (Exception ex){
            ex.printStackTrace();
            log.error("trackmore batch query fail" + ex.getLocalizedMessage());
        }
    }











    @Override
    public void processTrackmoreJson(JSONObject item, RequestTask task) throws Exception{
        if(item == null || StringUtils.isEmpty(item.toJSONString())){
            return;//抓取失败不做任何动作，下次继续抓取
        }
        RequestTask task1 = new RequestTask();
        task1.setId(task.getId());
        task1.setFetchTime(new Date());
        requestTaskService.updateByPrimaryKeySelective(task1);//更新抓取时间
        if(isFinish(item)){
            task.setTaskStatus(5); //2.1.1 request_task标记该条数据task_status=5 表示已经完结
            requestTaskService.updateStatusFinish(task.getId());//任务标记为已完结
        }
        saveTrackJson(task, item);
    }

    /**
     * 判断是否完结
     * 判断标准 返回的json串中的status为delivered
     * @param item
     * @return
     */
    private boolean isFinish(JSONObject item) {
        String status = item.getString("status");
        ErpOrders orders = new ErpOrders();
        orders.setOrdersMailCode(item.getString("tracking_number"));
        if (StringUtils.equals(status, "delivered")) {
            return true;
        } else  if (StringUtils.equals(status, "notfound")) {//查询不到
            orders.setExceptionStatus(ExceptionEnum.TRACKMORE_NOT_FOUND.getValue());
            erp_ordersMapper.updateExceptionStatus(orders);
            return false;
        } else  if (StringUtils.equals(status, "undelivered")) {//妥投失败
            orders.setExceptionStatus(ExceptionEnum.trackmore_undelivered.getValue());
            erp_ordersMapper.updateExceptionStatus(orders);
            return false;
        }else  if (StringUtils.equals(status, "exception")) {//异常
            orders.setExceptionStatus(ExceptionEnum.trackmore_exception.getValue());
            erp_ordersMapper.updateExceptionStatus(orders);
            return false;
        }else  if (StringUtils.equals(status, "expired")) {//运输过久
            orders.setExceptionStatus(ExceptionEnum.trackmore_expired.getValue());
            erp_ordersMapper.updateExceptionStatus(orders);
            return false;
        }else  if (StringUtils.equals(status, "pickup")) {//到达代取
            orders.setExceptionStatus(ExceptionEnum.trackmore_pickup.getValue());
            erp_ordersMapper.updateExceptionStatus(orders);
            return false;
        } else{
            return false;
        }
    }

    private void saveTrackJson(RequestTask task, JSONObject item) throws Exception {
        //未完结
        //2.2.1 将结果写入或者更新 track_info 和 track_info_detail
        infoService.saveOrUpdateInfoAndDetail(task, item);
        //2.2.2 将结果写入track_source中
        //trackSourceService.add(task, item.toJSONString());
        //2.2.3.依据状态表logistics_status中的状态标记,进行标记数据,erp_orders字段`track_status`标记对应的值
        switch (calculaType){
            case 1:
                judeStateException(task, item);
                break;
            case 2:
                calcula.judeStateException(task, item);
                break;
        }
    }

    private int processTrackmoreSource(List<JSONObject> jsonObjects, int status, List<LogisticsStatus> statusList, ErpOrders erpOrders){
        for (int i=jsonObjects.size()-1;i>=0;i--){//因为数据是倒序的，现在倒着遍历就成 顺序的
            status = getStatus(status,statusList, jsonObjects.get(i),erpOrders);
            reckonDate(erpOrders, jsonObjects, status);
        }
        return status;
    }

    /**
     * 推算5个状态的具体时间
     * @param erpOrders
     * @param jsonObjects
     * @param status
     */
    private void reckonDate(ErpOrders erpOrders, List<JSONObject> jsonObjects, int status){
        if(status == 1 && erpOrders.getInternetDate() == null ){
            try{erpOrders.setInternetDate(DateUtils.formmatStr(jsonObjects.get(jsonObjects.size() - 1).getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){}
        }else if(status == 2 && erpOrders.getInternetDate() == null ){
            erpOrders.setInternetDate(this.getDate(erpOrders.getBaleDate(), jsonObjects));
        }else if(status == 3){
            if(erpOrders.getBaleDate() == null){
                erpOrders.setBaleDate(this.getDate(erpOrders.getTrafficDate(), jsonObjects));
            }
            if(erpOrders.getInternetDate() == null){
                erpOrders.setInternetDate(this.getDate(erpOrders.getBaleDate(), jsonObjects));
            }
        }else if(status == 4){
            if(erpOrders.getTrafficDate() == null){
                erpOrders.setTrafficDate(this.getDate(erpOrders.getLandingDate(), jsonObjects));
            }
            if(erpOrders.getBaleDate() == null){
                erpOrders.setBaleDate(this.getDate(erpOrders.getTrafficDate(), jsonObjects));
            }
            if(erpOrders.getInternetDate() == null){
                erpOrders.setInternetDate(this.getDate(erpOrders.getBaleDate(), jsonObjects));
            }
        }else if(status == 5){
            if(erpOrders.getLandingDate() == null){
                erpOrders.setLandingDate(this.getDate(erpOrders.getDeliveredDate(), jsonObjects));
            }
            if(erpOrders.getTrafficDate() == null){
                erpOrders.setTrafficDate(this.getDate(erpOrders.getLandingDate(), jsonObjects));
            }
            if(erpOrders.getBaleDate() == null){
                erpOrders.setBaleDate(this.getDate(erpOrders.getTrafficDate(), jsonObjects));
            }
            if(erpOrders.getInternetDate() == null){
                erpOrders.setInternetDate(this.getDate(erpOrders.getBaleDate(), jsonObjects));
            }
        }
    }

    public Date getDate(Date dateParm,List<JSONObject> jsonObjects){
        String dateStr = DateFormatUtils.format(dateParm, "yyyy-MM-dd HH:mm");
        for (int i=0;i<jsonObjects.size();i++){
            try{
                Date date = DateUtils.formmatStr(jsonObjects.get(i).getString("Date"), "yyyy-MM-dd HH:mm");
                String dateStr1 = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm");
                if(dateStr1.equals(dateStr)){
                    return DateUtils.formmatStr(jsonObjects.get(i+1).getString("Date"), "yyyy-MM-dd HH:mm");
                }
            }catch (Exception ex){}
        }
        return null;
    }

    /**
     * 排序
     * 数据 时间从大到小
     * @param item
     * @throws Exception
     */
    public List<JSONObject> sortDate(JSONObject item)throws Exception{
        //检查是否有物流信息
        JSONObject origin_info = item.getJSONObject("origin_info");
        JSONObject destination_info = item.getJSONObject("destination_info");
        if(origin_info == null && destination_info == null){//无信息
            return null;
        }
        List<JSONObject> jsonObjects = new ArrayList<>();
        if(origin_info != null){
            JSONArray trackinfo = origin_info.getJSONArray("trackinfo");
            for (int i=0;trackinfo != null && i<trackinfo.size();i++){
                jsonObjects.add(trackinfo.getJSONObject(i));
            }
        }
        if(destination_info != null){
            JSONArray trackinfo1 = destination_info.getJSONArray("trackinfo");
            for (int i=0;trackinfo1 != null && i<trackinfo1.size();i++){
                jsonObjects.add(trackinfo1.getJSONObject(i));
            }
        }
        if(CollectionUtils.isEmpty(jsonObjects)){
            return null;
        }
        for (int i=0;i<jsonObjects.size();i++){
            long dateLong = 0l;
            try{dateLong = DateUtils.formmatStr(jsonObjects.get(i).getString("Date"), "yyyy-MM-dd HH:mm").getTime();}catch (Exception ex){}
            for(int j=0;j<jsonObjects.size();j++){
                long date = 0l;
                try{date = DateUtils.formmatStr(jsonObjects.get(j).getString("Date"), "yyyy-MM-dd HH:mm").getTime();}catch (Exception ex){}
                if(date < dateLong || dateLong == 0l){
                    JSONObject object2 = jsonObjects.get(i);
                    jsonObjects.set(i, jsonObjects.get(j));
                    jsonObjects.set(j, object2);
                }
            }
        }
        return jsonObjects;
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
     * @param item
     */
    private ErpOrders judeStateException( RequestTask task, JSONObject item) throws Exception {
        ErpOrders erpOrders = erpOrdersService.selectOrderByOrderId(task.getErpOrdersId());
        Long minus = System.currentTimeMillis() - erpOrders.getOrdersExportTime().getTime();
        //检查渠道是否有匹配状态
        List<LogisticsStatus> logisticsStatuses = informactionService.selectByChannel(task.getChannel());
        if (CollectionUtils.isEmpty(logisticsStatuses)){
            log.info("该request task 没有查找到合适的状态 (上网 交航等) : request task id为:" + task.getId() + "\n 返回的json串为:" + item.toJSONString());
            erpOrders.setTrackStatus(ExceptionEnum.NOT_FOUND_LOGISTICS_STATUS.getValue());
            erpOrdersService.updateTrackmoreStatus(erpOrders);
            return erpOrders;
        }
        //冒泡排序记录和判断信息是否为空
        List<JSONObject> jsonObjects = this.sortDate(item);
        if(CollectionUtils.isEmpty(jsonObjects)){
            return erpOrders;
        }
        int status = erpOrders.getTrackStatus();
        status = this.processTrackmoreSource(jsonObjects, status, logisticsStatuses, erpOrders);
        if(task.getTaskStatus() == 5 || status == 5){//判断当任务请求标记为结束时，但是根据渠道状态匹配不到物流结束的状态
            status = 5;
            try{
                erpOrders.setDeliveredDate(DateUtils.formmatStr(jsonObjects.get(0).getString("Date"),"yyyy-MM-dd HH:mm"));
            }catch (Exception ex){
                log.error("任务请求标记为结束时妥投时间计算异常");
                systemExceptionMapper.insert(new SystemException("任务请求标记为结束时妥投时间计算异常:" + task.getTrackingNumber(), ex.getLocalizedMessage() + "数据：" + item.toJSONString()));
            }
        }
        erpOrders.setTrackStatus(status);
        //erpOrders.setExceptionStatus(getExceptionStatus(status, minus));

        erpOrdersService.updateErpOrders(erpOrders);//标记状态 和时间
        return erpOrders;
    }

    private Integer getStatus(Integer status, List<LogisticsStatus> statusList, JSONObject jsonObject, ErpOrders erpOrders){
        String statusDescription = jsonObject.getString("StatusDescription") + "," +jsonObject.getString("Details");
        //state=14 未妥投 判断response 是否含有DELIVERE DSHIPMENT DELIVERED时间为 144
        if(splitStatus(statusList.get(0),statusDescription)){
            status = ExceptionEnum.TRACKMORE_STATUS1.getValue();//上网
            try{erpOrders.setInternetDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if(splitStatus(statusList.get(1),statusDescription)){
            status = ExceptionEnum.TRACKMORE_STATUS2.getValue();//封发
            try{erpOrders.setBaleDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if(splitStatus(statusList.get(2),statusDescription)){
            status = ExceptionEnum.TRACKMORE_STATUS3.getValue();//交航
            try{ erpOrders.setTrafficDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if( splitStatus(statusList.get(3),statusDescription)){
            status = ExceptionEnum.TRACKMORE_STATUS4.getValue();//落地
            try{erpOrders.setLandingDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }else if(splitStatus(statusList.get(4),statusDescription)){
            status = ExceptionEnum.TRACKMORE_STATUS5.getValue();//已妥投
            try{erpOrders.setDeliveredDate(DateUtils.formmatStr(jsonObject.getString("Date"),"yyyy-MM-dd HH:mm"));}catch (Exception ex){log.error("判断状态时间转换异常");}
        }
        return status;
    }

    /**
     * 计算状态匹配
     * @param logisticsStatus
     * @param desc
     * @return
     */
    public boolean splitStatus(LogisticsStatus logisticsStatus,String desc){
        String excludeKeyword = logisticsStatus.getExcludeKeyword();
        if(!StringUtils.isEmpty(excludeKeyword)){
            String[] excludeKeywords = excludeKeyword.split("/");
            for(int i=0;i<excludeKeywords.length;i++){
                if(desc.toLowerCase().contains(excludeKeywords[i].trim().toLowerCase())){//如果这条记录包含了要排除的某个关键字就 排除这条记录
                    return false;
                }
            }
        }
        String keyword = logisticsStatus.getKeyword();
        if(!StringUtils.isEmpty(keyword)){
            String[] split = keyword.split("/");
            for(int i=0;i<split.length;i++){
                if(desc.toLowerCase().contains(split[i].trim().toLowerCase())){//如果包含了某个关键字，就确定该状态
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
        int exceptionStatus = 0;
        if(status == 1 && minus >= 72 * 3600 * 1000){
            exceptionStatus = ExceptionEnum.TRACKMORE_STATUS11.getValue();//上网了 未封发
        }else if(status == 2 && minus >= 96 * 3600 * 1000){
            exceptionStatus = ExceptionEnum.TRACKMORE_STATUS12.getValue();//封发了 未交航
        }else if(status == 3 && minus >= 120 * 3600 * 1000){
            exceptionStatus = ExceptionEnum.TRACKMORE_STATUS13.getValue();//交航 未落地
        }else if(status == 4 && minus >= 144 * 3600 * 1000){
            exceptionStatus = ExceptionEnum.TRACKMORE_STATUS14.getValue();//落地 未妥投
        }else if(status == 803 && minus >= 48 * 3600 * 1000){
            exceptionStatus = ExceptionEnum.TRACKMORE_STATUS10.getValue();//上网
        }
        return exceptionStatus;
    }


}
