package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.LogisticsPickup;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.web.vo.MessageVo;

import java.util.List;

/**
 * Created by zh on 2017/6/29.
 */
public interface ILogisticsPickupService {

    /**
     * 批量导入
     * @param pickupList
     * @return
     */
    Integer insertBatch(List<LogisticsPickup> pickupList);

    /**
     * 模糊查询
     * 分页
     * @return
     */
    public List<LogisticsPickup> selectLogisticsPickup(LogisticsPickup pickup);


    public void updatePickupToAggred(String orderId);

    public void updatePickupToAggredBatch(List<String> orderIds);

    /**
     * pickup表中register_status更新为1 ,新增 register_time
     * @param registerTasks
     */
    public void updateStatusRegistered(List<RequestTask> registerTasks);

    LogisticsPickup selectLogisticsPickupByOrderId(String orderId);

}
