package com.yks.bigdata.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.yks.bigdata.dto.report.TrackMain;
import com.yks.bigdata.dto.trackmore.TrackYtInfo;
import com.yks.bigdata.service.channel.ChannelSaichengService;
import com.yks.bigdata.util.DateUtils;
import com.yks.bigdata.util.HttpRequestUtils;
import com.yks.bigdata.util.MD5Util;
import com.yks.bigdata.util.ResponseData;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by liuxing on 2017/9/9.
 */
public class TestIB {

    @Test
    public void sendGETIB()throws Exception{
        //String  url = "http://api.myib.com/v4/tracking/events?packageId=1126829851083";
        //String  url = "http://api.myib.com/v4/tracking/events?trackingCode=9274890204304400274274&trackingCode=9205590204304400112339";
        String url = "http://api.myib.com/v4/tracking/events?a=1&trackingCode=9200190204304400360355&trackingCode=9200190204304400371962&trackingCode=9274890204304400360366&trackingCode=9274890204304400371980&trackingCode=9261290204304400360377&trackingCode=9200190204304400371993&trackingCode=9261290204304400360391&trackingCode=9200190204304400372204&trackingCode=9261290204304400360407&trackingCode=9274890204304400372000&trackingCode=9274890204304400361851&trackingCode=9274890204304400372017&trackingCode=9274890204304400367808&trackingCode=9200190204304400372020&trackingCode=9200190204304400361932&trackingCode=9274890204304400372031&trackingCode=9274890204304400361943&trackingCode=9274890204304400372048&trackingCode=9274890204304400362025&trackingCode=9274890204304400372055&trackingCode=9200190204304400363080&trackingCode=9274890204304400372062&trackingCode=9274890204304400367945&trackingCode=9274890204304400372086&trackingCode=9274890204304400364265&trackingCode=9274890204304400372109&trackingCode=9274890204304400364302&trackingCode=9205590204304400372160&trackingCode=9274890204304400364319&trackingCode=9274890204304400372178&trackingCode=9274890204304400364326&trackingCode=9274890204304400380098&trackingCode=9274890204304400339485&trackingCode=9200190204304400327167&trackingCode=9274890204304400338617&trackingCode=9274890204304400325761&trackingCode=9274890204304400348845&trackingCode=9200190204304400346342&trackingCode=9261290204304400344971&trackingCode=9200190204304400349787&trackingCode=9274890204304400341310&trackingCode=9274890204304400338259&trackingCode=9274890204304400344434&trackingCode=9261290204304400322467&trackingCode=9200190204304400250359&trackingCode=9205590204304400248540&trackingCode=9200190204304400247595&trackingCode=9274890204304400270610&trackingCode=9274890204304400256232&trackingCode=9200190204304400265506";
        Map<String,String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Accept", "application/json");
        map.put("ClientKey", "52IBkhQgVeQwt57eYomYSA==");
        map.put("ClientSecret", "XD6gOQJDB9YZTeLSBcNpVA==");
        map.put("MarketplaceAlias", "Ebay");
        map.put("SellerAlias", "Youkeshu");

        ResponseData responseData = HttpRequestUtils.sendHttpGet(url, map);
        System.out.println(responseData.getResponseText());
    }

    @Test
    public void testYT()throws Exception{
        String url = "http://test.edi.ytoglobal.com/outerEdiTrack/searchEdiTrack";
        Map<String,String> map = new HashMap<>();
        map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Message>\n" +
                "    <Header>\n" +
                "\t    <SeqNo>201608031015</SeqNo>\n" +
                "        <TimeStamp>2016-08-03 10:17:00</TimeStamp>\n" +
                "\t</Header>\n" +
                "\t<WaybillNos>\n" +
                "\t    <WaybillNo>1000092812,GTRZ600000117HK</WaybillNo>\n" +
                "\t\t<Language>zh_CN</Language>\n" +
                "\t</WaybillNos>\n" +
                "</Message>";
        Map<String,String> params = new HashMap<>();
        params.put("message",xml);
        params.put("sign",Base64.encode(MD5Util.encode(xml+"123456").getBytes()));
        params.put("client_id","123456");
        ResponseData responseData = HttpRequestUtils.sendHttpPost(url, params, map);
        System.out.println(responseData.getStatusCode());
        System.out.println(responseData.getResponseText());

        Document document = DocumentHelper.parseText(responseData.getResponseText());
        Element rootElement = document.getRootElement();
    }

    @Test
    public void testYT1()throws Exception{
        String url = "http://edi.ytoglobal.com/outerEdiTrack/searchEdiTrack";
        String client_id = "YKS";
        Map<String,String> map = new HashMap<>();
        map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Message>\n" +
                "    <Header>\n" +
                "\t    <SeqNo>2016080310151afdsa</SeqNo>\n" +
                "        <TimeStamp>2016-08-03 10:17:00</TimeStamp>\n" +
                "\t</Header>\n" +
                "\t<WaybillNos>\n" +
                "\t    <WaybillNo>G00151830883,G00150909215,G00317609758</WaybillNo>\n" +
                "\t\t<Language>zh_CN</Language>\n" +
                "\t</WaybillNos>\n" +
                "</Message>";
        Map<String,String> params = new HashMap<>();
        params.put("message",xml);
        params.put("sign",Base64.encode(MD5Util.encode(xml+client_id).getBytes()));
        params.put("client_id",client_id);
        ResponseData responseData = HttpRequestUtils.sendHttpPost(url, params, map);
        System.out.println(responseData.getStatusCode());
        System.out.println(responseData.getResponseText());

        Document document = DocumentHelper.parseText(responseData.getResponseText());
        Element rootElement = document.getRootElement();
    }

    @Test
    public void testJson(){
        String json = "{\"id\":\"fe517061dff08d7939046cb69e14d83a\",\"tracking_number\":\"85512695887\",\"carrier_code\":\"china-post\",\"status\":\"expired\",\"created_at\":\"2017-09-29T18:30:49+08:00\",\"updated_at\":\"2017-10-20T12:44:34+08:00\",\"order_create_time\":null,\"customer_email\":\"\",\"title\":\"\",\"order_id\":null,\"comment\":null,\"customer_name\":null,\"original_country\":\"China\",\"itemTimeLength\":62,\"stayTimeLength\":56,\"origin_info\":{\"ItemReceived\":\"2017-08-23 10:43\",\"ItemDispatched\":null,\"DepartfromAirport\":\"2017-08-25 12:51\",\"ArrivalfromAbroad\":null,\"CustomsClearance\":null,\"DestinationArrived\":null,\"weblink\":\"http:\\/\\/intmail.183.com.cn\",\"phone\":null,\"carrier_code\":\"china-post\",\"trackinfo\":[{\"Date\":\"2017-08-25 12:51\",\"StatusDescription\":\"计划交航\",\"Details\":\"\"},{\"Date\":\"2017-08-23 17:42\",\"StatusDescription\":\"离开【中国邮政集团公司苏州市大宗邮件处理中心】，下一站【苏州中心】\",\"Details\":\"\"},{\"Date\":\"2017-08-23 12:51\",\"StatusDescription\":\"【中国邮政集团公司苏州市大宗邮件处理中心】已封发\",\"Details\":\"\"},{\"Date\":\"2017-08-23 10:43\",\"StatusDescription\":\"【中国邮政集团公司苏州市大宗邮件处理中心】已收寄\",\"Details\":\"\"},{\"Date\":\"2017-08-20 00:26\",\"StatusDescription\":\"电子信息已收到\",\"Details\":\"\"}]},\"destination_info\":{\"ItemReceived\":null,\"ItemDispatched\":null,\"DepartfromAirport\":null,\"ArrivalfromAbroad\":null,\"CustomsClearance\":null,\"DestinationArrived\":null,\"weblink\":null,\"phone\":null,\"carrier_code\":null,\"trackinfo\":null},\"lastEvent\":\"计划交航,2017-08-25 12:51\",\"lastUpdateTime\":\"2017-08-25 12:51\"}";
        TrackMain trackMain = JSON.parseObject(json, TrackMain.class);
        System.out.println(trackMain.getId());

    }

}
