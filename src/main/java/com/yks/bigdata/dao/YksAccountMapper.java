package com.yks.bigdata.dao;


import com.yks.bigdata.dto.trackmore.YksAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YksAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YksAccount record);

    int insertSelective(YksAccount record);

    YksAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YksAccount record);

    int updateByPrimaryKey(YksAccount record);

    void insertBatch(List<YksAccount> list);

    List<YksAccount> selectYksAccounts();

    YksAccount selectByAccount(String account);

    void deleteData();
}