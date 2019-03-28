package com.yks.bigdata.service.channel;

import com.yks.bigdata.service.channel.ChannelLWEService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liuxing on 2017/9/15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class ChannelLWEServiceTest {

    @Autowired
    ChannelLWEService channelLWEService;

    @Test
    public void queryLWEApi() throws Exception {
        channelLWEService.queryLWEApi();
    }

    @Test
    public void sendRequestQueryIBApi() throws Exception {
        String url = "http://api.unixus.com.my/Tracking/V2/Tracking.svc/json/Details/?ids=156130512872784,156130512597113,156130512698381,0173941746031745,156130512607016,156130512779275,156130512686691,156130512734114,156130512734696,156130512709990";
        channelLWEService.sendRequestQueryIBApi(url);
    }

}