package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.LogisticsPickup;
import com.yks.bigdata.vo.UpdatePickUp;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Logistics_pickupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsPickup record);

    int insertSelective(LogisticsPickup record);

    LogisticsPickup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsPickup record);

    int updateByPrimaryKey(LogisticsPickup record);

    void addBatch(List<LogisticsPickup> pickupList);


    /**
     * 模糊查询
     * 分页
     * @param pickup
     * @return
     */
    public List<LogisticsPickup> selectLogisticsPickup(LogisticsPickup pickup);

    public List<LogisticsPickup> existsPickUp(List<String> ids);

    public void updatePickupToAggred(String orderId);
    public void updatePickupToAggredBatch(List<String> orderIds);

    public void updateStatusRegistered(UpdatePickUp pickUp);

    LogisticsPickup selectLogisticsPickupByOrderId(String orderId);
}