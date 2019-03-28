package com.yks.bigdata.util;

import com.yks.bigdata.common.Constants;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zh on 2017/6/22.
 */
@Component
public class JdbcUtils {


    public static Connection getKylinConnection(){
        try{
            //加载MySql的驱动类
            Class.forName("org.apache.kylin.jdbc.Driver") ;
        }catch(ClassNotFoundException e){
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace() ;
        }
        Connection con=null;
        try {
            con = DriverManager.getConnection("jdbc:kylin://192.168.5.206:7070/test001", "ADMIN" , "KYLIN" ) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static Connection getConnection(){
        try{
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver") ;
        }catch(ClassNotFoundException e){
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace() ;
        }
        Connection con=null;
        try {
            con = DriverManager.getConnection(Constants.url , Constants.username , Constants.password ) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void destoryConnection(Connection con){
        try{
            con.close();
        }catch(Exception ex){}

    }

    public static Connection getConnection2(){
        try{
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver") ;
        }catch(ClassNotFoundException e){
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace() ;
        }
        Connection con=null;

        String url="jdbc:mysql://dev.trackmoredbm.kokoerp.com:3306/trackmore";
        String username="trackmore";
        String password="341pZJ6NLbuXNmTp";
        try {
            con = DriverManager.getConnection(url , username ,password ) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

}
