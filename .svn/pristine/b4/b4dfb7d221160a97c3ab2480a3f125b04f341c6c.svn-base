package com.yks.bigdata.service.channel;

import com.github.pagehelper.PageHelper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dao.TrackYtInfoMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackYtInfo;
import com.yks.bigdata.service.trackmore.impl.RequestTaskServiceImpl;
import com.yks.bigdata.util.DateUtils;
import com.yks.bigdata.util.HttpRequestUtils;
import com.yks.bigdata.util.MD5Util;
import com.yks.bigdata.util.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
 * Created by liuxing on 2017/9/22.
 * 对接圆通速递
 */
@Service
public class ChannelYTService {

    private static final String URL = "http://edi.ytoglobal.com/outerEdiTrack/searchEdiTrack";
    private static final String CLIENT_ID = "YKS";

    @Autowired
    SystemExceptionMapper systemExceptionMapper;

    @Autowired
    RequestTaskServiceImpl requestTaskService;

    @Autowired
    TrackYtInfoMapper trackYtInfoMapper;

    @Value("${fetch.thread.size}")
    private int threadSize;//线程池大小

    public void queryYT(){
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        Future<Object> submit = null;
        systemExceptionMapper.insert(new SystemException("trackmore fetch LWE data begin", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        RequestTask requestTask = new RequestTask();
        requestTask.setChannel("'圆通速递-宅配','圆通速递-店配'");
        requestTask.setTaskStatus(2);
        Integer integer = requestTaskService.selectRequestTasksCount(requestTask);
        int count = integer%50==0?integer/50:integer/50 + 1;
        for(int i=1;i<=count;i++){
            PageHelper.startPage(i, 50, false);
            List<RequestTask> queryTasks = requestTaskService.selectRequestTasks(requestTask);
            if(CollectionUtils.isEmpty(queryTasks)){
                continue;
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (RequestTask task:queryTasks) {
                stringBuffer.append(task.getTrackingNumber()).append(",");
            }
            final String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<Message>\n" +
                    "    <Header>\n" +
                    "\t    <SeqNo>2016080310151afdsa</SeqNo>\n" +
                    "        <TimeStamp>2016-08-03 10:17:00</TimeStamp>\n" +
                    "\t</Header>\n" +
                    "\t<WaybillNos>\n" +
                    "\t    <WaybillNo>"+stringBuffer+"</WaybillNo>\n" +
                    "\t\t<Language>zh_CN</Language>\n" +
                    "\t</WaybillNos>\n" +
                    "</Message>";
            final String trackNums = stringBuffer.toString();
            submit = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    sendRequestQueryYTApi(xml, trackNums);
                    return null;
                }
            });
        }
        try{submit.get();}catch (Exception ex){}
        executorService.shutdownNow();
        systemExceptionMapper.insert(new SystemException("trackmore fetch LWE data end", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }

    public void sendRequestQueryYTApi(String message, String trackNums){
        try {
            Map<String,String> map = new HashMap<>();
            map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String,String> params = new HashMap<>();
            params.put("message",message);
            params.put("sign", Base64.encode(MD5Util.encode(message+CLIENT_ID).getBytes()));
            params.put("client_id", CLIENT_ID);
            ResponseData responseData = HttpRequestUtils.sendHttpPost(URL, params, map);
            Document document = DocumentHelper.parseText(responseData.getResponseText());
            List<TrackYtInfo> trackYtInfos = this.parseXml(document.getRootElement());
            Map<String, List<TrackYtInfo>> group = this.group(trackYtInfos, trackNums);
            for (Map.Entry<String, List<TrackYtInfo>> entries:group.entrySet()) {
                saveYTData(entries.getKey(), entries.getValue());
            }
        }catch (Exception ex){
            StringBuffer sb = new StringBuffer();
            sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append("\n");
            sb.append("url：" + message).append("\n");
            sb.append("圆通接口查询失败:" + ex.getLocalizedMessage()).append("\n");
            systemExceptionMapper.insert(new SystemException("圆通接口解析失败", sb.toString()));
            ex.printStackTrace();
        }
    }

    /**
     * 保存数据
     * @param trackCode
     * @param list
     */
    @Transactional(value = "transactionManager")
    public void saveYTData(String trackCode, List<TrackYtInfo> list){
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        trackYtInfoMapper.deleteByTrackNum(trackCode);
        trackYtInfoMapper.addBatch(list);
    }

    /**
     * 解析xml
     * @param element
     * @return
     * @throws Exception
     */
    public List<TrackYtInfo> parseXml(Element element)throws Exception{
        List<TrackYtInfo> trackYtInfos = new ArrayList<>();
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()){
            Element next = iterator.next();
            if("Trackings".equals(next.getName())){
                Iterator<Element> iterator1 = next.elementIterator();
                while (iterator1.hasNext()){
                    Element next2 = iterator1.next();
                    if("Tracking".equals(next2.getName())){
                        trackYtInfos.add(parseXmlValue(next2));
                    }
                }
            }
        }
        return trackYtInfos;
    }

    /**
     * 获取value
     * @param element
     * @return
     * @throws Exception
     */
    public TrackYtInfo parseXmlValue(Element element)throws Exception{
        TrackYtInfo trackYtInfo = new TrackYtInfo();
        trackYtInfo.setCreateTime(new Date());
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()){
            Element next = iterator.next();
            if(next.isTextOnly() && "WaybillNo".equals(next.getName())){
                trackYtInfo.setWaybillno(next.getText());
            }else if(next.isTextOnly() && "ReferenceOrderNo".equals(next.getName())){
                trackYtInfo.setReferenceorderno(next.getText());
            }else if(next.isTextOnly() && "ReferenceChangeNo".equals(next.getName())){
                trackYtInfo.setReferencechangeno(next.getText());
            }else if(next.isTextOnly() && "EventCode".equals(next.getName())){
                trackYtInfo.setEventcode(next.getText());
            }else if(next.isTextOnly() && "EventDetail".equals(next.getName())){
                trackYtInfo.setEventdetail(next.getText());
            }else if(next.isTextOnly() && "EventOperater".equals(next.getName())){
                trackYtInfo.setEventoperater(next.getText());
            }else if(next.isTextOnly() && "EventOperaterCode".equals(next.getName())){
                trackYtInfo.setEventoperatercode(next.getText());
            }else if(next.isTextOnly() && "EventLocationCode".equals(next.getName())){
                trackYtInfo.setEventlocationcode(next.getText());
            }else if(next.isTextOnly() && "EventLocation".equals(next.getName())){
                trackYtInfo.setEventlocation(next.getText());
            }else if(next.isTextOnly() && "EventTime".equals(next.getName())){
                trackYtInfo.setEventtime(DateUtils.formmatStr(next.getText(),"yyyy-MM-dd HH:mm:ss"));
            }else if(next.isTextOnly() && "ServiceCode".equals(next.getName())){
                trackYtInfo.setServicecode(next.getText());
            }else if(next.isTextOnly() && "OriginalTracking".equals(next.getName())){
                trackYtInfo.setOriginaltracking(next.getText());
            }else if(next.isTextOnly() && "VisibleSign".equals(next.getName())){
                trackYtInfo.setVisiblesign(next.getText());
            }
        }
        return trackYtInfo;
    }

    /**
     * 分组，因为没有主见，所以每次会删除相同物流号的记录，在重新插入
     * 而获取的数据没有分组，数据是乱的
     * @param trackYtInfos
     * @param trackNum
     * @return
     */
    public Map<String,List<TrackYtInfo>> group(List<TrackYtInfo> trackYtInfos, String trackNum){
        Map<String,List<TrackYtInfo>> map = new HashMap<>();
        String[] split = trackNum.split(",");
        for(int i=0;i<split.length;i++){
            String s = split[i];
            List<TrackYtInfo> list = new ArrayList<>();
            for (TrackYtInfo trackYtInfo:trackYtInfos) {
                if(s.equals(trackYtInfo.getWaybillno())){
                    list.add(trackYtInfo);
                }
            }
            map.put(s, list);
        }
        return map;
    }

    private String getCode(String code){
        if("AF".equals(code)){
            code = "快件到达处理中心";
        }else if("CCSC".equals(code)){
            code = "海关扣件-快件被海关没收";
        }else if("CP".equals(code)){
            code = "快件正在清关";
        }else if("CR".equals(code)){
            code = "海关退关";
        }else if("DS".equals(code)){
            code = "已经安排派送";
        }else if("HC".equals(code)){
            code = "海关扣件";
        }else if("IGND".equals(code)){
            code = "未到货";
        }else if("ND".equals(code)){
            code = "配送中";
        }else if("OKDS".equals(code)){
            code = "配送完成";
        }else if("RC".equals(code)){
            code = "清关完成";
        }else if("SH".equals(code)){
            code = "快件暂扣";
        }
        return code;
    }

}
