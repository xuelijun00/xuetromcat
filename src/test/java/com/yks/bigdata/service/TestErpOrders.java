package com.yks.bigdata.service;

import com.yks.bigdata.dao.Erp_ordersMapper;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.impl.YksAccountServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class TestErpOrders {

    @Autowired
    IErpOrdersService erpOrdersService;

    @Autowired
    Erp_ordersMapper ordersMapper;

    @Autowired
    YksAccountServiceImpl yksAccount;

    @Test
    public void importAccount() throws Exception {
        yksAccount.importYksAccount();
    }

    @Test
    public void importErpOrders() throws Exception {
        erpOrdersService.importErpOrders();
    }

    @Test
    public void selectAvliOrders()throws Exception{
        List<ErpOrders> erpOrderses = ordersMapper.selectAvailablePickUpOrders();
        System.out.println(erpOrderses);
    }


    @Test
    public void testErp() throws SQLException {
        List<ErpOrders> erp_orderses1 = erpOrdersService.existsOrders("1111633019082");
        System.out.println(erp_orderses1.size());
    }

    @Test
    public void testErpTrackStatus() throws SQLException {
        ErpOrders orders = new ErpOrders();
        orders.setErpOrdersId(1083051644003l);
        orders.setTrackStatus(1);
        erpOrdersService.updateTrackmoreStatus(orders);
    }


}
