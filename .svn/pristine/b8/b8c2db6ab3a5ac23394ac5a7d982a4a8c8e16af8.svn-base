package com.yks.bigdata.config;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;

import java.sql.Connection;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/6/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class DataSourceConfigurationTest {

    @Resource(name = "masterDataSource")
    private DataSource dataSource;

    @Test
    public void testDataSource()throws Exception{
        Connection connection = dataSource.getConnection();
        Assert.assertTrue(!connection.isClosed());
    }

    @Resource(name = "erpDataSource")
    private DataSource erpDataSource;

    @Test
    public void testErpDataSource()throws Exception{
        Connection connection = erpDataSource.getConnection();
        Assert.assertTrue(!connection.isClosed());
    }

}