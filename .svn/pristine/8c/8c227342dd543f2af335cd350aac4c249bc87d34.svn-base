package com.yks.bigdata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dto.trackmore.TrackInfo;
import com.yks.bigdata.dto.trackmore.TrackInfoDetail;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxing on 2017/7/17.
 */
public class ParseTrackmoreJson {

    public static TrackInfo parseTrackInfo(String response)throws Exception{
        TrackInfo info = new TrackInfo();
        JSONObject rootObject = JSON.parseObject(response);
        JSONArray itemsArray = rootObject.getJSONObject("data").getJSONArray("items");
        JSONObject item = itemsArray.getJSONObject(0);
        info.setTrackingNumber(item.getString("tracking_number"));
        info.setCarrierCode(item.getString("carrier_code"));
        info.setStatus(item.getString("status"));
        //todo 1.丢失 createdAt 原因 返回的数据中没有此字段 2.丢失updateAt字段
        //info.setCreatedAt(item.getString(""));
        //info.setUpdatedAt();
        info.setLastevent(item.getString("lastEvent"));
        if (StringUtils.isNotEmpty(item.getString("lastUpdateTime")))
            info.setLastupdatetime(DateUtils.formmatStr(item.getString("lastUpdateTime"),"yyyy-M-dd HH:mm"));
        info.setOriginalCountry(item.getString("original_country"));
        info.setCreateAt(DateUtils.getCurrentDateStr());
        return info;
    }

    public static List<TrackInfoDetail> parseTrackInfoDetails(Integer infoId, String response) throws Exception {
        JSONArray itemsArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("items");
        JSONObject item = itemsArray.getJSONObject(0);
        //response json 有两部分 需要生成对用的TrackInfoDetail 分别为origin_info 和 destination_info
        JSONObject origin_info = item.getJSONObject("origin_info");
        List<TrackInfoDetail> detailList = parseDetailItem(infoId, 1, origin_info);
        JSONObject destination_info = item.getJSONObject("destination_info");
        List<TrackInfoDetail> detailList2 = parseDetailItem(infoId, 2, destination_info);
        detailList.addAll(detailList2);
        return detailList;
    }

    private static List<TrackInfoDetail> parseDetailItem(Integer infoId,Integer type,JSONObject jsonObject)throws Exception{
        ArrayList<TrackInfoDetail> detailList = new ArrayList<>();
        if (jsonObject != null){
            JSONArray trackinfo = jsonObject.getJSONArray("trackinfo");
            if (trackinfo == null){
                return detailList;
            }
            for (int i=0;i<trackinfo.size();i++){
                JSONObject info = trackinfo.getJSONObject(i);
                TrackInfoDetail detail = new TrackInfoDetail();
                detail.setTrackInfoId(infoId);
                detail.setTrackType(type);
                detail.setStatusDescription(info.getString("StatusDescription"));
                detail.setDetails(info.getString("Details"));
                detail.setTrackDate(DateUtils.formmatStr(info.getString("Date"),"yyyy-M-dd HH:mm"));
                detailList.add(detail);
            }
        }
        return detailList;
    }

}
