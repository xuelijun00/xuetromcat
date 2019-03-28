package com.yks.bigdata.service;

import com.yks.bigdata.dao.Request_taskMapper;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.impl.LoopSearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2017/7/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLoopService {

    @Autowired
    LoopSearchServiceImpl loopSearchService;
    @Autowired
    Request_taskMapper taskMapper;

//    @Test
//    public void testCreateRequestData(){
//        RequestTask task = taskMapper.selectByPrimaryKey(1);
//        RequestTask task3 = taskMapper.selectByPrimaryKey(2);
//        ArrayList<RequestTask> tasks = new ArrayList<>();
//        tasks.add(task);
//        tasks.add(task3);
//        String requestData = loopSearchService.createRequestData(tasks);
//        System.out.println(requestData);
//
//    }


//    @Test
//    public void testSplitByAccount(){
//        RequestTask task = taskMapper.selectByPrimaryKey(1);
//        RequestTask task3 = taskMapper.selectByPrimaryKey(2);
//        ArrayList<RequestTask> tasks = new ArrayList<>();
//        tasks.add(task);
//        tasks.add(task3);
//
//        loopSearchService.splitByAccount(tasks);
//    }

    @Test
    public void testLoop(){
        loopSearchService.loop();
    }

}
