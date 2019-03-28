package com.yks.bigdata.service.es.base;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zh on 2017/6/28.
 */
public abstract class ExBaseService {

    public EsQueryResponse simpleQuery(EsQueryParamterMap map){
        map.getUrl();
        Map<String, String> filterFileds = map.getFilterFileds();
        ArrayList<String> showFileds = map.getShowFileds();


        return null;
    }
}
