package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.Abnormal;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticsDetails;
import com.yks.bigdata.dto.trackmore.Physical;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zh on 2017/6/29.
 */
public interface ILogisticsDetailsService {


    /**
     * 查询订单是否存在
     *
     * @return
     * @throws SQLException
     */

	public List<LogisticsDetails> selectLogisticsDetails(LogisticsDetails channel);

}
