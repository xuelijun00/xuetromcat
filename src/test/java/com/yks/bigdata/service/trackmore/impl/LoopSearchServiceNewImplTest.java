package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import com.yks.bigdata.service.trackmore.LoopSearchService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by liuxing on 2017/7/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class LoopSearchServiceNewImplTest {

    @Autowired
    LoopSearchService loopSearchService;

    @Autowired
    TrackmoreRegisterServiceImpl registerTrackmore;

    @Autowired
    IErpOrdersService erpOrdersService;

    @Test
    public void importErpOrders() throws Exception {
        erpOrdersService.importErpOrders();
    }

    @Test
    public void registerTrackmore() throws Exception {
        registerTrackmore.registerTrackmore();
    }

    @Test
    public void queryTrackmore() throws Exception {
        loopSearchService.queryTrackmore();
    }

    @Autowired
    Tracker trackerSite;
    @Autowired
    IRequestTaskService requestTaskService;

    @Test
    public void queryTrackmore1() throws Exception {
        System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        RequestTask requestTask = requestTaskService.selectByErpOrderId(1129661255081l);
        String requestData = "{\"tracking_number\":\""+ requestTask.getTrackingNumber() +"\",\"carrier_code\":\""+ requestTask.getCarrierCode() +"\",\"lang\":\"cn\"}";
        String responseData = trackerSite.queryInfo(requestData, "aa012734-3222-461f-a8c0-92cef0d585b0");
        JSONObject item = JSON.parseObject(responseData).getJSONObject("data").getJSONArray("items").getJSONObject(0);
        loopSearchService.processTrackmoreJson(item, requestTask);
        System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void queryTrackmoreBatch() throws Exception {
        System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        /*RequestTask requestTask = requestTaskService.selectByErpOrderId(1126789089083l);
        String requestData = "{\"tracking_number\":\""+ requestTask.getTrackingNumber() +"\",\"carrier_code\":\""+ requestTask.getCarrierCode() +"\",\"lang\":\"cn\"}";*/
        String requestData = "?numbers=RW019919210CN,RW019954110CN,85512695887&lang=cn";
        String responseData = trackerSite.queryInfoBatch(requestData, "cf8ef0e0-4995-45b4-aa35-3b410ac2a99e");
        System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
    }

}