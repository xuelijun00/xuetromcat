package com.yks.bigdata.test;

import com.yks.bigdata.util.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zh on 2017/5/16.
 */
public class TestJdbc {

    public static void main(String[] args)throws Exception {

        /*QueryRunner query = new QueryRunner();
        List<Map<String, Object>> query1 = query.query(JdbcUtils.getKylinConnection(), "select count(1) from order2", new MapListHandler());
        System.out.println(query1);*/

        /*Jedis jedis = new Jedis("127.0.0.1");
        jedis.select(0);
        jedis.set("redis test", "2017-08-02");
        jedis.close();*/

        String a = "a/b";
        System.out.println(a.split("/")[1]);
    }
}
