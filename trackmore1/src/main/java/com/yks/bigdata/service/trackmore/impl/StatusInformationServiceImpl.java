package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.LogisticsChannelMapper;
import com.yks.bigdata.dao.Logistics_statusMapper;
import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import com.yks.bigdata.dto.trackmore.LogisticsStatus;
import com.yks.bigdata.service.trackmore.IStatusInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
@Service
public class StatusInformationServiceImpl implements IStatusInformationService {

    @Autowired
    Logistics_statusMapper statusMapper;

    @Autowired
    LogisticsChannelMapper channelMapper;

    @Override
    public void update(LogisticsStatus status) {
        statusMapper.updateByPrimaryKeySelective(status);
    }

    @Override
    public void enable(LogisticsStatus status) {

    }

    @Override
    public void disable(LogisticsStatus status) {

    }

    /**
     * 批量插入
     *
     * @param list
     */
    @Transactional( value = "transactionManager")
    @Override
    public void insertBatch(List<LogisticsStatus> list) {
        statusMapper.insertBatch(list);
    }

    @Override
    public List<LogisticsChannel> selectLogisticsStatusChannel() {
        return channelMapper.selectLogisticsChannel(null);
    }

    @Override
    public List<LogisticsStatus> selectStatusInformation(LogisticsStatus status) {
        return statusMapper.selectLogisticsStatus(status);
    }

    @Override
    public List<LogisticsStatus> selectByChannel(String channel) {
        return statusMapper.selectByChannel(channel);
    }

    @Override
    public LogisticsStatus selectStatusInformationById(Integer id){
        return statusMapper.selectByPrimaryKey(id);
    }

	@Override
	public Integer insert(LogisticsStatus status) {
		return statusMapper.insert(status);
	}

}
