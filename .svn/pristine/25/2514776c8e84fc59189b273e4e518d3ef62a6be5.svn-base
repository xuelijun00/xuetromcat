package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackIBInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackIBInfoMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByTrackingCode(String trackingCode);

    int insert(TrackIBInfo record);

    int insertSelective(TrackIBInfo record);

    void addBatch(List<TrackIBInfo> list);

    List<TrackIBInfo> selectByIbInfo(TrackIBInfo record);

    TrackIBInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackIBInfo record);

    int updateByPrimaryKey(TrackIBInfo record);
}