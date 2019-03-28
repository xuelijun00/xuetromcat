package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackXgdhlInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackXgdhlInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByTrackNum(String shipmentId);

    int insert(TrackXgdhlInfo record);

    void addBatch(List<TrackXgdhlInfo> list);

    int insertSelective(TrackXgdhlInfo record);

    TrackXgdhlInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackXgdhlInfo record);

    int updateByPrimaryKey(TrackXgdhlInfo record);
}