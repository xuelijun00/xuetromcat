package com.yks.bigdata.dao;


import com.yks.bigdata.dto.system.SystemException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemExceptionMapper {

    int insert(SystemException record);

    int insertSelective(SystemException record);

    void customizeSql(@Param("sql") String sql);
}