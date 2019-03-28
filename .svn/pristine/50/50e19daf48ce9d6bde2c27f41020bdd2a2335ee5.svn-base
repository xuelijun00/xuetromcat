package com.yks.bigdata.web.trackmore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import com.yks.bigdata.service.trackmore.LoopSearchService;
import com.yks.bigdata.service.trackmore.impl.Tracker;
import com.yks.bigdata.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxing on 2017/8/27.
 */
@Controller
@RequestMapping("/api")
public class ReceiveTrackmorePushController {

    @Autowired
    private SystemExceptionMapper systemExceptionMapper;

    @Autowired
    IRequestTaskService requestTaskService;

    @Autowired
    LoopSearchService loopSearchService;

    @Autowired
    Tracker tracker;

    /**
     * {
     "meta":{
     "code":200,
     "type":"Success",
     "message":"Success"
     },
     "data":{
     "id":"67c89417ca1c2252b0f300e5167199dc",
     "tracking_number":"85540892874",
     "carrier_code":"china-post",
     "status":"transit",
     "created_at":"2017-08-25T19:40:26+08:00",
     "updated_at":"2017-08-27T14:55:28+08:00",
     "title":"",
     "order_id":null,
     "customer_name":null,
     "customer_email":"",
     "original_country":"China",
     "itemTimeLength":4,
     "origin_info":{
     "weblink":"http://intmail.183.com.cn",
     "phone":null,
     "carrier_code":"china-post",
     "trackinfo":[
     {
     "Date":"2017-08-26 17:45",
     "StatusDescription":"离开【中国邮政集团公司苏州市大宗邮件处理中心】，下一站【苏州中心】",
     "Details":"CN"
     },
     {
     "Date":"2017-08-26 16:16",
     "StatusDescription":"【中国邮政集团公司苏州市大宗邮件处理中心】已封发",
     "Details":"CN"
     },
     {
     "Date":"2017-08-26 10:36",
     "StatusDescription":"【中国邮政集团公司苏州市大宗邮件处理中心】已收寄",
     "Details":"CN"
     },
     {
     "Date":"2017-08-23 23:06",
     "StatusDescription":"电子信息已收到",
     "Details":"CN"
     }
     ]
     },
     "destination_info":{
     "weblink":null,
     "phone":null,
     "carrier_code":null,
     "trackinfo":null
     },
     "lastUpdateTime":"2017-08-26 17:45",
     "lastEvent":"离开【中国邮政集团公司苏州市大宗邮件处理中心】，下一站【苏州中心】,CN,2017-08-26 17:45"
     },
     "verifyInfo":{
     "timeStr":1503816929,
     "signature":"c37507b88f611c8e3cd82df9752ad43186975468aef2b4f8088c950fb7fdc9e9"
     }
     }
     * @param request
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/receive_push",method = RequestMethod.POST)
    public void receivePush(HttpServletRequest request){
        try{
            StringBuilder content = new StringBuilder();
            ServletInputStream inputStream = request.getInputStream();
            byte[] bytes = new byte[1024];
            int lens;
            while ((lens = inputStream.read(bytes)) > 0) {
                content.append(new String(bytes, 0, lens));
            }
            String strcont = content.toString();
            if(!tracker.judgeException("trackpush", "", strcont)){
                return;
            }
            JSONObject rootObject = JSON.parseObject(strcont);
            String tracking_number = rootObject.getJSONObject("data").getString("tracking_number");
            RequestTask requestTask = new RequestTask();
            requestTask.setTrackingNumber(tracking_number);
            List<RequestTask> list = requestTaskService.selectRequestTasks(requestTask);
            if(CollectionUtils.isEmpty(list)){
                return;
            }
            requestTask = list.get(0);
            if(!verification(rootObject, requestTask.getTrackAccount())){//秘钥不匹配
                return;
            }
            loopSearchService.processTrackmoreJson(rootObject.getJSONObject("data"), requestTask);

        }catch (Exception ex){
            SystemException systemException = new SystemException();
            systemException.setType("push error");
            systemException.setErrContext(ex.getLocalizedMessage());
            systemException.setCreateTime(new Date());
            systemExceptionMapper.insert(systemException);
        }
    }

    private boolean verification(JSONObject rootObject, String account){
        JSONObject verifyInfo = rootObject.getJSONObject("verifyInfo");
        String timeStr = verifyInfo.getString("timeStr");
        String signature = verifyInfo.getString("signature");
        String encrypt = MD5Util.Encrypt(timeStr, account);
        return signature.equals(encrypt);
    }

}
