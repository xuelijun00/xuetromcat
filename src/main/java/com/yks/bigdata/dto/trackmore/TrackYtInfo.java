package com.yks.bigdata.dto.trackmore;

import java.util.Date;

public class TrackYtInfo {
    private Integer id;

    private String waybillno;

    private String referenceorderno;

    private String referencechangeno;

    private String eventcode;

    private String eventdetail;

    private String eventoperater;

    private String eventoperatercode;

    private String eventlocationcode;

    private String eventlocation;

    private Date eventtime;

    private String servicecode;

    private String originaltracking;

    private String visiblesign;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWaybillno() {
        return waybillno;
    }

    public void setWaybillno(String waybillno) {
        this.waybillno = waybillno == null ? null : waybillno.trim();
    }

    public String getReferenceorderno() {
        return referenceorderno;
    }

    public void setReferenceorderno(String referenceorderno) {
        this.referenceorderno = referenceorderno == null ? null : referenceorderno.trim();
    }

    public String getReferencechangeno() {
        return referencechangeno;
    }

    public void setReferencechangeno(String referencechangeno) {
        this.referencechangeno = referencechangeno == null ? null : referencechangeno.trim();
    }

    public String getEventcode() {
        return eventcode;
    }

    public void setEventcode(String eventcode) {
        this.eventcode = eventcode == null ? null : eventcode.trim();
    }

    public String getEventdetail() {
        return eventdetail;
    }

    public void setEventdetail(String eventdetail) {
        this.eventdetail = eventdetail == null ? null : eventdetail.trim();
    }

    public String getEventoperater() {
        return eventoperater;
    }

    public void setEventoperater(String eventoperater) {
        this.eventoperater = eventoperater == null ? null : eventoperater.trim();
    }

    public String getEventoperatercode() {
        return eventoperatercode;
    }

    public void setEventoperatercode(String eventoperatercode) {
        this.eventoperatercode = eventoperatercode == null ? null : eventoperatercode.trim();
    }

    public String getEventlocationcode() {
        return eventlocationcode;
    }

    public void setEventlocationcode(String eventlocationcode) {
        this.eventlocationcode = eventlocationcode == null ? null : eventlocationcode.trim();
    }

    public String getEventlocation() {
        return eventlocation;
    }

    public void setEventlocation(String eventlocation) {
        this.eventlocation = eventlocation == null ? null : eventlocation.trim();
    }

    public Date getEventtime() {
        return eventtime;
    }

    public void setEventtime(Date eventtime) {
        this.eventtime = eventtime;
    }

    public String getServicecode() {
        return servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode == null ? null : servicecode.trim();
    }

    public String getOriginaltracking() {
        return originaltracking;
    }

    public void setOriginaltracking(String originaltracking) {
        this.originaltracking = originaltracking == null ? null : originaltracking.trim();
    }

    public String getVisiblesign() {
        return visiblesign;
    }

    public void setVisiblesign(String visiblesign) {
        this.visiblesign = visiblesign == null ? null : visiblesign.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}