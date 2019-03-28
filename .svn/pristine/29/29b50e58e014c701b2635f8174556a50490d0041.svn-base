package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.YksAccountMapper;
import com.yks.bigdata.dto.trackmore.YksAccount;
import com.yks.bigdata.service.trackmore.IYksAccountService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxing on 2017/7/14.
 */
@Service
public class YksAccountServiceImpl implements IYksAccountService {

    @Autowired
    YksAccountMapper yksAccountMapper;

    @Resource(name = "biDataSource")
    private DataSource biDataSource;

    @Override
    public Integer insert(YksAccount account) {
        return yksAccountMapper.insert(account);
    }

    @Override
    public void insertBatch(List<YksAccount> list) {
        yksAccountMapper.insertBatch(list);
    }

    @Override
    public List<YksAccount> selectYksAccounts() {
        return yksAccountMapper.selectYksAccounts();
    }

    @Override
    public Map<String, String> getPlatform() {
        Map<String, String> map = new HashedMap();
        List<YksAccount> yksAccounts = selectYksAccounts();
        for (YksAccount account:yksAccounts) {
            map.put(account.getAccount(),account.getPlatform());
        }
        return map;
    }

    @Override
    public void importYksAccount() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "SELECT `platform`, `account`, `manager`, `ebayfinalvaluetype`, `zhandian`, `groupleader` FROM `configaccount`";
        List<YksAccount> accounts = (List<YksAccount>)runner.query(biDataSource.getConnection(),sql,new BeanListHandler(YksAccount.class));
        //清空数据
        yksAccountMapper.deleteData();
        //插入账号
        insertBatch(accounts);
    }

}
