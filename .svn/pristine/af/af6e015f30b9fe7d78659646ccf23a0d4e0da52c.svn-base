package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackLWEInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackLWEInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByTrackNum(String trackNum);

    int insert(TrackLWEInfo record);

    int insertSelective(TrackLWEInfo record);

    void addBatch(List<TrackLWEInfo> records);

    TrackLWEInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackLWEInfo record);

    int updateByPrimaryKey(TrackLWEInfo record);
}