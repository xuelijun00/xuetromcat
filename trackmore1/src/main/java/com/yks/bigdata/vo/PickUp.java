package com.yks.bigdata.vo;

/**
 * Created by zh on 2017/6/28.
 * 取件 vo
 */
public class PickUp {

    Long erp_orders_id;
    String freightcode; // y201
    String path; //泉州

    public PickUp(Long erp_orders_id, String freightcode, String path) {
        this.erp_orders_id = erp_orders_id;
        this.freightcode = freightcode;
        this.path = path;
    }

    public Long getErp_orders_id() {
        return erp_orders_id;
    }

    public void setErp_orders_id(Long erp_orders_id) {
        this.erp_orders_id = erp_orders_id;
    }

    public String getFreightcode() {
        return freightcode;
    }

    public void setFreightcode(String freightcode) {
        this.freightcode = freightcode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
