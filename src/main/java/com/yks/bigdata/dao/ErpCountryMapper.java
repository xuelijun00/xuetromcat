package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.ErpCountry;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErpCountryMapper {
    int deleteByPrimaryKey(Integer countryId);

    int insert(ErpCountry record);

    int insertSelective(ErpCountry record);

    ErpCountry selectByPrimaryKey(Integer countryId);

    int updateByPrimaryKeySelective(ErpCountry record);

    int updateByPrimaryKey(ErpCountry record);

    List<ErpCountry> selectErpCountry();
}