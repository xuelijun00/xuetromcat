package com.yks.bigdata.util;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public static ResponseData sendHttpGet(String url,String params, Map<String,String> header) {
        ResponseData responseData = new ResponseData();
        CloseableHttpClient httpClients = HttpClients.createDefault();
        try {
            if(params != null && params.length() > 0)
                url = url + URLEncoder.encode(params,"utf-8");
            //log.info("请求URL：" + url);
            HttpGet httpGet = new HttpGet(url);

            if(!CollectionUtils.isEmpty(header)){
                for (Map.Entry<String, String> str : header.entrySet()){
                    httpGet.setHeader(str.getKey(),str.getValue());
                }
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
    public static ResponseData sendHttpGet(String url,Map<String,String> header) {
        return sendHttpGet(url,null,header);
    }

    /**
     *发送post请求
     * @param url
     * @param params
     * @param headers
     */
    public static ResponseData sendHttpPostByMultipartEntity(String url,Map<String,String> params,Map<String,String> headers){
        HttpPost httpPost = new HttpPost(url);
        if(!CollectionUtils.isEmpty(headers)){
            for (Map.Entry<String, String> str : headers.entrySet()){
                httpPost.setHeader(str.getKey(),str.getValue());
            }
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
        return execPost(httpPost);
    }

    /**
     *
     * @param url
     * @return
     */
    public static ResponseData sendHttpPost(String url,Map<String,String> params,Map<String,String> headers){
        HttpPost httpPost = new HttpPost(url);
        if(!CollectionUtils.isEmpty(headers)){
            for (Map.Entry<String, String> str : headers.entrySet()){
                httpPost.setHeader(str.getKey(),str.getValue());
            }
        }
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        if(params != null && params.size() > 0){
            for (Map.Entry<String, String> str : params.entrySet()) {
                paramsList.add(new BasicNameValuePair(str.getKey(),str.getValue()));
            }
        }
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
        }catch (Exception ex){}
        return execPost(httpPost);
    }

    private static ResponseData execPost(HttpPost httpPost){
        ResponseData responseData = new ResponseData();
        CloseableHttpClient httpClient = HttpClients.createDefault();
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

    /**
     *发送post请求
     * @param url
     * @param params
     * @param headers
     */
    public static ResponseData sendHttpPostByStringEntity(String url,String params,Map<String,String> headers){
        //log.info("post请求URL:" + url);
        HttpPost httpPost = new HttpPost(url);
        if(!CollectionUtils.isEmpty(headers)){
            for (Map.Entry<String, String> str : headers.entrySet()){
                httpPost.setHeader(str.getKey(),str.getValue());
            }
        }
        httpPost.setEntity(new StringEntity(params,"utf-8"));
        return execPost(httpPost);
    }

    /*public static ResponseData sendHttpPostByMultipartEntity(String url,Map<String,String> params,Map<String,String> headers){
        ResponseData responseData = new ResponseData();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //log.info("post请求URL:" + url);
        HttpPost httpPost = new HttpPost(url);
        if(!CollectionUtils.isEmpty(headers)){
            for (Map.Entry<String, String> str : headers.entrySet()){
                httpPost.setHeader(str.getKey(),str.getValue());
            }
        }
        httpPost.setEntity();
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
    }*/

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
