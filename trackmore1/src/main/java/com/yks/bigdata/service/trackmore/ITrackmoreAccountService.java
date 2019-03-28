package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.TrackmoreAccount;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
public interface ITrackmoreAccountService {

    public int insert(TrackmoreAccount account);
    public void update(TrackmoreAccount account);

    public void enable(TrackmoreAccount account);
    public void disable(TrackmoreAccount account);

    List<TrackmoreAccount> selectTrackmoreAccount(TrackmoreAccount account);

    /**
     * 获取可用的account 额度大于0的账号 并且额度减少
     * @param quto
     * @return
     */
    TrackmoreAccount selectAvailableReduceAccount(Integer quto);

    /**
     * 获取额度大于40的账号
     * @return
     */
    TrackmoreAccount selectAvailableAccount();

    /**
     * 减少账号额度
     * @param id
     * @param reduceNum
     */
    void reduceDataCount(Integer id,Integer reduceNum);

    TrackmoreAccount selectByAccount(String account);
}
