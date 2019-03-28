package com.yks.bigdata.service;

import com.yks.bigdata.dao.Request_taskMapper;
import com.yks.bigdata.dao.Track_infoMapper;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackInfo;
import com.yks.bigdata.service.trackmore.ITrackInfoService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by zh on 2017/7/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTrackInfo {

    @Autowired
    Track_infoMapper infoMapper;

    @Autowired
    ITrackInfoService infoService;

    @Autowired
    Request_taskMapper taskMapper;

    @Test
    public void testInsert(){
        TrackInfo info = new TrackInfo();
        info.setOriginalCountry("1111");
        info.setTaskId(111);
        infoMapper.insert(info);
        System.out.println(info.getId());
    }

    @Test
    public void testSaveOrUpdateInfoAndDetail()throws Exception{
        RequestTask task = taskMapper.selectByPrimaryKey(1);
        String response = IOUtils.toString(new FileInputStream(new File("data/b.json")));
        infoService.saveOrUpdateInfoAndDetail(task,response);
    }
}
