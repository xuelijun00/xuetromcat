package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dao.Track_infoMapper;
import com.yks.bigdata.dao.Track_info_detailMapper;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackInfo;
import com.yks.bigdata.dto.trackmore.TrackInfoDetail;
import com.yks.bigdata.service.trackmore.ITrackInfoService;
import com.yks.bigdata.util.DateUtils;
import com.yks.bigdata.util.ParseTrackmoreJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2017/7/6.
 */
@Service
@Transactional(value = "transactionManager")
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
     * @param response
     */
    @Override
    public void saveOrUpdateInfoAndDetail(RequestTask task, String response) {
        Integer taskId = task.getId();
        TrackInfo trackInfo = infoMapper.selectByTaskId(taskId);
        if (trackInfo == null) {
            //不存在 则进行insert
            insertInfoAndDetail(task, response);
        } else {
            //存在 则进行删除后 新增
            Integer infoId = trackInfo.getId();
            //删除 detail
            detailMapper.deleteByInfoId(infoId);
            //删除info
            infoMapper.deleteByPrimaryKey(infoId);
            insertInfoAndDetail(task, response);
        }
    }

    private void insertInfoAndDetail(RequestTask task, String response) {
        try {
            //插入info表
            TrackInfo info = ParseTrackmoreJson.parseTrackInfo(response);
            info.setTaskId(task.getId());
            infoMapper.insert(info);

            //插入detail表
            List<TrackInfoDetail> detailList = ParseTrackmoreJson.parseTrackInfoDetails(info.getId(), response);
            if (detailList.size() > 0)
                detailMapper.addBatch(detailList);
            else
                log.info("task id为 :" + task.getId() + " 内单号为:" + task.getErpOrdersId() + " 返回的json 没有info_detail信息!");

        } catch (Exception e) {
            log.error("解析json异常", e);
        }
    }

	@Override
	public TrackInfo selectById(String ordersMailCode) {
		return infoMapper.selectById(ordersMailCode);
	}


}
