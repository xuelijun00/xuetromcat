package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.ErpOrders;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by zh on 2017/6/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class TestErpOrderDao {

    @Autowired
    Erp_ordersMapper erp_ordersMapper;

    @Test
    public void selectAvailablePickUpOrders(){
        List<ErpOrders> erp_orderses = erp_ordersMapper.selectAvailablePickUpOrders();
        System.out.println(erp_orderses.size());
    }


}
