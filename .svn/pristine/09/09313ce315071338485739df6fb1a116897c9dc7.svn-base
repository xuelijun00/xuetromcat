package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.LogisticsStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by zh on 2017/7/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestChannelStatus {

    @Autowired
    Logistics_statusMapper statusMapper;

    @Test
    public void testAss(){
        LogisticsStatus status = new LogisticsStatus();
        status.setChannel("china-post");
        List<LogisticsStatus> logisticsStatuses = statusMapper.selectLogisticsStatus(status);
        System.out.println(logisticsStatuses);
    }
}
