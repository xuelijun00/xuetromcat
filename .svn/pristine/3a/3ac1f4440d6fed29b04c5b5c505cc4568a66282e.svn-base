/*package com.yks.bigdata.test;

import com.yks.bigdata.util.HttpRequestUtils;
import com.yks.bigdata.util.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;*/

/**
 * Created by zh on 2017/7/9.
 */
/*
public class TestKylin {

    static String baseURL="http://192.168.5.206:7070/kylin/api";

    public static void main(String[] args) throws Exception {
        TestKylin testKylin = new TestKylin();
        testKylin.testAuthentication();
    }


    public void testAuthentication() throws Exception {


        String method = "PUT";
        String para = "/cubes/quanliangcube/rebuild";
        String body = "{    \"startTime\": 0,    \"endTime\": 1388563200000,    \"buildType\": \"BUILD\"}";
        String s = excute(para, method, body);
        System.out.println(s);


    }

    private   String excute(String para,String method,String body){

        StringBuilder out = new StringBuilder();
        try {
            URL url = new URL(baseURL+para);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            Base64.Encoder encoder = Base64.getEncoder();
            String s = new String(encoder.encode("ADMIN:KYLIN".getBytes()));
            String au="Basic "+s;
            connection.setRequestProperty  ("Authorization", au);
            connection.setRequestProperty("Content-Type","application/json");
            if(body !=null){
                byte[] outputInBytes = body.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in  = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                out.append(line);
            }
            in.close();
            connection.disconnect();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return out.toString();
    }
}

*/
