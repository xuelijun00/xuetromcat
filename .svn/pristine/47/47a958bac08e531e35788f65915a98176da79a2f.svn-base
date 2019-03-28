package com.yks.bigdata.service;

import com.yks.bigdata.dao.Request_taskMapper;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRequest_taskService {
    @Autowired
    IRequestTaskService taskService;

    @Autowired
    Request_taskMapper taskMapper;

    @Test
    public void testAggration() {
        taskService.aggregation();
    }

    @Test
    public void testGetAllNeddRegisterRequestTask() {
        List<RequestTask> tasks = taskService.getAllNeddRegisterRequestTask();
        Assert.assertNotNull(tasks);
        Assert.assertTrue(tasks.get(0).getTaskStatus() == 1);
    }

    @Test
    public void testGetAllNeedQueryRequestTask() {
        List<RequestTask> tasks = taskService.getAllNeedQueryRequestTask();
        Assert.assertNotNull(tasks);
        Assert.assertTrue(tasks.size() == 2);
        Assert.assertTrue(tasks.get(0).getTaskStatus() == 2);
    }

    @Test
    public void testUpdateStatusRegister() {
        RequestTask task = taskMapper.selectByPrimaryKey(1);
        ArrayList<RequestTask> tasks = new ArrayList<>();
        tasks.add(task);
        taskService.updateStatusRegister(tasks);

        RequestTask task2 = taskMapper.selectByPrimaryKey(1);
        Assert.assertTrue(task2.getTaskStatus() == 2);
    }

    @Test
    public void tsetIpdateStatusRegisterFailed(){
        RequestTask task = taskMapper.selectByPrimaryKey(1);
        RequestTask task3 = taskMapper.selectByPrimaryKey(2);
        ArrayList<RequestTask> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task3);
        taskService.updateStatusRegisterFailed(tasks);

        RequestTask task2 = taskMapper.selectByPrimaryKey(1);
        Assert.assertTrue(task2.getTaskStatus() == 4);
    }

    @Test
    public void tsetUpdateStatusFinish(){
        RequestTask task = taskMapper.selectByPrimaryKey(1);

        taskService.updateStatusFinish(task);

        RequestTask task2 = taskMapper.selectByPrimaryKey(1);
        Assert.assertTrue(task2.getTaskStatus() == 5);
    }
}
