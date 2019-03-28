package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.AbnormalMapper;
import com.yks.bigdata.dao.Track_infoMapper;
import com.yks.bigdata.dao.Track_info_detailMapper;
import com.yks.bigdata.dto.trackmore.TrackInfo;
import com.yks.bigdata.dto.trackmore.TrackInfoDetail;
import com.yks.bigdata.service.trackmore.ITrackInfoDetailService;
import com.yks.bigdata.service.trackmore.ITrackInfoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zh on 2017/7/6.
 */
@Service
@Transactional(value="transactionManager")
public class TrackInfoDetailServiceImpl implements ITrackInfoDetailService{

   @Autowired
   Track_info_detailMapper track_info_detail;

	@Autowired
	Track_infoMapper track_infoMapper;
	
	
	@Override
	public List<TrackInfoDetail> selectTrackInfoDetail(Integer id) {
		return track_info_detail.selectTrackInfoDetail(id);
	}

	@Override
	public List<TrackInfoDetail> selectStatusInformation(String ordersMailCode) {

		TrackInfo trackInfo = track_infoMapper.selectById(ordersMailCode);
		if(trackInfo==null){
			return null;
		}
		List<TrackInfoDetail> detail = track_info_detail.selectTrackInfoDetail(trackInfo.getId());

		for(TrackInfoDetail string : detail){
			System.out.println("详情"+string.getDetails());
		}

		return detail;
	}


}
