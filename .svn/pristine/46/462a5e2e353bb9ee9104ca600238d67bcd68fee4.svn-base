package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.LogisticsStatus;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Logistics_statusMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsStatus record);

    void insertBatch(List<LogisticsStatus> list);

    int insertSelective(LogisticsStatus record);

    LogisticsStatus selectByPrimaryKey(Integer id);

    List<LogisticsStatus> selectByChannel(String channel);

    int updateByPrimaryKeySelective(LogisticsStatus record);

    int updateByPrimaryKey(LogisticsStatus record);

  //  List<Logistics_status> selectLogisticsStatus(Logistics_status account);

    List<LogisticsStatus> selectLogisticsStatus(LogisticsStatus status);

}