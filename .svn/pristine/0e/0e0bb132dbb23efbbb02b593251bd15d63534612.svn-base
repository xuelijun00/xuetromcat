package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackYtInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackYtInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByTrackNum(String shipmentId);
    void addBatch(List<TrackYtInfo> list);

    int insert(TrackYtInfo record);

    int insertSelective(TrackYtInfo record);

    TrackYtInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackYtInfo record);

    int updateByPrimaryKey(TrackYtInfo record);
}