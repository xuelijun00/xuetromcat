package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticReport;
import com.yks.bigdata.dto.trackmore.LogisticsStatus;
import com.yks.bigdata.dto.trackmore.TrackInfoDetail;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zh on 2017/6/29.
 */
public interface IErpOrdersService {

    void importErpOrders() throws Exception;

    /**
     * 查询订单是否存在
     *
     * @return
     * @throws SQLException
     */
    List<ErpOrders> existsOrders(String ids);

    ErpOrders selectOrderByOrderId(Long erpOrderId);

    /**
     * 根据条件获取多个渠道信息
     * @param channel
     * @return
     */
    List<ErpOrders> selectErpOrders(ErpOrders channel);


    List<String> selectbuyerCountry();
    
    List<String> selectplatform();
    
    List<String> selectchannelName();

    void updateTrackmoreStatus(ErpOrders orders);

    /**
     * 查询异常订单
     * @param channel
     * @return
     */
    List<ErpOrders> selectExceptionErpOrders(ErpOrders channel);

    /**
     * 查询订单明细
     * @param channel
     * @return
     */
    List<ErpOrders> selectErpOrdersDetails(ErpOrders channel);

    void updateErpOrders(ErpOrders channel);


    /**
     * 报表
     */
    List<LogisticReport> selectReportData(LogisticReport report);

    List<LogisticReport> selectReportDataChannel(LogisticReport report);

}
