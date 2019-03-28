package com.yks.bigdata.service.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/6/24.
 */

//@Component
public class ScheduledTest {

    private static Logger log = LoggerFactory.getLogger(ScheduledTest.class);
    private static int i = 0;

    @Scheduled(cron = "0/1 * * * * ? ")
    public void test(){
        log.info("定时任务：" + i++);
    }

    @Scheduled(cron = "0/1 * * * * ? ")
    public void test1(){
        log.info("定时任务1：" + i++);
    }


}
