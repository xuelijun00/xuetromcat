package com.yks.bigdata.service.es.base;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zh on 2017/6/28.
 */
public class EsQueryParamterMap {

    //请求url
    String url;


    //结果显示哪些字段
    ArrayList<String> showFileds;

    //过滤条件 相当于 where key = value
    Map<String,String> filterFileds;

    Integer size = 10;
    Integer from = 0;

    public EsQueryParamterMap(String url, ArrayList<String> showFileds, Map<String, String> filterFileds, Integer size, Integer from) {
        this.url = url;
        this.showFileds = showFileds;
        this.filterFileds = filterFileds;
        this.size = size;
        this.from = from;
    }

    public EsQueryParamterMap(String url, ArrayList<String> showFileds, Map<String, String> filterFileds) {
        this.url = url;
        this.showFileds = showFileds;
        this.filterFileds = filterFileds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getShowFileds() {
        return showFileds;
    }

    public void setShowFileds(ArrayList<String> showFileds) {
        this.showFileds = showFileds;
    }

    public Map<String, String> getFilterFileds() {
        return filterFileds;
    }

    public void setFilterFileds(Map<String, String> filterFileds) {
        this.filterFileds = filterFileds;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }
}
