package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.LogisticsChannelMapper;
import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import com.yks.bigdata.service.trackmore.ILogisticsChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh on 2017/6/29.
 */
@Service
public class LogisticsChannelServiceImpl implements ILogisticsChannelService{

    @Autowired
    LogisticsChannelMapper channelMapper;

    @Transactional(value = "transactionManager")
    @Override
    public Integer insert(LogisticsChannel channel) {
        return channelMapper.insert(channel);
    }

    @Transactional(value = "transactionManager")
    @Override
    public void update(LogisticsChannel channel) {
        channelMapper.updateByPrimaryKeySelective(channel);
    }

    @Override
    public List<LogisticsChannel> selectLogisticsChannels(LogisticsChannel channel){
        return channelMapper.selectLogisticsChannel(channel);
    }

    @Override
    public List<String> selectLogisticsChannelCode(){
        return channelMapper.selectLogisticsChannelCode();
    }

    @Override
    public LogisticsChannel selectLogisticsChannelById(Integer id){
        return channelMapper.selectByPrimaryKey(id);
    }

    @Override
    public void disableOrEnabledChannel(Integer id, Integer status) {
        LogisticsChannel channel = new LogisticsChannel();
        channel.setId(id);
        channel.setStatus(status);
        channelMapper.updateByPrimaryKeySelective(channel);
    }

    /**
     * 批量插入
     *
     * @param list
     */
    @Override
    public void insertBatch(List<LogisticsChannel> list) {
        if(!CollectionUtils.isEmpty(list)){
            channelMapper.insertBatch(list);
        }
    }

    /**
     * 生成request task 需要的数据
     * //获取已启用的物流渠道
     * @return
     */
    @Override
    public Map<String, String> selectLogisticsChannels() {
        LogisticsChannel channel = new LogisticsChannel();
        channel.setStatus(1);
        List<LogisticsChannel> logisticsChannels = channelMapper.selectLogisticsChannel(channel);
        Map<String, String> map = new HashMap<>();
        for (LogisticsChannel ch:logisticsChannels) {
            map.put(ch.getYksChannelName(),ch.getShortCode());
        }
        return map;
    }
}
