package com.yks.bigdata.service.channel;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by liuxing on 2017/10/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class ChannelSaichengServiceTest {

    @Autowired
    ChannelSaichengService channelSaichengService;
    @Test
    public void querySaicheng() throws Exception {
        channelSaichengService.querySaicheng();
    }

    @Test
    public void test() throws Exception {
        String url = "http://114.80.227.2:8080/api/track/TrackEvents?tracktype=2&userId=YKS&findNumber=99700160UDX760461801022003028,99700160UEW718337601022906056,99700160UDW760768401022702156,99700160UDW761002101022402250,99700160UEW718313201022706155,99700160UDW760363701022102338,99700160UEW718322701022406063,99700160UEW718323001022806028,99700160UDW761040001022702480,99700160UDX761238801022703136";
        channelSaichengService.sendRequestQuerySaichengApi(url);
    }

}