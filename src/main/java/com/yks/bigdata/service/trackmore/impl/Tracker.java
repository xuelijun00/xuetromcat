package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.exception.*;
import com.yks.bigdata.exception.TrackHttpException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class Tracker {

    private static Logger log = LoggerFactory.getLogger(Tracker.class);

    private AtomicInteger count = new AtomicInteger(1);

    @Autowired
    private SystemExceptionMapper systemExceptionMapper;

    /**
     * Apikey my
     */
    //private static final String Apikey = "c7cdeb1d-7fa2-4fe2-97a6-73a7c64768a7";
    //private static final String Apikey = "c6e70625-7a26-4373-9d5d-49f119d79a65";
    public static void main(String[] args) throws Exception {
        String Apikey = "c7cdeb1d-7fa2-4fe2-97a6-73a7c64768a7";
//
//		String urlStr ="?page=1&limit=25";
//		String requestData=null;
//		String result = new Tracker().orderOnlineByJson(requestData,urlStr,"get",Apikey);
//		System.out.println(result);

		/*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00");
		System.out.println(format.format(new Date()));
		System.out.println(format.parse("2017-06-12T06:50:42+00:00").getYear() + 1900);*/

        //注册 test
        //成功为 {"meta":{"code":200,"type":"Success","message":"Success"},"data":{"id":"376c250081c4553cb197894fa49d888a","tracking_number":"8000311584581","carrier_code":"turkey-post","status":"pending","created_at":"2017-07-05T10:00:50+00:00","customer_email":null,"customer_name":null,"order_id":null,"title":null}}
        //失败为
        // 重复注册{"meta":{"code":4016,"type":"Bad Request","message":"Tracking already exists."},"data":[]}
        //批量注册 有成功也有失败 {"meta":{"code":201,"type":"Success","message":"The request was successful and a resource was created."},"data":{"submitted":3,"added":2,"trackings":[{"id":"053bbdf932caf0774ec13b954a09ebf6","tracking_number":"RH051844173TR","carrier_code":"turkey-post","status":"pending","created_at":"2017-07-07T01:59:51+00:00","customer_email":null,"customer_name":null,"order_id":null,"title":null},{"id":"13e11d7ffd53ae893c734d6e496183b2","tracking_number":"RH051844187TR","carrier_code":"turkey-post","status":"pending","created_at":"2017-07-07T01:59:52+00:00","customer_email":null,"customer_name":null,"order_id":null,"title":null}],"errors":[{"tracking_number":"8000311584582221","carrier_code":"turkey-post","code":4016,"message":"Tracking already exists."}]}}
//		String urlStr =null;
//		String requestData= "{\"tracking_number\": \"RL717285408CN\",\"carrier_code\":\"china-post\"}";
//		System.out.println(requestData);
//		String result = new Tracker().orderOnlineByJson(requestData,urlStr,"post",Apikey);
//		System.out.println(result);


        //查询
        /*String urlStr = null;
        long d1 = System.currentTimeMillis();
        //String requestData = "{\"tracking_number\": \"RL717285408CN\",\"carrier_code\":\"china-post\"}";
        String requestData = "{\"tracking_number\": \"RL717286505CN\",\"carrier_code\":\"china-post\"}";
        String result = new Tracker().orderOnlineByJson(requestData, urlStr, "realtime", Apikey);
        System.out.println(System.currentTimeMillis() -d1);
        System.out.println(result);*/

        String urlStr ="?page=1&limit=200";
        String result = new Tracker().orderOnlineByJson(null,urlStr,"get","769dc19d-e517-4bf4-b318-959751eaf7f0");
        System.out.println(result);
        JSONObject rootObject = JSON.parseObject(result);
        JSONArray jsonArray = rootObject.getJSONObject("data").getJSONArray("items");
        System.out.println(jsonArray.getJSONObject(0).getString("tracking_number"));
        jsonArray.remove(0);
        System.out.println(jsonArray.getJSONObject(0).getString("tracking_number"));

    }


    public String queryInfo(String requestData,String Apikey) throws Exception {
        //Apikey = "c7cdeb1d-7fa2-4fe2-97a6-73a7c64768a7";
        String urlStr = null;
        String result = this.orderOnlineByJson(requestData, urlStr, "realtime", Apikey);
        if (!judgeException("realtime",requestData,result)) {
            log.error("trace more 接口查询失败! 请求data为:" + requestData + " \n 返回data: " + result);
            return null;
        }else{
            return result;
        }
    }

    public String queryInfoBatch(String requestData,String Apikey) throws Exception {
        String result = this.orderOnlineByJson(null, requestData, "get", Apikey);
        if (!judgeException("get",requestData,result)) {
            log.error("trace more 接口查询失败! 请求data为:" + requestData + " \n 返回data: " + result);
            return null;
        }else{
            return result;
        }
    }


    public boolean judgeException(String type,String requestData,String response) {
        if (StringUtils.isBlank(response)){
            log.info("track more接口返回的json为null");
            return  false;
        }
        JSONObject rootObject = JSON.parseObject(response);
        Integer code = rootObject.getJSONObject("meta").getInteger("code");
        if (code == 200 || code == 201)
            return true;
        else{
            if (code == 202) {
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError202Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 401){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError401Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 402){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError402Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 403){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError403Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 404){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError404Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 405){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError405Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 409){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError409Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 429){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                log.error("Too Many Requests - 超过 API 限制。请求暂停，等待两分钟，然后再试。baseMsg :");
                try{Thread.sleep(3*1000);}catch (Exception ex){};
                throw new TrackHttpError429Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 500){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError500Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 503){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError503Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4001){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4001Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4002){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4002Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4012){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4012Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4013){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4013Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4014){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4014Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4015){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4015Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4016){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4016Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4017){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4017Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4018){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4018Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4020){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4020Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4021){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4021Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4031){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4031Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4032){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4032Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }else if(code == 4033){
                systemExceptionMapper.insert(new SystemException("trace more 接口查询失败","http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response));
                throw new TrackHttpError4033Exception("http type 为 :"+type+" \n requestData 为:"+requestData+"\n response为:"+response);
            }
        }
        return false;
    }

    /**
     * 批量注册
     *
     * @param requestData
     * @return
     * @throws Exception
     */
    public String registerBatch(String requestData, String Apikey) throws Exception {
        String urlStr = null;
        String response = this.orderOnlineByJson(requestData, urlStr, "batch", Apikey);
        judgeException("batch",requestData,response);
        return response;
    }

    public Boolean registerLogisticsProjectBatch(String requestData, String Apikey) throws Exception {
        String urlStr = null;
        String response = this.orderOnlineByJson(requestData, urlStr, "batch", Apikey);
        return judgeException("batch",requestData,response);
    }

    /**
     * Json
     */
    public String orderOnlineByJson(String requestData, String urlStr, String type, String Apikey) throws Exception {
        // ---headerParams
        Map<String, String> headerparams = new HashMap<>();
        headerparams.put("Trackingmore-Api-Key", Apikey);
        headerparams.put("Content-Type", "application/json");

        // ---bodyParams
        List<String> bodyParams = new ArrayList<>();
        String result = null;
        if (type.equals("post")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/post";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("get")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/get";
            String RelUrl = ReqURL + urlStr;
            result = sendPost(RelUrl, headerparams, bodyParams, "GET");
        } else if (type.equals("batch")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/batch";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        } else if (type.equals("codeNumberGet")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings";
            String RelUrl = ReqURL + urlStr;
            result = sendGet(RelUrl, headerparams, "GET");
        } else if (type.equals("codeNumberPut")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings";
            bodyParams.add(requestData);
            String RelUrl = ReqURL + urlStr;
            result = sendPost(RelUrl, headerparams, bodyParams, "PUT");
        } else if (type.equals("codeNumberDelete")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings";
            String RelUrl = ReqURL + urlStr;
            result = sendGet(RelUrl, headerparams, "DELETE");
        } else if (type.equals("realtime")) {
            String ReqURL = "http://api.trackingmore.com/v2/trackings/realtime";
            bodyParams.add(requestData);
            result = sendPost(ReqURL, headerparams, bodyParams, "POST");
        }
        return result;
    }

    private String sendPost(String url, Map<String, String> headerParams, List<String> bodyParams, String mothod)throws Exception {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(mothod);
            for (Entry<String, String> entry : headerParams.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            StringBuffer sbBody = new StringBuffer();
            for (String str : bodyParams) {
                sbBody.append(str);
            }
            out.write(sbBody.toString());
            out.flush();
            //result.append(IOUtils.toString(conn.getInputStream(), "UTF-8"));
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            /*int BUFFER_SIZE=10240;
            char[] buffer = new char[BUFFER_SIZE]; // or some other size,
            int charsRead;
            while ( (charsRead  = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                result.append(buffer, 0, charsRead);
            }*/
            String line;
            while (in.ready() && (line = in.readLine()) != null) {
                result.append(line);
            }
            conn.disconnect();
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String sendGet(String url, Map<String, String> headerParams, String mothod)throws Exception {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod(mothod);
            for (Entry<String, String> entry : headerParams.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw new TrackHttpException(url,e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}
