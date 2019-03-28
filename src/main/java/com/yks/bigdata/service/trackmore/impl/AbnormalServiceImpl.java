package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.AbnormalMapper;
import com.yks.bigdata.dto.trackmore.Abnormal;
import com.yks.bigdata.service.trackmore.IAbnormalService;
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
public class AbnormalServiceImpl implements IAbnormalService{

    private static Logger log = LoggerFactory.getLogger(AbnormalServiceImpl.class);

    @Autowired
    AbnormalMapper Abnormal;



    @Override
    public List<Abnormal> selectAbnormal(Abnormal channel) {
        return Abnormal.selectAbnormal(channel);
    }


    @Override
    public List<String> selectbuyerCountry(){
        return Abnormal.selectbuyerCountry();
    }




}
