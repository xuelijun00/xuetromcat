package com.yks.bigdata.service.trackmore;

import java.util.List;

import com.yks.bigdata.dto.trackmore.TrackInfoDetail;

/**
 * Created by zh on 2017/7/6.
 */
public interface ITrackInfoDetailService {
	
	List<TrackInfoDetail> selectTrackInfoDetail(Integer id);

	List<TrackInfoDetail> selectStatusInformation(String ordersMailCode);
	
}
