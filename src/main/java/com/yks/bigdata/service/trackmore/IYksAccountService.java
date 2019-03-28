package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.YksAccount;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxing on 2017/7/14.
 */
public interface IYksAccountService {

    Integer insert(YksAccount account);

    void insertBatch(List<YksAccount> list);

    List<YksAccount> selectYksAccounts();

    void importYksAccount() throws SQLException;

    Map<String,String> getPlatform();
}
