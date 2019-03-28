package com.yks.bigdata.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dao.TrackXgdhlInfoMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackXgdhlInfo;
import com.yks.bigdata.service.trackmore.impl.RequestTaskServiceImpl;
import com.yks.bigdata.util.HttpRequestUtils;
import com.yks.bigdata.util.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by liuxing on 2017/9/19.
 * 香港DHL接口对接
 */
@Service
public class ChannelXGDHLService {

    @Autowired
    SystemExceptionMapper systemExceptionMapper;

    @Autowired
    RequestTaskServiceImpl requestTaskService;

    @Autowired
    TrackXgdhlInfoMapper trackXgdhlInfoMapper;

    @Value("${fetch.thread.size}")
    private int threadSize;//线程池大小

    private final static String TOKENURL = "https://api.dhlecommerce.dhl.com/rest/v1/OAuth/AccessToken?clientId=MjUyNzU1MDU0&password=MjAzMDI5MTU&returnFormat=json";
    private final static String QUERYURL = "https://api.dhlecommerce.dhl.com/rest/v2/Tracking";

    private static Map<String,String> headers = new HashMap<>();
    static {
        headers.put("Content-Type", "application/json");
    }

    /**
     * 获取香港dhl的token，24小时失效
     * @return
     */
    public String getToken(){
        ResponseData responseData = HttpRequestUtils.sendHttpGet(TOKENURL, headers);
        JSONObject jsonObject = JSON.parseObject(responseData.getResponseText());
        return jsonObject.getJSONObject("accessTokenResponse").getString("token");
    }

    public void queryDHL(){
        String token = getToken();
        if(StringUtils.isEmpty(token)){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        Future<Object> submit = null;
        systemExceptionMapper.insert(new SystemException("trackmore fetch LWE data begin", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        RequestTask requestTask = new RequestTask();
        requestTask.setChannel("'香港DHL(PKG)'");
        //requestTask.setTaskStatus(2);
        Integer integer = requestTaskService.selectRequestTasksCount(requestTask);
        int count = integer%50==0?integer/50:integer/50 + 1;
        for(int i=1;i<=count;i++){
            PageHelper.startPage(i, 50, false);
            List<RequestTask> queryTasks = requestTaskService.selectRequestTasks(requestTask);
            if(CollectionUtils.isEmpty(queryTasks)){
                continue;
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (RequestTask task:queryTasks) {
                stringBuffer.append("\"").append(task.getTrackingNumber()).append("\"").append(",");
            }
            final String param = "{\"trackItemRequest\":{\"trackingReferenceNumber\":["+ stringBuffer.toString().substring(0,  stringBuffer.toString().length() - 1) +"],\"messageLanguage\": \"zh_CN\",\"messageVersion\":\"1.1\",\"token\":\""+ token +"\"}}";
            submit = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    sendRequestQueryDHLApi(param);
                    return null;
                }
            });
        }
        try{submit.get();}catch (Exception ex){}
        executorService.shutdownNow();
        systemExceptionMapper.insert(new SystemException("trackmore fetch LWE data end", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }

    /**
     *
     * @param param
     */
    public void sendRequestQueryDHLApi(String param){
        try {
            ResponseData responseData = HttpRequestUtils.sendHttpPostByStringEntity(QUERYURL, param, headers);
            JSONObject jsonObject = JSON.parseObject(responseData.getResponseText());
            Integer responseCode = jsonObject.getJSONObject("trackItemResponse").getInteger("responseCode");
            if(responseCode != 0){
                systemExceptionMapper.insert(new SystemException("香港DHL接口解析失败", responseData.getResponseText() + "\n" + param));
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONObject("trackItemResponse").getJSONArray("items");
            for (int i=0;i<jsonArray.size();i++) {
                List<TrackXgdhlInfo> list = new ArrayList<>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String shipmentID = jsonObject1.getString("shipmentID");
                String trackingID = jsonObject1.getString("trackingID");
                String consignmentNoteNumber = jsonObject1.getString("consignmentNoteNumber");
                String countryCode = jsonObject1.getJSONObject("destination").getString("countryCode");
                Double weight = jsonObject1.getDouble("weight");
                String weightUnit = jsonObject1.getString("weightUnit");

                JSONArray events = jsonObject1.getJSONArray("events");
                String orderNumber = jsonObject1.getString("orderNumber");
                for(int j=0;j<events.size();j++){
                    TrackXgdhlInfo trackXgdhlInfo = new TrackXgdhlInfo();
                    trackXgdhlInfo.setShipmentId(shipmentID);
                    trackXgdhlInfo.setTrackingId(trackingID);
                    trackXgdhlInfo.setOrderNumber(orderNumber);
                    trackXgdhlInfo.setConsignmentNoteNumber(consignmentNoteNumber);
                    trackXgdhlInfo.setCountryCode(countryCode);
                    trackXgdhlInfo.setWeight(weight);
                    trackXgdhlInfo.setWeightunit(weightUnit);
                    trackXgdhlInfo.setCreateTime(new Date());

                    trackXgdhlInfo.setStatus(events.getJSONObject(j).getString("status"));
                    trackXgdhlInfo.setDescription(events.getJSONObject(j).getString("description"));
                    trackXgdhlInfo.setTimestamp(events.getJSONObject(j).getDate("timestamp"));
                    trackXgdhlInfo.setTimezone(events.getJSONObject(j).getString("timezone"));
                    JSONObject address = events.getJSONObject(j).getJSONObject("address");
                    String city = address.getString("city");
                    String postalCode = address.getString("postalCode");
                    String state = address.getString("state");
                    String countryCode1 = address.getString("countryCode");
                    trackXgdhlInfo.setAddress(city==null?"":city + "\n" + postalCode==null?"":postalCode + "\n" + state==null?"":state + "\n" + countryCode1==null?"":countryCode1);
                    list.add(trackXgdhlInfo);
                }
                saveLWEData(shipmentID, list);
            }
        }catch (Exception ex){
            StringBuffer sb = new StringBuffer();
            sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append("\n");
            sb.append("url：" + param).append("\n");
            sb.append("香港DHL接口查询失败:" + ex.getLocalizedMessage()).append("\n");
            systemExceptionMapper.insert(new SystemException("香港DHL接口解析失败", sb.toString()));
            ex.printStackTrace();
        }
    }


    @Transactional(value = "transactionManager")
    public void saveLWEData(String trackCode, List<TrackXgdhlInfo> list){
        trackXgdhlInfoMapper.deleteByTrackNum(trackCode);
        trackXgdhlInfoMapper.addBatch(list);
    }

}
