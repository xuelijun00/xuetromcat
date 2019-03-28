package com.yks.bigdata.service.trackmore.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by liuxing on 2017/10/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class ErpOrdersProcessServiceTest {

    @Autowired
    ErpOrdersProcessService erpOrdersProcessService;

    @Test
    public void processData() throws Exception {
        erpOrdersProcessService.processData();
    }
    @Test
    public void processData1() throws Exception {
        erpOrdersProcessService.processData("'云途中西专线挂号'","2017-10-12","2017-10-27");
    }

    @Test
    public void getRedisAllData() throws Exception {
        erpOrdersProcessService.getRedisAllData();
    }

}