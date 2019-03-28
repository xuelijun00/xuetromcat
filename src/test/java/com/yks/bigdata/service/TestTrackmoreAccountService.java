package com.yks.bigdata.service;

import com.yks.bigdata.dao.TrackmoreAccountMapper;
import com.yks.bigdata.dto.trackmore.TrackmoreAccount;
import com.yks.bigdata.service.trackmore.ITrackmoreAccountService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by zh on 2017/7/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class TestTrackmoreAccountService {
    @Autowired
    ITrackmoreAccountService accountService;

    @Autowired
    TrackmoreAccountMapper mapper;

    @Test
    public void testSelectTrackmoreAccount(){
        TrackmoreAccount account = new TrackmoreAccount();
        account.setAccount("1107473538@163.com");
        List<TrackmoreAccount> trackmoreAccounts = accountService.selectTrackmoreAccount(account);
        System.out.println(trackmoreAccounts.size());
    }

    @Test
    public void testSelectAvailableAccount(){
        TrackmoreAccount trackmoreAccount = accountService.selectAvailableAccount();
        System.out.println(trackmoreAccount);
    }

}
