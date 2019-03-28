package com.yks.bigdata.service;

import com.yks.bigdata.dao.Request_taskMapper;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.ITrackSourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * Created by zh on 2017/7/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSourceService {

    @Autowired
    ITrackSourceService sourceService;
    @Autowired
    Request_taskMapper taskMapper;

    @Test
    public void testAddBatch(){
        RequestTask task = taskMapper.selectByPrimaryKey(1);
        RequestTask task2 = taskMapper.selectByPrimaryKey(2);
        ArrayList<RequestTask> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);

        sourceService.addBatch(tasks,"000000000");
    }
}
