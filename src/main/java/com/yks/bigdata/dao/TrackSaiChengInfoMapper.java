package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackSaiChengInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackSaiChengInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrackSaiChengInfo record);

    int insertSelective(TrackSaiChengInfo record);

    TrackSaiChengInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackSaiChengInfo record);

    int updateByPrimaryKey(TrackSaiChengInfo record);

    int deleteByTrackNum(String trackingNumber);

    void addBatch(List<TrackSaiChengInfo> saiChengInfos);
}