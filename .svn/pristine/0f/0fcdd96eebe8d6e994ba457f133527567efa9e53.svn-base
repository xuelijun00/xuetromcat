package com.yks.bigdata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by zh on 2017/6/27.
 */
public class EsJsonParseUtils {

    /**
     * 解析 es查询中返回的 json
     * @param response json串
     * @param errorMsg 如果返回 hits为0 则代表出现问题,输出日志
     * @return
     */
    public static JSONArray parseHits(String response, String errorMsg){
        JSONObject rootObject = JSON.parseObject(response);
        Integer total = (Integer) rootObject.getJSONObject("hits").get("total");
        if (total<=0){
            System.out.printf(errorMsg);
            return null;
        }
       return rootObject.getJSONObject("hits").getJSONArray("hits");
    }

}
