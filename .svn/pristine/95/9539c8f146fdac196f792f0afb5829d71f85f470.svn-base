package com.yks.bigdata.dto.trackmore;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class RequestTask implements Serializable {
    private Integer id;

    private Long erpOrdersId;

    private String trackAccount;

    private String trackingNumber;

    private String carrierCode;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertAt;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fetchTime;

    /**
     * 默认1，1、需要创建
     *      2、创建后需要获取物流信息
     *      3、包裹已成功妥投,就不用在获取信息了
     *      4 注册失败!
     *      5.已经完结
     */
    private Integer taskStatus;

    /**
     * 渠道id
     */
    private Integer channelId;

    private String channel;

    public RequestTask(){}
    public RequestTask(Long erpOrdersId, String trackAccount, String trackingNumber, String carrierCode,String channel){
        this.erpOrdersId = erpOrdersId;
        this.trackAccount = trackAccount;
        this.trackingNumber = trackingNumber;
        this.carrierCode = carrierCode;
        this.taskStatus = 1;
        this.insertAt = new Date();
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Long getErpOrdersId() {
        return erpOrdersId;
    }

    public void setErpOrdersId(Long erpOrdersId) {
        this.erpOrdersId = erpOrdersId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrackAccount() {
        return trackAccount;
    }

    public void setTrackAccount(String trackAccount) {
        this.trackAccount = trackAccount == null ? null : trackAccount.trim();
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber == null ? null : trackingNumber.trim();
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode == null ? null : carrierCode.trim();
    }

    public Date getInsertAt() {
        return insertAt;
    }

    public void setInsertAt(Date insertAt) {
        this.insertAt = insertAt;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /*public Boolean isCreated(){
        if (taskStatus == 1)
            return true;
        return false;
    }*/
}