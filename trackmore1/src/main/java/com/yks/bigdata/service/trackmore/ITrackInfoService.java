package com.yks.bigdata.service.trackmore;

import java.util.List;

import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackInfo;

/**
 * Created by zh on 2017/7/6.
 */
public interface ITrackInfoService {

    /**
     * 将结果写入或者更新 track_info 和 track_info_detail
     * @param task
     * @param response
     */
    public void saveOrUpdateInfoAndDetail(RequestTask task,String response);
    
    TrackInfo selectById(String ordersMailCode);
    
}
