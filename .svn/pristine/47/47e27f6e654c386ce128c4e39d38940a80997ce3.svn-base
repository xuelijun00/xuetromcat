package com.yks.bigdata.dao;

import java.util.List;

import com.yks.bigdata.dto.trackmore.Abnormal;
import com.yks.bigdata.dto.trackmore.LogisticsDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsDetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsDetails record);

    int insertSelective(LogisticsDetails record);

    LogisticsDetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsDetails record);

    int updateByPrimaryKey(LogisticsDetails record);
    
    List<LogisticsDetails> selectLogisticsDetails(LogisticsDetails record);
    
}