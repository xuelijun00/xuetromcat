package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.LogisticsChannel;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zh on 2017/6/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class Testlogistics_channel {

    @Autowired
    LogisticsChannelMapper channelMapper;

    @Test
    public void insert(){
        LogisticsChannel channel = new LogisticsChannel();

        channel.setShortCode("turkey-post");
        channel.setEnglishName("Turkey Post");
        channel.setChinessName("土耳其物流");
        channel.setYksChannelCode("Y266S");

        channelMapper.insert(channel);
    }
}
