package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dao.Track_infoMapper;
import com.yks.bigdata.dao.Track_info_detailMapper;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackInfo;
import com.yks.bigdata.dto.trackmore.TrackInfoDetail;
import com.yks.bigdata.service.trackmore.ITrackInfoService;
import com.yks.bigdata.util.ParseTrackmoreJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by zh on 2017/7/6.
 */
@Service
public class TrackInfoServiceImpl implements ITrackInfoService {

    private static Logger log = LoggerFactory.getLogger(TrackInfoServiceImpl.class);

    @Autowired
    Track_infoMapper infoMapper;

    @Autowired
    Track_info_detailMapper detailMapper;

    /**
     * 将结果写入或者更新 track_info 和 track_info_detail
     *
     * @param task
     * @param item
     */
    @Override
    @Transactional(value = "transactionManager")
    public void saveOrUpdateInfoAndDetail(RequestTask task, JSONObject item) throws Exception {
        Integer taskId = task.getId();
        TrackInfo trackInfo = infoMapper.selectByTaskId(taskId);
        if (trackInfo == null) {
            insertInfoAndDetail(task, item);//不存在 则进行insert
        } else {
            Integer infoId = trackInfo.getId();//存在 则进行删除后 新增
            detailMapper.deleteByInfoId(infoId); //删除 detail
            infoMapper.deleteByPrimaryKey(infoId); //删除info
            insertInfoAndDetail(task, item);
        }
    }

    @Transactional(value = "transactionManager")
    @Override
    public void saveOrUpdateInfoAndDetail(TrackInfo trackInfo, List<TrackInfoDetail> trackInfoDetails){
        TrackInfo trackInfo1 = infoMapper.selectByTaskId(trackInfo.getTaskId());
        if (trackInfo1 != null) {
            Integer infoId = trackInfo1.getId();//存在 则进行删除后 新增
            detailMapper.deleteByInfoId(infoId); //删除 detail
            infoMapper.deleteByPrimaryKey(infoId); //删除info
        }
        infoMapper.insert(trackInfo);//插入info表
        for (TrackInfoDetail trackInfoDetail:trackInfoDetails) {
            trackInfoDetail.setTrackInfoId(trackInfo.getId());
        }
        detailMapper.addBatch(trackInfoDetails);//插入detail表
    }

    private void insertInfoAndDetail(RequestTask task, JSONObject item) throws Exception {
        TrackInfo info = ParseTrackmoreJson.parseTrackInfo(item);
        info.setTaskId(task.getId());
        infoMapper.insert(info);//插入info表

        List<TrackInfoDetail> detailList = ParseTrackmoreJson.parseTrackInfoDetails(info.getId(), item);
        if(!CollectionUtils.isEmpty(detailList)){
            detailMapper.addBatch(detailList);//插入detail表
        }
    }

	@Override
	public TrackInfo selectById(String ordersMailCode) {
		return infoMapper.selectById(ordersMailCode);
	}


}
