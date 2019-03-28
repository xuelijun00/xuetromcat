package com.yks.bigdata.vo;

/**
 * Created by zh on 2017/6/26.
 */
public class Trackmore_account {

    String id;
    String account;
    String api_key;
    Long create_time;
    Integer data_count;
    Integer status;

    public Trackmore_account(String id, String account, String api_key, Long create_time, Integer data_count, Integer status) {
        this.id = id;
        this.account = account;
        this.api_key = api_key;
        this.create_time = create_time;
        this.data_count = data_count;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Integer getData_count() {
        return data_count;
    }

    public void setData_count(Integer data_count) {
        this.data_count = data_count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
