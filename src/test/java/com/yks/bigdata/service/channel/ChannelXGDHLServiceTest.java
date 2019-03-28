package com.yks.bigdata.service.channel;

import com.yks.bigdata.service.channel.ChannelXGDHLService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liuxing on 2017/9/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelXGDHLServiceTest {

    @Autowired
    ChannelXGDHLService channelXGDHLService;

    @Test
    public void queryDHL() throws Exception {
        channelXGDHLService.queryDHL();
    }

    @Test
    public void sendRequestQueryDHLApi() throws Exception {
    }

}