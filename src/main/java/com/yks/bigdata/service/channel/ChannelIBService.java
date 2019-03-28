package com.yks.bigdata.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dao.TrackIBInfoMapper;
import com.yks.bigdata.dto.report.TrackNode;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.*;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.IStatusInformationService;
import com.yks.bigdata.service.trackmore.impl.CalculaImpl;
import com.yks.bigdata.service.trackmore.impl.RequestTaskServiceImpl;
import com.yks.bigdata.util.DateUtils;
import com.yks.bigdata.util.HttpRequestUtils;
import com.yks.bigdata.util.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by liuxing on 2017/9/12.
 * IB接口对接
 */
@Service
public class ChannelIBService {

    //private static final String  IBAPIURL = "http://api.myib.com/v4/tracking/events?packageId=1126829851083";
    private static String  IBAPIURL = "http://api.myib.com/v4/tracking/events?a=1%s";

    private static Map<String,String> ibHeaders = new HashMap<>();
    static {
        ibHeaders.put("Content-Type", "application/json");
        ibHeaders.put("Accept", "application/json");
        ibHeaders.put("ClientKey", "52IBkhQgVeQwt57eYomYSA==");
        ibHeaders.put("ClientSecret", "XD6gOQJDB9YZTeLSBcNpVA==");
        ibHeaders.put("MarketplaceAlias", "Ebay");
        ibHeaders.put("SellerAlias", "Youkeshu");
    }

    @Autowired
    SystemExceptionMapper systemExceptionMapper;

    @Autowired
    TrackIBInfoMapper trackIBInfoMapper;

    @Autowired
    IStatusInformationService informactionService;

    @Autowired
    IErpOrdersService erpOrdersService;

    @Autowired
    CalculaImpl calcula;

    @Value("${fetch.thread.size}")
    private int threadSize;//线程池大小

    private static final String CHANNELNAME = "IB";

    /**
     * 1、并发查询
     * 2、获取erporders中渠道是ib的 并且状态是 未完结的订单抓取数据
     * 3、每次可以查询50个物流号
     * 4、保存的数据有些字段是合并来的，数据分隔失败 \n
     * @throws Exception
     */
    public void queryIBApi() {
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        Future<Object> submit = null;
        try{
            List<LogisticsStatus> logisticsStatuses = informactionService.selectByChannel(CHANNELNAME);
            if(CollectionUtils.isEmpty(logisticsStatuses)) return;
            systemExceptionMapper.insert(new SystemException("trackmore fetch IB data begin", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
            //trackstatus字段完结=logisticsStatuses.size()，不等于的话就是未完结，就需要跟踪数据
            //查询IB渠道获取获取物流信息的记录的总数
            ErpOrders orders1 = new ErpOrders();
            orders1.setChannelName("'" + CHANNELNAME + "'");
            orders1.setTrackStatus(logisticsStatuses.size());
            orders1.setStartTime("2017-10-12");
            orders1.setEndTime("2017-11-01");
            Long aLong = erpOrdersService.selectErpOrdersChannelCount(orders1);
            long l = aLong % 50 == 0 ? aLong / 50 : aLong / 50 + 1;
            for(int i=1;i<= l;i++){
                //每次查询50条记录
                PageHelper.startPage(i, 50, false);
                List<ErpOrders> erpOrders = erpOrdersService.selectErpOrdersChannel(orders1);
                StringBuffer stringBuffer = new StringBuffer();
                for (ErpOrders orders:erpOrders) {
                    stringBuffer.append("&trackingCode=").append(orders.getOrdersMailCode());
                }
                if(StringUtils.isEmpty(stringBuffer))continue;
                final String formatURL = String.format(IBAPIURL, stringBuffer.toString());
                submit = executorService.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        sendRequestQueryIBApi(formatURL);
                        return null;
                    }
                });
                submit.get();
            }
            executorService.shutdownNow();
            systemExceptionMapper.insert(new SystemException("trackmore fetch IB data end", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        }catch (Exception ex){
            systemExceptionMapper.insert(new SystemException("trackmore fetch IB data exception" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"),ex.getMessage()));
        }
    }

    public void sendRequestQueryIBApi(String url){
        try {
            ResponseData responseData = HttpRequestUtils.sendHttpGet(url, ibHeaders);
            if (responseData.getStatusCode() != 200) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append("\n");
                sb.append("url：" + url).append("\n");
                sb.append("code:" + responseData.getStatusCode()).append("\n");
                sb.append("responseText:" + responseData.getResponseText());
                systemExceptionMapper.insert(new SystemException("IB接口查询失败", sb.toString()));
                return;
            }
            JSONArray jsonArray = JSON.parseArray(responseData.getResponseText());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!jsonObject.getBoolean("success")){//如果能查询到返回success = true
                    continue;
                }
                String trackCode = jsonObject.getString("trackingCode");
                String packageId = jsonObject.getString("packageId");
                JSONArray events = jsonObject.getJSONArray("events");
                if (events == null || events.size() <= 0) {
                    continue;
                }
                List<TrackIBInfo> list = new ArrayList<>();
                for (int j = 0; j < events.size(); j++) {
                    JSONObject jsonObject1 = events.getJSONObject(j);
                    TrackIBInfo trackIBInfo = new TrackIBInfo();
                    trackIBInfo.setTrackingCode(trackCode);
                    trackIBInfo.setPackageid(packageId);
                    trackIBInfo.setTime(DateUtils.formmatStr(jsonObject1.getJSONObject("time").getString("local"), "yyyy-MM-dd'T'HH:mm:ss"));
                    JSONObject location = jsonObject1.getJSONObject("location");
                    if(location != null){
                        trackIBInfo.setLocation(location.getString("city") + "\n" + location.getString("state") + "\n" + location.getString("zip") + "\n" + location.getString("country"));
                    }
                    trackIBInfo.setComment(jsonObject1.getString("comment"));
                    JSONObject status = jsonObject1.getJSONObject("status");
                    trackIBInfo.setStatus(status.getString("id") + "\n" + getIdStatus(status.getInteger("id")) + "\n" + status.getString("name"));
                    trackIBInfo.setCreateTime(new Date());
                    list.add(trackIBInfo);
                }
                saveIBData(trackCode, list);
                calculaData(trackCode, list);
            }
        }catch (Exception ex){
            StringBuffer sb = new StringBuffer();
            sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append("\n");
            sb.append("url：" + url).append("\n");
            sb.append("IB接口查询失败:" + ex.getLocalizedMessage()).append("\n");
            systemExceptionMapper.insert(new SystemException("IB接口解析失败", sb.toString()));
            ex.printStackTrace();
        }
    }

    @Transactional(value = "transactionManager")
    public void saveIBData(String trackCode, List<TrackIBInfo> list){
        trackIBInfoMapper.deleteByTrackingCode(trackCode);
        trackIBInfoMapper.addBatch(list);
    }

    private String getIdStatus(int id){
        String status = "";
        switch (id){
            case 1:status = "已交付";break;
            case 3:status = "留下通知";break;
            case 6:status = "拒绝/拒收";break;
            case 7:status = "已移动";break;
            case 9:status = "已丢失";break;
            case 12:status = "已转发";break;
            case 13:status = "无法按地址交货";break;
            case 15:status = "准备派送";break;
            case 16:status = "已到达邮局";break;
            case 27:status = "收到数据";break;
            case 28:status = "已在仓库收货";break;
            case 29:status = "已到达海关";break;
            case 30:status = "已清关";break;
            case 31:status = "已在原始仓库添加到货柜";break;
            case 32:status = "已扫描地点：";break;
            case 33:status = "已在仓库收到退货";break;
            case 34:status = "已退还给承运方";break;
            case 38:status = "已离开仓库";break;
            case 40:status = "已在仓库存留";break;
            case 49:status = "可以收寄";break;
            case 57:status = "已由当地部门收货";break;
            case 80:status = "航班延误";break;
            case 91:status = "供应商送货已到达";break;
            case 114:status = "AW 跟踪注释（公共通知）";break;
            case 117:status = "AW 跟踪注释（已清关）";break;
            case 118:status = "天气/自然灾害";break;
            case 120:status = "机械故障";break;
            case 121:status = "路线不当（路运/空运）";break;
            case 122:status = "安全/法规 [TSA]API 4.x 跟踪";break;
            case 123:status = "货柜损坏： 包裹无法交货";break;
            case 128:status = "转运中";break;
            case 130:status = "无法交货";break;
            case 131:status = "收到订单信息";break;
            case 132:status = "已到达机场";break;
            case 135:status = "死邮件/已由邮局处置";break;
            case 145:status = "已收寄";break;
            case 146:status = "已由代理收寄并处理";break;
            case 150:status = "途中";break;
            case 151:status = "警告";break;
            case 153:status = "已交给代理进行最终交货";break;
            case 154:status = "已在 USPS 设施接受";break;
            case 155:status = "已到达 USPS 设施";break;
            case 156:status = "已离开 USPS 设施";break;
            case 158:status = "已在 USPS 设施接受";break;
            case 159:status = "已到达国际服务中心";break;
            case 160:status = "全球派送准备就绪";break;
            case 163:status = "已安排重新交货";break;
            case 165:status = "已生成包裹退回通知";break;
            case 166:status = "提醒重新安排再次交货";break;
            case 169:status = "涉及动物的交货意外";break;
            case 170:status = "无法访问";break;
            case 171:status = "当地天气导致的延误交货意外情况";break;
            case 172:status = "已到达国际服务中心";break;
            case 175:status = "代理已交货给接收方";break;
            case 177:status = "在邮局存留";break;
            case 178:status = "已由邮局代理交货";break;
            case 179:status = "始发地航空公司已接受";break;
            case 180:status = "始发地航空公司承运方已出发/起飞";break;
            case 186:status = "交货延误";break;
            case 187:status = "正在处理因天气导致的延误意外情况API 4.x 跟踪";break;
            case 191:status = "正在等候国际航空公司的检查";break;
            case 192:status = "未通过航空公司的检查";break;
            case 193:status = "已进入美国";break;
            case 197:status = "未能清关";break;
            case 205:status = "已尝试交货";break;
            case 209:status = "联系不上客户";break;
            case 212:status = "存放在中国仓库";break;
            case 213:status = "从中国仓库发出";break;
            case 223:status = "已交给邮政服务";break;
            case 226:status = "代理已交货给商家";break;
            case 239:status = "未通过海关 - 中国";break;
            case 240:status = "被海关扣留 - 中国";break;
            case 241:status = "被海关扣留 - 美国";break;
            case 242:status = "物品已交货，但未扫描";break;
            case 245:status = "未通过航空公司的检查 - 退回发货方";break;
            default:status = "" + id;break;
        }
        return status;
    }

    /**
     * 将数据转换成jsonobject
     * @param trackIBInfos
     */
    public List<JSONObject> convert(List<TrackIBInfo> trackIBInfos){
        List<JSONObject> jsonObjects = new ArrayList<>();
        TrackNode trackNode;
        for (int i=trackIBInfos.size() - 1;i >= 0;i--){
            trackNode = new TrackNode();
            trackNode.setDate(DateFormatUtils.format(trackIBInfos.get(i).getTime(),"yyyy-MM-dd HH:mm:ss"));
            trackNode.setDetails(trackIBInfos.get(i).getStatus());
            trackNode.setStatusDescription(trackIBInfos.get(i).getLocation());
            String s = JSON.toJSONString(trackNode).replace("date","Date").replace("statusDescription","StatusDescription").replace("details","Details");
            jsonObjects.add(JSON.parseObject(s));
        }
        return jsonObjects;
    }

    public void calculaData(String trackNum, List<TrackIBInfo> list){
        ErpOrders orders = erpOrdersService.selectByOrdersMailCode(trackNum);
        List<JSONObject> jsonObjects = this.convert(list);
        List<LogisticsStatus> logisticsStatuses = informactionService.selectByChannel(CHANNELNAME);
        if(CollectionUtils.isEmpty(logisticsStatuses)) return;
        orders = calcula.processTrackmoreSource(jsonObjects, logisticsStatuses, orders, 0);
        erpOrdersService.updateErpOrders(orders);
    }

}
