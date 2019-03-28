package com.yks.bigdata.dto.trackmore;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class LogisticsChannel implements Serializable {
    private Integer id;

    private String shortCode;

    private String englishName;

    private String chinessName;

    private String yksChannelCode;

    private String yksChannelName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 0、禁用
     * 1、启用
     */
    private Integer status;

    private String startTime;
    private String endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode == null ? null : shortCode.trim();
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName == null ? null : englishName.trim();
    }

    public String getChinessName() {
        return chinessName;
    }

    public void setChinessName(String chinessName) {
        this.chinessName = chinessName == null ? null : chinessName.trim();
    }

    public String getYksChannelCode() {
        return yksChannelCode;
    }

    public void setYksChannelCode(String yksChannelCode) {
        this.yksChannelCode = yksChannelCode == null ? null : yksChannelCode.trim();
    }

    public String getYksChannelName() {
        return yksChannelName;
    }

    public void setYksChannelName(String yksChannelName) {
        this.yksChannelName = yksChannelName == null ? null : yksChannelName.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}