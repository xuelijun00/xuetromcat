package com.yks.bigdata.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liuxing on 2017/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KylinUtilsTest {

    @Autowired
    KylinUtils kylinUtils;

    @Test
    public void getConnection() throws Exception {
        /*kylinUtils.getConnection();*/
    }

}