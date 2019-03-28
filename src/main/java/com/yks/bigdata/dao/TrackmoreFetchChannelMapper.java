package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackmoreFetchChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackmoreFetchChannelMapper {
    int deleteByPrimaryKey(TrackmoreFetchChannel key);

    int insert(TrackmoreFetchChannel record);

    int insertSelective(TrackmoreFetchChannel record);

    TrackmoreFetchChannel selectByPrimaryKey(TrackmoreFetchChannel key);

    List<TrackmoreFetchChannel> selectByStatus(Integer fetchStatus);

    int updateByPrimaryKeySelective(TrackmoreFetchChannel record);

    int updateByPrimaryKey(TrackmoreFetchChannel record);
}