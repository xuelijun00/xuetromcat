package com.yks.bigdata.service.scheduled;

import com.yks.bigdata.service.trackmore.LoopSearchService;
import com.yks.bigdata.service.trackmore.impl.LoopSearchServiceNewImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liuxing on 2017/8/16.
 */
@Component
@Order(value = 10)
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    LoopSearchService loopSearchService;

    private static Logger log = LoggerFactory.getLogger(LoopSearchServiceNewImpl.class);

    @Override
    public void run(String... strings) throws Exception {
        try{
            while (true){
                loopSearchService.loop();
                Thread.sleep(1000 * 60);
                log.info("数据抓取：" + DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"));
            }
        }catch (Exception ex){}

    }
}
