package com.yks.bigdata.service.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.common.Constants;
import com.yks.bigdata.util.EsJsonParseUtils;
import com.yks.bigdata.util.EsUtils;
import com.yks.bigdata.vo.Logistics_channel;
import com.yks.bigdata.vo.Logistics_channelPut;
import com.yks.bigdata.vo.Orders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by zh on 2017/6/26.
 */
@Service
public class ChannelService {

    public static void main(String[] args)throws  Exception {
        String url = Constants.full_index_type_logistic_channel;
        String s = JSON.toJSONString(new Logistics_channelPut("turkey-post", "Turkey Post", "土耳其物流", "Y266S"));
        EsUtils.post(url, s);
    }





    /**
     * 从es中获取Logistics_channel物流渠道
     *
     * @return
     * @throws Exception
     */
    public Logistics_channel getAvailableChannel() throws Exception {
        String url = Constants.full_index_type_logistic_channel + "/_search";
        String json = "{}";
        String response = EsUtils.get(url, json);
        JSONObject rootObject = JSON.parseObject(response);
        JSONArray hitsArray = rootObject.getJSONObject("hits").getJSONArray("hits");
        Integer total = (Integer) rootObject.getJSONObject("hits").get("total");
        if (total <= 0) {
            System.out.println("channel数目为0");
            return null;
        }
        new ArrayList<Logistics_channelPut>();
        for (int i = 0; i < hitsArray.size(); i++) {
            JSONObject hit = hitsArray.getJSONObject(i);
            JSONObject source = hit.getJSONObject("_source");
            return new Logistics_channel((String)hit.get("_id"),(String) source.get("short_code"), (String) source.get("english_name"), (String) source.get("chiness_name"), (String) source.get("yks_channel_name"));
        }
        return null;
    }

    /**
     * 往order对象 添加物流渠道信息
     *
     * @param order
     * @throws Exception
     */
    public void addChannel(Orders order) throws Exception {
        Logistics_channel logistics_channel = queryChannelByYksChannelName(order.getFreightcode());
        order.setChanel(logistics_channel);
    }


    /**
     * 获取所有的物流渠道信息
     *
     * @param yksChannelName 如Y266S
     * @return LogisticsChannel
     * @throws Exception
     */
    public Logistics_channel queryChannelByYksChannelName(String yksChannelName) throws Exception {
        String url = Constants.full_index_type_logistic_channel + "/_search";
        String response = EsUtils.get(url, null);
        Logistics_channel logistics_channel = parseLogistics_channelHits(response, yksChannelName);
        return logistics_channel;
    }

    /**
     * 解析 queryChannelByYksChannelName 的hit
     *
     * @param response
     * @param yksChannelName
     * @return
     */
    private Logistics_channel parseLogistics_channelHits(String response, String yksChannelName) {
        String errorMsg = yksChannelName + " 该Logistics_channelPut 不存在";
        JSONArray hits = EsJsonParseUtils.parseHits(response, errorMsg);

        for (int i = 0; i < hits.size(); i++) {
            JSONObject hit = hits.getJSONObject(i);
            JSONObject source = hit.getJSONObject("_source");
            return new Logistics_channel((String)hit.get("_id"), (String) source.get("short_code"), (String) source.get("english_name"), (String) source.get("chiness_name"), (String) source.get("yks_channel_name"));
        }
        return null;
    }

}
