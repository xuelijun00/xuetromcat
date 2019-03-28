package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackInfoDetail;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Track_info_detailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrackInfoDetail record);

    int insertSelective(TrackInfoDetail record);

    TrackInfoDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackInfoDetail record);

    int updateByPrimaryKey(TrackInfoDetail record);

    void deleteByInfoId(Integer infoId);

    void addBatch(List<TrackInfoDetail> detailList);
   
    List<TrackInfoDetail> selectTrackInfoDetail(@Param("trackInfoId")Integer trackInfoId);
}