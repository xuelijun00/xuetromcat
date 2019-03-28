package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticReport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sun.rmi.runtime.Log;

import java.util.List;

@Repository
public interface Erp_ordersMapper {
    int deleteByPrimaryKey(Long erpOrdersId);

    ErpOrders selectByOrdersMailCode(String ordersMailCode);

    int insert(ErpOrders record);

    int insertSelective(ErpOrders record);

    ErpOrders selectByPrimaryKey(Long erpOrdersId);

    int updateByPrimaryKeySelective(ErpOrders record);

    int updateByPrimaryKey(ErpOrders record);

    void updateExceptionStatus(ErpOrders record);

    /**
     * 获取 需要 取件execel中的erp_orders_id中的orders
     * @return
     */
    List<ErpOrders> selectAvailablePickUpOrders();

    /**
     * 查询订单 国家 （distinct）
     * @return
     */
    List<String> selectbuyerCountry();
    
    List<String> selectplatform();
    
    List<String> selectchannelName();

    void addBatch(List<ErpOrders> list);

    List<ErpOrders> existsOrders(List<Long> orderses);

    ErpOrders existsOrder(Long erpOrderId);

    /**
     * 按条件查询渠道信息
     * @param orders
     * @return
     */
    List<ErpOrders> selectErpOrders(ErpOrders orders);

    Long selectErpOrdersChannelCount(ErpOrders orders);
    List<ErpOrders> selectErpOrdersChannel(ErpOrders orders);

    /**
     * 查询异常订单
     * @param orders
     * @return
     */
    List<ErpOrders> selectExceptionErpOrders(ErpOrders orders);


    /**
     * 查询订单明细
     * @param orders
     * @return
     */
    List<ErpOrders> selectErpOrdersDetails(ErpOrders orders);

    /**
     * 报表
     */
    List<LogisticReport> selectReportData(LogisticReport report);

    List<LogisticReport> selectReportDataAndDate(LogisticReport report);

    /**
     * 报表
     */
    List<LogisticReport> selectReportDataChannel(LogisticReport report);

    /**
     * 增加异常备注
     * @param orders
     */
    void updateExceptionMessage(ErpOrders orders);

    /**
     * 确定此订单是否存在
     * @param erpOrderId
     * @return
     */
    Integer selectCountByOrderId(Long erpOrderId);

    /**
     * 更新出货时间
     * @param orders
     * @return
     */
    int updateOrderOutTime(ErpOrders orders);

}