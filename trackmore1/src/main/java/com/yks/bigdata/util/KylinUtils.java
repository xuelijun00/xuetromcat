/*
package com.yks.bigdata.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

*/
/**
 * Created by zh on 2017/7/9.
 *//*

public class KylinUtils {

    static String baseURL="http://192.168.5.206:7070/kylin/api";

    */
/**
     * 重建cube
     * @param cubeName
     *//*

    public static void rebuild(String cubeName){
        String method = "PUT";
        String para = "/cubes/"+cubeName+"/rebuild";
        String body = "{    \"startTime\": 0,    \"endTime\": 1388563200000,    \"buildType\": \"BUILD\"}";
        String s = excute(para, method, body);
        System.out.println(s);
    }

    private static String excute(String para,String method,String body){

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

package com.yks.bigdata.util;

import com.yks.bigdata.dto.trackmore.LogisticReport;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Driver;
import java.util.List;
import java.util.Properties;

@Component
@PropertySource(value = "classpath:config/kylin.properties")
@ConfigurationProperties(prefix = "kylin")
public class KylinUtils {

    @Value(value = "${kylin.driver-class-name}")
    private String driver;
    private String username;
    private String password;
    private String url;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private static Connection connection = null;
    private static Logger LOG = LoggerFactory.getLogger(KylinUtils.class);

    public Connection getConnection(){
            try{
                if(connection == null || connection.isClosed()){
                    Driver driver = (Driver) Class.forName(getDriver()).newInstance();
                    Properties info = new Properties();
                    info.put("user",username);
                    info.put("password",password);
                    connection = driver.connect(url, info);
                    LOG.error("获取kylin成功！");
                }
            }catch (Exception ex){
                LOG.error("获取kylin链接出错：" + ex.getLocalizedMessage());
            }
        return connection;
    }
    public void destory(Connection connection){
        try{
            if(connection != null){
                connection.close();
            }
        }catch (Exception ex){
            LOG.error("关闭kylin链接出错：" + ex.getLocalizedMessage());
        }
    }

}