package com.yks.bigdata.es;

import com.yks.bigdata.service.es.BulkLoadOrdersService;
import com.yks.bigdata.vo.PickUp;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by zh on 2017/6/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBulkLoadOrderService {

    @Autowired
    BulkLoadOrdersService bulkLoadOrdersService;

    @Test
    public void loadOrderServic()throws Exception{
        bulkLoadOrdersService.bulkLoadOrders();
    }

    @Test
    public void testBatchUpdateOrderJsonStr() throws Exception {
        PickUp pickUp = new PickUp(1083044816003L, "Y266S", "泉州");
        String s = bulkLoadOrdersService.updateOrderJsonStr(pickUp);
        System.out.println(s);
    }

    @Test
    public void testProcessOrders()throws Exception{

        String str = IOUtils.toString(new FileInputStream("data/csv/execel.csv"));
        String[] ids = str.split("\r\n");
        ArrayList<PickUp> pickUps = new ArrayList<>();
        for (String id:ids){
            pickUps.add(new PickUp(Long.parseLong(id), "Y266S", "泉州"));
        }
//        PickUp pickUp = new PickUp(1083044816003L, "Y266S", "泉州");
//        PickUp pickUp2 = new PickUp(1083044279003L, "Y266S", "泉州");
//        PickUp pickUp3 = new PickUp(1083045544003L, "Y266S", "泉州");
//
//
//        pickUps.add(pickUp);
//        pickUps.add(pickUp2);
//        pickUps.add(pickUp3);

        bulkLoadOrdersService.processOrders(pickUps);

    }


}
