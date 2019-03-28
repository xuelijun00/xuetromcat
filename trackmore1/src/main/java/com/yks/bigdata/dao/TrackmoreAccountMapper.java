package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackmoreAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackmoreAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrackmoreAccount record);

    int insertSelective(TrackmoreAccount record);

    TrackmoreAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackmoreAccount record);

    int updateByPrimaryKey(TrackmoreAccount record);

    /**
     * 获取可用的account 额度大于0的账号
     * @return
     */
    TrackmoreAccount selectAvailableAccount(Integer quto);

    /**
     * 按照条件查询账号
     * @param account
     * @return
     */
    List<TrackmoreAccount> selectTrackmoreAccount(TrackmoreAccount account);

    TrackmoreAccount selectByAccount(String account);
}