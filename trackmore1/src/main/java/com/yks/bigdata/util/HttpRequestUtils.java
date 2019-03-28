package com.yks.bigdata.util;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.mime.content.FileBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by liuxing on 2017/5/5.
 */
public class HttpRequestUtils {

    private static Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

    /**
     * 发送get请求
     * @param url
     * @param params
     * @param header
     * @return
     */
    public static ResponseData sendHttpGet(String url,String params, Header[] header) {
        ResponseData responseData = new ResponseData();
        CloseableHttpClient httpClients = HttpClients.createDefault();
        try {
            if(params != null && params.length() > 0)
                url = url + URLEncoder.encode(params,"utf-8");
            //log.info("请求URL：" + url);
            HttpGet httpGet = new HttpGet(url);
            if(header != null && header.length > 0){
                httpGet.setHeaders(header);
            }
            CloseableHttpResponse response = httpClients.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();   //获取状态码
            responseData.setStatusCode(statusCode);
            if(statusCode == 200){
                log.info("get请求成功！");
                responseData.setResponseText(getResponseText(response));
                responseData.setHeaders(response.getAllHeaders());
            }else{
                log.info("get请求失败！状态码：" + statusCode);
                responseData.setResponseText(getResponseText(response));
            }
            httpClients.close();
        }catch (Exception ex){
            log.info("get请求失败！" + ex.getLocalizedMessage());
        }
        return responseData;
    }

    public static ResponseData sendHttpGet(String url) {
        return sendHttpGet(url,null,null);
    }
    public static ResponseData sendHttpGet(String url,String params) {
        return sendHttpGet(url,params,null);
    }

    /**
     *发送post请求
     * @param url
     * @param params
     * @param headers
     */
    public static ResponseData sendHttpPost(String url,Map<String,String> params,Header[] headers){
        ResponseData responseData = new ResponseData();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //log.info("post请求URL:" + url);
        HttpPost httpPost = new HttpPost(url);
        if(headers != null && headers.length > 0){
            httpPost.setHeaders(headers);
        }
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        if(params != null && params.size() > 0){
            if(params.containsKey("file")){
                FileBody fileBody = new FileBody(new File(params.get("file")));
                multipartEntityBuilder.addPart("file",fileBody);
            }
            for (Map.Entry<String, String> str : params.entrySet()) {
                multipartEntityBuilder.addTextBody(str.getKey(),str.getValue());
            }
        }
        httpPost.setEntity(multipartEntityBuilder.build());
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            responseData.setStatusCode(statusCode);
            if(statusCode == 200){
                log.info("post请求成功！");
                responseData.setHeaders(response.getAllHeaders());
                responseData.setResponseText(getResponseText(response));
            }else{
                log.info("post请求失败！状态码：" + statusCode);
                responseData.setResponseText(getResponseText(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("post请求失败！");
        }
        return responseData;
    }
    public static ResponseData sendHttpPost(String url) {
        return sendHttpPost(url,null,null);
    }
    public static ResponseData sendHttpPost(String url,Map<String,String> params) {
        return sendHttpPost(url,params,null);
    }

    private static String getResponseText(CloseableHttpResponse response){
        try{
            InputStream inputStream = response.getEntity().getContent();
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String contentEncoding = "";
            if(response.containsHeader("Content-Encoding")){
                contentEncoding = response.getHeaders("Content-Encoding")[0].getValue();
            }
            if(contentEncoding.indexOf("gzip") > -1){
                inputStream = new GZIPInputStream(response.getEntity().getContent());
            }
            int i;
            while((i = inputStream.read(bytes)) != -1){
                byteArrayOutputStream.write(bytes,0,i);
            }
            return new String(byteArrayOutputStream.toByteArray(),Charset.forName("utf-8"));
        }catch(Exception ex){
            log.info("获取responseText出错：" + ex.getLocalizedMessage());
        }
        return null;
    }

}
