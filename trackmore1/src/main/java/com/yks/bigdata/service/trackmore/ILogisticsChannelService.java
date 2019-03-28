package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/30.
 */
public interface ILogisticsChannelService {

    /**
     * 插入单个渠道
     * @param channel
     */
    Integer insert(LogisticsChannel channel);

    /**
     * 更新
     * @param channel
     */
    void update(LogisticsChannel channel);

    /**
     * 根据条件获取多个渠道信息
     * @param channel
     * @return
     */
    List<LogisticsChannel> selectLogisticsChannels(LogisticsChannel channel);

    /**
     * 生成request task 需要的数据
     * @return
     */
    Map<String,String> selectLogisticsChannels();

    List<String> selectLogisticsChannelCode();

    /**
     * 根据id查询渠道
     * @return
     */
    LogisticsChannel selectLogisticsChannelById(Integer id);

    /**
     * 禁用或启动通道
     * @param id
     */
    void disableOrEnabledChannel(Integer id,Integer status);

    /**
     * 批量插入
     * @param list
     */
    void insertBatch(List<LogisticsChannel> list);

}
