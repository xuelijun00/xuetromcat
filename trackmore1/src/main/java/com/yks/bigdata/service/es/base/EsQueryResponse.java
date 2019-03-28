package com.yks.bigdata.service.es.base;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by zh on 2017/6/28.
 */
public class EsQueryResponse {

    //查询到多少个
    Integer hit;
    //hits jsonarray对象
    JSONArray hitsJsonArray;

}
