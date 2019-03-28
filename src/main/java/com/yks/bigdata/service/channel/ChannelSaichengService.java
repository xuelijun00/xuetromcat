package com.yks.bigdata.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dao.TrackSaiChengInfoMapper;
import com.yks.bigdata.dto.report.TrackNode;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticsStatus;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackSaiChengInfo;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.IStatusInformationService;
import com.yks.bigdata.service.trackmore.impl.CalculaImpl;
import com.yks.bigdata.service.trackmore.impl.RequestTaskServiceImpl;
import com.yks.bigdata.util.DateUtils;
import com.yks.bigdata.util.HttpRequestUtils;
import com.yks.bigdata.util.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by liuxing on 2017/10/22.
 * 对接赛诚物流渠道
 */
@Service
public class ChannelSaichengService {

    //userId=YKS or userId=YKSEP
    private static final String URL = "http://114.80.227.2:8080/api/track/TrackEvents?tracktype=2&userId=YKS&findNumber=%s";
    private static Logger log = LoggerFactory.getLogger(ChannelSaichengService.class);

    @Autowired
    SystemExceptionMapper systemExceptionMapper;

    @Autowired
    TrackSaiChengInfoMapper trackSaiChengInfoMapper;

    @Autowired
    IErpOrdersService erpOrdersService;

    private static final String YYYYMMDDTHHMMSS1000 = "yyyy-MM-dd'T'HH:mm:ss+10:00";
    private static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYYMMDD = "yyyy-MM-dd";
    private static final String CHANNELNAME = "'赛诚中澳专线挂号','赛诚中澳专线平邮'";

    @Value("${fetch.thread.size}")
    private int threadSize;//线程池大小

    public void querySaicheng(){
        /*ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        Future<Object> submit = null;*/
        systemExceptionMapper.insert(new SystemException("trackmore fetch saicheng data begin", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        List<LogisticsStatus> logisticsStatuses = informactionService.selectByChannel("赛诚中澳专线挂号");
        /*禁用的渠道状态是604，获取这些指定渠道的数据 != 状态size就是需要去抓取的*/
        ErpOrders orders = new ErpOrders();
        orders.setChannelName("'" + CHANNELNAME + "'");
        orders.setTrackStatus(logisticsStatuses.size());
        Long aLong = erpOrdersService.selectErpOrdersChannelCount(orders);
        long count = aLong%10==0?aLong/10:aLong/10 + 1;
        for(int i=1;i<=count;i++){
            /*每次查询10条记录，赛诚api限制的了记录数*/
            PageHelper.startPage(i, 10, false);
            List<ErpOrders> erpOrders1 = erpOrdersService.selectErpOrdersChannel(orders);
            if(CollectionUtils.isEmpty(erpOrders1)){
                continue;
            }
            Map<String, ErpOrders> map = new HashMap<>();
            StringBuffer stringBuffer = new StringBuffer();
            for (ErpOrders orders1:erpOrders1) {
                stringBuffer.append(orders1.getOrdersMailCode()).append(",");
                map.put(orders1.getOrdersMailCode(),orders1);
            }
            final String url = String.format(URL,stringBuffer.deleteCharAt(stringBuffer.length() - 1));
            sendRequestQuerySaichengApi(url, map);//每次批量查询10条
            /*submit = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    sendRequestQuerySaichengApi(url);
                    return null;
                }
            });*/
        }
        /*try{submit.get();}catch (Exception ex){}
        executorService.shutdownNow();*/
        systemExceptionMapper.insert(new SystemException("trackmore fetch saicheng data end", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }

    public void sendRequestQuerySaichengApi(String url, Map<String, ErpOrders> map){
        try {
            ResponseData responseData = HttpRequestUtils.sendHttpGet(url);
            Map<String, List<TrackSaiChengInfo>> stringListMap = this.parseXml(responseData.getResponseText());
            for (Map.Entry<String, List<TrackSaiChengInfo>> entries:stringListMap.entrySet()) {
                this.saveYTData(entries.getKey(), entries.getValue());
                this.calculaData(entries.getValue(), map.get(entries.getKey()));
            }
        }catch (Exception ex){
            saveErrorLog(url, ex);
            ex.printStackTrace();
        }
    }
    public void sendRequestQuerySaichengApi(String url){
        try {
            ResponseData responseData = HttpRequestUtils.sendHttpGet(url);
            Map<String, List<TrackSaiChengInfo>> stringListMap = this.parseXml(responseData.getResponseText());
            for (Map.Entry<String, List<TrackSaiChengInfo>> entries:stringListMap.entrySet()) {
                saveYTData(entries.getKey(), entries.getValue());
            }
        }catch (Exception ex){
            saveErrorLog(url, ex);
            ex.printStackTrace();
        }
    }

    private Map<String,List<TrackSaiChengInfo>> parseXml(String xml)throws Exception{
        Map<String,List<TrackSaiChengInfo>> map = new HashMap<>();
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        Iterator<Element> iterator = rootElement.elementIterator();
        while (iterator.hasNext()){
            Element next = iterator.next();
            if("orders".equals(next.getName())){
                Iterator<Element> iterator1 = next.elementIterator();
                while (iterator1.hasNext()){
                    Element next2 = iterator1.next();
                    if("order".equals(next2.getName())){
                        List<TrackSaiChengInfo> saiChengInfos = parseXmlValue(next2);
                        if(!CollectionUtils.isEmpty(saiChengInfos)){
                            map.put(saiChengInfos.get(0).getTrackingNumber(), parseXmlValue(next2));
                        }
                    }
                }
            }
        }
        return map;
    }

    private List<TrackSaiChengInfo> parseXmlValue(Element element){
        List<TrackSaiChengInfo> saiChengInfos = new ArrayList<>();
        Iterator<Element> iterator = element.elementIterator();
        String orderNo = "";
        String trackingNumber = "";
        String status = "";
        String productType = "";
        while (iterator.hasNext()){
            Element next = iterator.next();
            if(next.isTextOnly() && "orderNo".equals(next.getName())){
                orderNo = next.getText();
            }else if(next.isTextOnly() && "trackingNumber".equals(next.getName())){
                trackingNumber = next.getText();
            }else if(next.isTextOnly() && "status".equals(next.getName())){
                status = next.getText();
            }else if(next.isTextOnly() && "productType".equals(next.getName())){
                productType = next.getText();
            }else if("events".equals(next.getName())){
                Iterator<Element> iterator1 = next.elementIterator();
                while (iterator1.hasNext()){
                    Element next1 = iterator1.next();
                    if("event".equals(next1.getName())){
                        Iterator<Element> iterator2 = next1.elementIterator();
                        TrackSaiChengInfo trackSaiChengInfo = new TrackSaiChengInfo();
                        trackSaiChengInfo.setCreateTime(new Date());
                        trackSaiChengInfo.setOrderno(orderNo);
                        trackSaiChengInfo.setTrackingNumber(trackingNumber);
                        trackSaiChengInfo.setStatus(status);
                        trackSaiChengInfo.setProductType(productType);
                        while (iterator2.hasNext()){
                            Element next2 = iterator2.next();
                            if(next2.isTextOnly() && "date".equals(next2.getName())){
                                Date date = null;
                                try{date = DateUtils.formmatStr(next2.getText(), YYYYMMDDTHHMMSS1000);}catch (Exception ex){
                                    try{date = DateUtils.formmatStr(next2.getText(), YYYYMMDDHHMMSS);}catch (Exception ex1){
                                        try{date = DateUtils.formmatStr(next2.getText(), YYYYMMDD);}catch (Exception ex2){
                                            saveErrorLog(next2.getText(), ex2);
                                            log.error(next2.getText() + "时间转换错误：" + ex1.getLocalizedMessage());
                                        }
                                    }
                                }
                                trackSaiChengInfo.setDate(date);
                            }else if(next2.isTextOnly() && "description".equals(next2.getName())){
                                trackSaiChengInfo.setDescription(next2.getText());
                            }else if(next2.isTextOnly() && "location".equals(next2.getName())){
                                trackSaiChengInfo.setLocation(next2.getText());
                            }
                        }
                        saiChengInfos.add(trackSaiChengInfo);
                    }
                }
            }
        }
        return saiChengInfos;
    }

    /**
     * 增加错误日志
     * @param url
     * @param ex
     */
    private void saveErrorLog(String url, Exception ex){
        StringBuffer sb = new StringBuffer();
        sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        sb.append("url：").append(url).append("\n");
        sb.append("赛诚接口查询失败:").append(ex.getLocalizedMessage()).append("\n");
        systemExceptionMapper.insert(new SystemException("赛诚接口解析失败", sb.toString()));
    }
    /**
     * 保存数据
     * @param trackCode
     * @param list
     */
    @Transactional(value = "transactionManager")
    public void saveYTData(String trackCode, List<TrackSaiChengInfo> list){
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        trackSaiChengInfoMapper.deleteByTrackNum(trackCode);
        trackSaiChengInfoMapper.addBatch(list);
    }

    @Autowired
    CalculaImpl calcula;

    @Autowired
    IStatusInformationService informactionService;

    public void calculaData(List<TrackSaiChengInfo> list, ErpOrders erpOrders){
        List<JSONObject> jsonObjects = this.convert(list);
        List<LogisticsStatus> logisticsStatuses = informactionService.selectByChannel("赛诚中澳专线挂号");
        if(CollectionUtils.isEmpty(logisticsStatuses)) return;
        ErpOrders orders = calcula.processTrackmoreSource(jsonObjects, logisticsStatuses, erpOrders, 0);
        erpOrdersService.updateErpOrders(orders);
    }

    /**
     * 将数据转换成jsonobject
     * @param saiChengInfos
     */
    public List<JSONObject> convert(List<TrackSaiChengInfo> saiChengInfos){
        List<JSONObject> jsonObjects = new ArrayList<>();
        TrackNode trackNode;
        for (int i=saiChengInfos.size() - 1;i >= 0;i--){
            trackNode = new TrackNode();
            trackNode.setDate(DateFormatUtils.format(saiChengInfos.get(i).getDate(),"yyyy-MM-dd HH:mm:ss"));
            trackNode.setDetails(saiChengInfos.get(i).getDescription());
            trackNode.setStatusDescription(saiChengInfos.get(i).getLocation());
            String s = JSON.toJSONString(trackNode).replace("date","Date").replace("statusDescription","StatusDescription").replace("details","Details");
            jsonObjects.add(JSON.parseObject(s));
        }
        return jsonObjects;
    }


}
