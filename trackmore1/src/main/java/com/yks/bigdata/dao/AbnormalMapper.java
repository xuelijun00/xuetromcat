package com.yks.bigdata.dao;

import java.util.List;

import com.yks.bigdata.dto.trackmore.Abnormal;
import com.yks.bigdata.dto.trackmore.Physical;

public interface AbnormalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Abnormal record);

    int insertSelective(Abnormal record);

    Abnormal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Abnormal record);

    int updateByPrimaryKey(Abnormal record);
    
    
    List<Abnormal> selectAbnormal(Abnormal channel);

    List<String> selectbuyerCountry();
}