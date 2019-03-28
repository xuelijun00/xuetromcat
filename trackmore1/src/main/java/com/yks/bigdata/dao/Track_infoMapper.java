package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Track_infoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrackInfo record);

    int insertSelective(TrackInfo record);

    TrackInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackInfo record);

    int updateByPrimaryKey(TrackInfo record);

    TrackInfo selectByTaskId(Integer taskId);
    
    TrackInfo selectById(@Param("trackingNumber")String trackingNumber);
}