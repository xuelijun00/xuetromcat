package com.yks.bigdata.util;

import com.yks.bigdata.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by zh on 2017/6/21.
 * es工具类
 */
public class EsUtils {

    private static String baseurl= Constants.baseurl;

    public static String put(String url,String jsonStr)throws Exception{
        String uriAPI = baseurl+url;
        HttpPut httpPut = new HttpPut(uriAPI);
        return execute(httpPut,url,jsonStr);
    }

    public static String post(String url,String jsonStr)throws Exception{
        String uriAPI = baseurl+url;
        HttpPost httpPut = new HttpPost(uriAPI);
        return execute(httpPut,url,jsonStr);
    }


    public static String get(String url, String jsonStr)throws Exception{
        String uriAPI = baseurl+url;
        HttpPost httpRequst = new HttpPost(uriAPI);
        return execute(httpRequst,url,jsonStr);
    }


    /**
     * 手动刷新
     */
    public static void handleReflashIndex(String index)throws Exception{
        String url=index+"/_flush";
        String json="{}";
        EsUtils.get(url,json);
    }


    public static String execute(HttpEntityEnclosingRequestBase httpRequst, String url, String jsonStr)throws Exception{
        if (StringUtils.isEmpty(jsonStr)){
            jsonStr = "{}";
        }
        StringEntity entity = new StringEntity(jsonStr,"utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpRequst.setEntity(entity);
        String result= "";
        try {
            //使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);//其中HttpGet是HttpUriRequst的子类
//            if(httpResponse.getStatusLine().getStatusCode() == 200)
//            {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);//取出应答字符串
                // 一般来说都要删除多余的字符
                result.replaceAll("\r", "");//去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
//            }
//            else
//                httpRequst.abort();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = e.getMessage().toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = e.getMessage().toString();
        }
        if (result.contains("error")){
            System.out.println("es返回结果异常 "+result);
        }
        return result;
    }
}
