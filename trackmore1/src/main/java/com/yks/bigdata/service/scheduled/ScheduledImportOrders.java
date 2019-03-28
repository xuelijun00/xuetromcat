package com.yks.bigdata.service.scheduled;

import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.LoopSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zh on 2017/6/30.
 */
@Component
public class ScheduledImportOrders {
    private static Logger log = LoggerFactory.getLogger(ScheduledImportOrders.class);

    @Autowired
    IErpOrdersService ordersService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void importErpOrders(){
        log.info("定时任务：导入erp_orders任务begin");
        try {
            ordersService.importErpOrders();
        } catch (Exception e) {
           log.error("定时导入任务失败!" ,e);
        }
        log.info("定时任务：导入erp_orders任务end");
    }

    @Autowired
    LoopSearchService loopSearchService;

    @Scheduled(cron = "0 0 0,6,12,18 * * ?")
    public void trackmore() throws Exception {
        log.info("抓取任务：begin");
        try {
            loopSearchService.loop();
        } catch (Exception e) {
            log.error("trackmore数据抓取任务失败!" ,e);
        }
        log.info("抓取任务：end");
    }

}

