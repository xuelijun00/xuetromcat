package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsChannelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsChannel record);

    int insertSelective(LogisticsChannel record);

    LogisticsChannel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsChannel record);

    int updateByPrimaryKey(LogisticsChannel record);

    /**
     * 按照ykschannelcode 获取渠道
     * @param yksChannelName
     * @return
     */
    List<LogisticsChannel> retrievelByYksChannelName(List<String> yksChannelName);

    /**
     * 按条件查询渠道信息
     * @param channel
     * @return
     */
    List<LogisticsChannel> selectLogisticsChannel(LogisticsChannel channel);

    /**
     * 查询trackmore 渠道编码 （distinct）
     * @return
     */
    List<String> selectLogisticsChannelCode();

    /**
     * 批量插入
     * @param list
     */
    void insertBatch(List<LogisticsChannel> list);


}