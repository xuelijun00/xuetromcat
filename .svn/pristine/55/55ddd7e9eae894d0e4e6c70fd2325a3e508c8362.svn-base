package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import com.yks.bigdata.dto.trackmore.LogisticsStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
public interface IStatusInformationService {

    Integer insert(LogisticsStatus status);
    void update(LogisticsStatus status);

    void enable(LogisticsStatus status);
    void disable(LogisticsStatus status);

    List<LogisticsChannel> selectLogisticsStatusChannel();

    List<LogisticsStatus> selectStatusInformation(LogisticsStatus status);

    List<LogisticsStatus> selectByChannel(String channel);

    /**
     * 根据id查询渠道
     * @return
     */
    LogisticsStatus selectStatusInformationById(Integer id);

    /**
     * 批量插入
     * @param list
     */
    void insertBatch(List<LogisticsStatus> list);
}
