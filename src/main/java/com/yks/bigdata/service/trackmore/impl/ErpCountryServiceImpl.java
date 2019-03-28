package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.ErpCountryMapper;
import com.yks.bigdata.dto.trackmore.ErpCountry;
import com.yks.bigdata.service.trackmore.IErpCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxing on 2017/8/24.
 */
@Service
public class ErpCountryServiceImpl implements IErpCountryService {

    @Autowired
    ErpCountryMapper erpCountryMapper;

    @Override
    public List<ErpCountry> selectErpCountry() {
        return erpCountryMapper.selectErpCountry();
    }

    @Override
    public Map<String, String> selectErpCountryMap() {
        Map<String, String> map = new HashMap<>();
        List<ErpCountry> erpCountries = erpCountryMapper.selectErpCountry();
        for(ErpCountry erpCountry:erpCountries){
            map.put(erpCountry.getCountryEn(),erpCountry.getCountryCn());
        }
        return map;
    }
}
