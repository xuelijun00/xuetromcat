package com.yks.bigdata.service.scheduled;

import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import com.yks.bigdata.service.trackmore.impl.ErpOrdersProcessService;
import com.yks.bigdata.service.trackmore.impl.ErpOrdersServiceImpl;
import com.yks.bigdata.service.trackmore.impl.LoopSearchServiceNewImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by liuxing on 2017/10/20.
 */
@Component
public class AsyncTask {

    private static Logger log = LoggerFactory.getLogger(ErpOrdersServiceImpl.class);

    @Autowired
    IRequestTaskService requestTaskService;
    @Autowired
    LoopSearchServiceNewImpl loopSearchServiceNew;
    @Autowired
    ErpOrdersProcessService erpOrdersProcessService;

    @Async
    public void doRedisTask(String channelName,String startTime,String endTime) {
        long start = System.currentTimeMillis();
        erpOrdersProcessService.processData(channelName, startTime, endTime);
        long end = System.currentTimeMillis();
        //return new AsyncResult<>("耗时：" +  (end - start) + "毫秒");
    }


    @Async
    public Future<String> doTask(List<Long> erpOrdersIds, String key){
        long start = System.currentTimeMillis();
        List<String> list = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<erpOrdersIds.size();i++) {
            if(i%40 == 0 && stringBuffer.length() > 0){
                list.add(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
                stringBuffer = new StringBuffer();
            }
            stringBuffer.append(erpOrdersIds.get(i)).append(",");
        }
        if(stringBuffer.length() > 0)
            list.add(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());

        for(int i=0;i<list.size();i++){
            try{
                List<RequestTask> tasks = requestTaskService.selectByErpOrderIdBatch(list.get(i));
                loopSearchServiceNew.queryTrackmore40(tasks, key);
            }catch (Exception ex){
                ex.printStackTrace();
                log.error(ex.getLocalizedMessage());
            }
        }
        long end = System.currentTimeMillis();
        return new AsyncResult<>("请求订单数：" + erpOrdersIds.size() + " 耗时：" +  (end - start) + "毫秒");
    }

}
