package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dao.Track_sourceMapper;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackSource;
import com.yks.bigdata.service.trackmore.ITrackSourceService;
import com.yks.bigdata.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2017/7/6.
 */
@Service
public class TrackSourceServiceImpl implements ITrackSourceService {

    @Autowired
    Track_sourceMapper sourceMapper;

    @Override
    @Transactional(value="transactionManager")
    public void addBatch(List<RequestTask> registerTasks, String response) {
        ArrayList<TrackSource> result = new ArrayList<TrackSource>();
        for (RequestTask task:registerTasks){
            TrackSource trackSource = new TrackSource();
            trackSource.setSource(response);
            trackSource.setCreateAt(DateUtils.getCurrentDateStr());
            trackSource.setTaskId(task.getId());
            result.add(trackSource);
        }
        sourceMapper.addBatch(result);
    }

    @Override
    @Transactional(value="transactionManager")
    public void add(RequestTask task, String response) {
        TrackSource trackSource = new TrackSource();
        trackSource.setSource(response);
        trackSource.setCreateAt(DateUtils.getCurrentDateStr());
        trackSource.setTaskId(task.getId());
        sourceMapper.insert(trackSource);
    }
}
