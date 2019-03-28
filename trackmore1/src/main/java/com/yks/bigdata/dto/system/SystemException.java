package com.yks.bigdata.dto.system;

import java.util.Date;

public class SystemException {
    private Integer id;

    private String type;

    private Date createTime;

    private String errContext;

    public SystemException(){}
    public SystemException(String type,String errContext){
        this.type = type;
        this.errContext = errContext;
        this.createTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getErrContext() {
        return errContext;
    }

    public void setErrContext(String errContext) {
        this.errContext = errContext == null ? null : errContext.trim();
    }
}