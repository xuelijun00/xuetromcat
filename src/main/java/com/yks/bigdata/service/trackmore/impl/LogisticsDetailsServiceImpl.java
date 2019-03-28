package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.LogisticsDetailsMapper;
import com.yks.bigdata.dto.trackmore.LogisticsDetails;
import com.yks.bigdata.service.trackmore.ILogisticsDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
@Service
@Transactional
public class LogisticsDetailsServiceImpl implements ILogisticsDetailsService{

    private static Logger log = LoggerFactory.getLogger(LogisticsDetailsServiceImpl.class);

    @Autowired
    LogisticsDetailsMapper LogisticsDetails;



    @Override
    public List<LogisticsDetails> selectLogisticsDetails(LogisticsDetails channel) {
        return LogisticsDetails.selectLogisticsDetails(channel);
    }





}
