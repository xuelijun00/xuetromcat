package com.yks.bigdata.web.vo;

import com.yks.bigdata.dto.trackmore.LogisticsChannel;

/**
 * Created by Administrator on 2017/7/4.
 */
public class ChannelExcelTemp {

    private String a_yksChannelCode;
    private String b_yksChannelName;
    private String c_trackmoreCode;

    public LogisticsChannel toDto(){
        LogisticsChannel channel = new LogisticsChannel();
        channel.setYksChannelCode(this.a_yksChannelCode);
        channel.setYksChannelName(this.b_yksChannelName);
        channel.setShortCode(this.c_trackmoreCode);
        return channel;
    }

    public String getA_yksChannelCode() {
        return a_yksChannelCode;
    }

    public void setA_yksChannelCode(String a_yksChannelCode) {
        this.a_yksChannelCode = a_yksChannelCode;
    }

    public String getB_yksChannelName() {
        return b_yksChannelName;
    }

    public void setB_yksChannelName(String b_yksChannelName) {
        this.b_yksChannelName = b_yksChannelName;
    }

    public String getC_trackmoreCode() {
        return c_trackmoreCode;
    }

    public void setC_trackmoreCode(String c_trackmoreCode) {
        this.c_trackmoreCode = c_trackmoreCode;
    }
}
