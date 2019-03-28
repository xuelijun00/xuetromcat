package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Track_sourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrackSource record);

    int insertSelective(TrackSource record);

    TrackSource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackSource record);

    int updateByPrimaryKeyWithBLOBs(TrackSource record);

    int updateByPrimaryKey(TrackSource record);

    void addBatch(List<TrackSource> sources);


}