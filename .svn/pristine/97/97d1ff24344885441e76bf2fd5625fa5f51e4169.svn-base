package com.yks.bigdata.service;

import com.yks.bigdata.dao.Logistics_pickupMapper;
import com.yks.bigdata.dao.Request_taskMapper;
import com.yks.bigdata.dto.trackmore.LogisticsPickup;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.ILogisticsPickupService;
import com.yks.bigdata.web.vo.MessageVo;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2017/6/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class TestLogistics_pickup {
    @Autowired
    ILogisticsPickupService service;

    @Autowired
    IErpOrdersService ordersService;

    @Autowired
    Logistics_pickupMapper mapper;
    @Autowired
    Request_taskMapper taskMapper;


    @Test
    public void testInsert(){
        LogisticsPickup pickup = new LogisticsPickup();
        pickup.setOrderId("11111");
        mapper.insert(pickup);
    }
    @Test
    public void testSelect(){
        LogisticsPickup pickup = new LogisticsPickup();
        //pickup.setOrderId("1111632937081");
        pickup.setParamEnd("2015-03-01");
        List<LogisticsPickup> logistics_pickups = service.selectLogisticsPickup(pickup);
        System.out.println(logistics_pickups.size());
    }

    @Test
    public void testPickUpInsert()throws Exception{
        String str = IOUtils.toString(new FileInputStream("data/csv/execel.csv"));
        String[] ids = str.split("\r\n");
        ArrayList<LogisticsPickup> pickUps = new ArrayList<>();
        for (String id:ids){
            LogisticsPickup pickup = new LogisticsPickup();
            pickup.setOrderId(id.split(",")[0]);
            pickup.setChannelName("泉州");
            pickup.setChannelCode("Y266S");
            pickup.setStatus(0);
            pickUps.add(pickup);
            //pickUps.add(new LogisticsPickup(Long.parseLong(id), "Y266S", "泉州"));
        }
        //MessageVo messageVo = service.insertBatch(pickUps);
        //System.out.println(messageVo != null ? messageVo.getMessage():"");
    }

    @Test
    public void testUpdateStatusRegistered(){
        RequestTask task = taskMapper.selectByPrimaryKey(1);
        ArrayList<RequestTask> tasks = new ArrayList<>();
        tasks.add(task);
        service.updateStatusRegistered(tasks);
    }


}
