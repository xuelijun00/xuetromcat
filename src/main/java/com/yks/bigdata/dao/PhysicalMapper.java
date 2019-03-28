package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.Physical;

import java.util.List;

public interface PhysicalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Physical record);

    int insertSelective(Physical record);

    Physical selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Physical record);


    int updateByPrimaryKey(Physical record);

    List<Physical> selectPhysical(Physical channel);

    List<String> selectplatform();
    
    List<String> selectCountry();
    
    List<String> selectchannel();
}