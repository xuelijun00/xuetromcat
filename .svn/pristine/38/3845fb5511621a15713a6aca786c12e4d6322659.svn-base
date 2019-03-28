package com.yks.bigdata.service.scheduled;

import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.LoopSearchService;
import com.yks.bigdata.service.trackmore.impl.ErpOrdersProcessService;
import com.yks.bigdata.service.trackmore.impl.TrackmoreRegisterServiceImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zh on 2017/6/30.
 */
@Component
public class ScheduledImportOrders {
    private static Logger log = LoggerFactory.getLogger(ScheduledImportOrders.class);

    /*@Autowired
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
    }*/

    /*@Autowired
    TrackmoreRegisterServiceImpl registerTrackmoreService;

    @Scheduled(cron = "0 0 0,6,12,18 * * ?")
    public void trackmoreRegister(){
        log.info("trackmore注册任务：begin");
        try {
            registerTrackmoreService.registerTrackmore();
        } catch (Exception e) {
            log.error("trackmore注册任务失败!" ,e);
        }
        log.info("trackmore注册任务：end");
    }*/

    /*@Autowired
    ErpOrdersProcessService erpOrdersProcessService;

    @Scheduled(cron = "0 0 1,13 * * ?")
    public void trackmoreRedis() throws Exception {
        log.info("redis计算任务：begin");
        erpOrdersProcessService.processData();
        log.info("redis计算任务：end");
    }*/

}

