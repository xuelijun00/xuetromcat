package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.TrackmoreAccount;
import com.yks.bigdata.util.DateUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/6/29.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class TrackmoreAccountMapperTest {

    @Autowired
    private TrackmoreAccountMapper trackmoreAccountMapper;

    @Test
    public void deleteByPrimaryKey() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        TrackmoreAccount trackmoreAccount = new TrackmoreAccount();
        trackmoreAccount.setId(1);
        trackmoreAccount.setAccount("1107473538@163.com");
        trackmoreAccount.setApiKey("c7cdeb1d-7fa2-4fe2-97a6-73a7c64768a7");
        trackmoreAccount.setCreateTime(new Date());
        trackmoreAccount.setStatus(1);
        trackmoreAccount.setDataCount(1500000);
        int insert = trackmoreAccountMapper.insert(trackmoreAccount);
        Assert.assertEquals(1,insert);
    }

    @Test
    public void insertSelective() throws Exception {
    }

    @Test
    public void selectByPrimaryKey() throws Exception {
    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {
    }

    @Test
    public void updateByPrimaryKey() throws Exception {
    }

}