package com.yks.bigdata.service.trackmore;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackInfo;
import com.yks.bigdata.dto.trackmore.TrackInfoDetail;

/**
 * Created by zh on 2017/7/6.
 */
public interface ITrackInfoService {

    /**
     * 将结果写入或者更新 track_info 和 track_info_detail
     * @param task
     * @param item
     */
    void saveOrUpdateInfoAndDetail(RequestTask task, JSONObject item) throws Exception;
    void saveOrUpdateInfoAndDetail(TrackInfo trackInfo, List<TrackInfoDetail> trackInfoDetails);
    
    TrackInfo selectById(String ordersMailCode);
    
}
