/*
package com.yks.bigdata.service.trackmore.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.common.LoopProcessConstant;
import com.yks.bigdata.dao.Erp_ordersMapper;
import com.yks.bigdata.dao.LogisticsChannelMapper;
import com.yks.bigdata.dao.Logistics_statusMapper;
import com.yks.bigdata.dao.TrackmoreAccountMapper;
import com.yks.bigdata.dto.trackmore.*;
import com.yks.bigdata.service.trackmore.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

*/
/**
 * Created by zh on 2017/7/5.
 * <p>
 * 循环读取数据request_task中的数据(request_task 表 查询条件task_status 为1 ),对数据做一下内容事情
 * 1.如果状态为 刚创建时 (request_task 表 查询条件task_status 为1 ),进行注册
 * 1.1 注册之后更新为request_task 表 task_status = 2; pickup表中register_status更新为1 ,新增 register_time;
 * 将请求返回的信息的json保存在track_source中
 * 1.2 如果注册失败,更新为 request_task 表 task_status = 4
 * 将请求的数据信息json保存在track_source中
 * <p>
 * 2.如果状态为已注册(request_task 表 查询条件task_status 为2 ) ,则进行查询物流信息
 * 2.1 如果查询tracemore接口返回数据 是完结的 (判断标准 返回的json串中的status为delivered或者是exception异常)
 * 2.1.1 request_task标记该条数据task_status=5 表示已经完结
 * 2.1.2 将结果写入或者更新 track_info 和 track_info_detail
 * 2.1.3 将结果写入track_source中
 * 2.1.4.依据状态表logistics_status中的状态标记,进行标记数据,
 * erp_orders字段`track_status`标记对应的值;
 * <p>
 * 2.2 如果查询tracemore接口返回数据 未完结的,则更新信息
 * 2.2.1 将结果写入或者更新 track_info 和 track_info_detail
 * 2.2.2 将结果写入track_source中
 * 2.2.3.依据状态表logistics_status中的状态标记,进行标记数据,
 * erp_orders字段`track_status`标记对应的值
 * 3.如果上述trackmore接口出现异常:
 * 暂时不考虑这个情况,以后再修改
 *//*

//@Service
public class LoopSearchServiceImpl implements LoopSearchService {

    private static Logger log = LoggerFactory.getLogger(LoopSearchServiceImpl.class);

    @Autowired
    IRequestTaskService requestTaskService;

    @Autowired
    ILogisticsPickupService pickupService;

    @Autowired
    ITrackSourceService trackSourceService;

    @Autowired
    ITrackInfoService infoService;

    @Autowired
    ITrackInfoDetailService infoDetailService;

    @Autowired
    Tracker tracker;

    @Autowired
    TrackmoreAccountMapper accountMapper;

    @Autowired
    Erp_ordersMapper ordersMapper;

    @Autowired
    LogisticsChannelMapper channelMapper;

    @Autowired
    Logistics_statusMapper statusMapper;

    //用于控制访问trace more频率,因为超过一秒访问3次以上会出现返回异常
    Long times;

    @Override
    public void loop() {

        try {
            log.info("定时loop service begin");
            //为了防止上一天的任务还没有执行完毕 但是 下一个定时器又启动了 所以做的设置
            if (!LoopProcessConstant.bo) {
                LoopProcessConstant.started();
                //0.1获取所有任务池中的所有需要注册的requst_task数据 (request_task 表 查询条件task_status 为1 )
                List<RequestTask> registerTasks = requestTaskService.getAllNeddRegisterRequestTask();
                //0.2.批量循环注册数据注册
                registerTraceMore(registerTasks);
                //0.3获取所有任务池中的所有需要查询的requst_task数据 (request_task 表 查询条件task_status 为2 )
                List<RequestTask> queryTasks = requestTaskService.getAllNeedQueryRequestTask();
                queryTraceMore(queryTasks);
                LoopProcessConstant.stoped();
            } else {
                log.error("上次循环定时任务未完成!所以这次定时任务取消");
            }

            log.info("定时loop service end");
        } catch (Exception e) {
            log.error("定时注册查询任务异常", e);
        }


    }

    */
/**
     * 批量循环注册数据注册
     *
     * @param registerTasks
     *//*

    private void registerTraceMore(List<RequestTask> registerTasks) throws Exception {
        if (registerTasks.size() == 0)
            return;
        List<List<RequestTask>> splitTasks = splitTraceMore(registerTasks);
        for (List<RequestTask> task : splitTasks) {
            try {
                registerTraceMore40(task);
            } catch (Exception e) {
                log.error("注册失败!", e);
            }
        }
    }

    */
/**
     * 切分注册数据
     * 40个账号切分一次
     * @param registerTasks
     * @return
     *//*

    private List<List<RequestTask>> splitTraceMore(List<RequestTask> registerTasks) {
        if (registerTasks == null || registerTasks.size() == 0) {
            return null;
        }
        ArrayList<List<RequestTask>> result = new ArrayList<List<RequestTask>>();
        int size = registerTasks.size();
        ArrayList<RequestTask> ordersList = new ArrayList<RequestTask>();
        for (int i = 0; i < size; i++) {
            RequestTask task = registerTasks.get(i);
            ordersList.add(task);
            //满40个使用另外一个ordersList
            if (ordersList.size() == 40) {
                result.add(ordersList);
                ordersList = new ArrayList<RequestTask>();
            }
        }
        if (ordersList.size() != 0) {
            result.add(ordersList);
        }
        return result;
    }

    */
/**
     * 100个账号一个批次 注册
     * 1.1 注册之后更新为request_task 表 task_status = 2; pickup表中register_status更新为1 ,新增 register_time;
     * 将请求返回的信息的json保存在track_source中
     * 1.2 如果注册失败,更新为 request_task 表 task_status = 4
     * 将请求的数据信息json保存在track_source中
     * <p>
     * 我累个草,trace more接口只允许一次性 提交40个,哎,心真累
     * {"meta":{"code":202,"type":"Bad Request","message":"The largest number of 40 each operation."},"data":[]}
     *
     * @param registerTasks
     *//*

    @Transactional(value = "transactionManager")
    private void registerTraceMore40(List<RequestTask> registerTasks) throws Exception {
        //由于这批task可能有多个账号,而且tackmore需要获取该账号的api,所以的进行切分
        List<SplitTaskVo> splitTaskVos = splitByAccount(registerTasks);  //?
        for (SplitTaskVo splitTaskVo : splitTaskVos) {
            String requestData = createRequestData(splitTaskVo.getTasks());

            //为了防止 api请求频率过快,导致接口报错
            if (times !=null && (System.currentTimeMillis() - times) <=334 ){
                Thread.sleep(334 - (System.currentTimeMillis() - times));
            }
            times = System.currentTimeMillis();

            String response = tracker.registerBatch(requestData, splitTaskVo.getApikey());

            List<RequestTask> sucessTask = getSucessTask(splitTaskVo.getTasks(), response);
            if (sucessTask.size() > 0) {
                //如果注册成功
                //1.1更新为request_task 表 task_status = 2
                requestTaskService.updateStatusRegister(sucessTask);
                //1.2  pickup表中register_status更新为1 ,新增 register_time;
                pickupService.updateStatusRegistered(sucessTask);
                //1.3 将请求返回的信息的json保存在track_source中
                //trackSourceService.addBatch(registerTasks,response);
            }

            List<RequestTask> failedTask = getFailedTask(splitTaskVo.getTasks(), response);
            if (failedTask.size() > 0) {
                //注册失败
                //1.1注册失败,更新为 request_task 表 task_status = 4
                requestTaskService.updateStatusRegisterFailed(failedTask);
                //1.2将请求的数据信息json保存在track_source中
                trackSourceService.addBatch(failedTask, response);
            }

        }

    }


    */
/**
     * 按照task的account进行切分为几部分
     *
     * @param registerTasks
     * @return
     *//*

    private List<SplitTaskVo> splitByAccount(List<RequestTask> registerTasks) {
        ArrayList<SplitTaskVo> vos = new ArrayList<>();
        for (RequestTask task : registerTasks) {
            SplitTaskVo spilt = querySplitTaskVoByAccount(vos, task.getTrackAccount());
            if (spilt == null) {
                SplitTaskVo splitTaskVo = new SplitTaskVo();
                splitTaskVo.setAccount(task.getTrackAccount());
                TrackmoreAccount trackmoreAccount = accountMapper.selectByAccount(task.getTrackAccount());
                splitTaskVo.setApikey(trackmoreAccount.getApiKey());
                vos.add(splitTaskVo);
            }
        }
        for (RequestTask task : registerTasks) {
            SplitTaskVo splitTaskVo = querySplitTaskVoByAccount(vos, task.getTrackAccount());
            splitTaskVo.getTasks().add(task);
        }
        return vos;
    }

    private SplitTaskVo querySplitTaskVoByAccount(List<SplitTaskVo> registerTasks, String account) {
        for (SplitTaskVo split : registerTasks) {
            if (StringUtils.equals(account, split.getAccount()))
                return split;
        }
        return null;
    }

    */
/**
     * 生成 track more接口的请求json
     *
     * @param registerTasks
     * @return
     *//*

    private String createRequestData(List<RequestTask> registerTasks) {

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < registerTasks.size(); i++) {
            RequestTask task = registerTasks.get(i);
            sb.append("{\"tracking_number\": \"" + task.getTrackingNumber() + "\",\"carrier_code\":\"" + task.getCarrierCode() + "\"}");
            if (i != (registerTasks.size() - 1))
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }


    */
/**
     * 注册成功的task
     *
     * @param tasks
     * @param response
     * @return
     *//*

    private List<RequestTask> getSucessTask(List<RequestTask> tasks, String response) {
        return getSucessTaskOrFailedTask(tasks, response, "trackings");
    }

    */
/**
     * 注册失败的task
     *
     * @param tasks
     * @param response
     * @return
     *//*

    private List<RequestTask> getFailedTask(List<RequestTask> tasks, String response) {
        return getSucessTaskOrFailedTask(tasks, response, "errors");
    }

    private List<RequestTask> getSucessTaskOrFailedTask(List<RequestTask> tasks, String response, String nodeName) {
        ArrayList<RequestTask> requestTasks = new ArrayList<>();
        JSONObject rootObject = JSON.parseObject(response);
        JSONArray trackings = rootObject.getJSONObject("data").getJSONArray(nodeName);
        if (trackings == null)
            return requestTasks;
        for (int i = 0; i < trackings.size(); i++) {
            JSONObject track = trackings.getJSONObject(i);
            String trackingNumber = track.getString("tracking_number");
            RequestTask task = queryByTrackingNumber(tasks, trackingNumber);
            requestTasks.add(task);
        }
        return requestTasks;
    }


    private RequestTask queryByTrackingNumber(List<RequestTask> tasks, String trackingNumber) {
        for (RequestTask task : tasks) {
            if (StringUtils.equals(task.getTrackingNumber(), trackingNumber)) {
                return task;
            }
        }
        return null;
    }

    */
/**
     * 使用tracemore接口进行查询
     * *  2.如果状态为已注册(request_task 表 查询条件task_status 为2 ) ,则进行查询物流信息
     * 2.1 如果查询tracemore接口返回数据 是完结的 (判断标准 返回的json串中的status为delivered或者是exception异常)
     * 2.1.1 request_task标记该条数据task_status=5 表示已经完结
     * 2.1.2 将结果写入或者更新 track_info 和 track_info_detail
     * 2.1.3 将结果写入track_source中
     * 2.1.4.依据状态表logistics_status中的状态标记,进行标记数据,
     * erp_orders字段`track_status`标记对应的值;
     * <p>
     * 2.2 如果查询tracemore接口返回数据 未完结的,则更新信息
     * 2.2.1 将结果写入或者更新 track_info 和 track_info_detail
     * 2.2.2 将结果写入track_source中
     * 2.2.3.依据状态表logistics_status中的状态标记,进行标记数据,
     * erp_orders字段`track_status`标记对应的值
     *
     * @param queryTasks
     *//*

    private void queryTraceMore(List<RequestTask> queryTasks) {
        if (queryTasks.size() == 0)
            return;
        for (RequestTask task : queryTasks) {
            try {
                if (times !=null && (System.currentTimeMillis() - times) <=334 ){
                    Thread.sleep(334 - (System.currentTimeMillis() - times));
                }
                times = System.currentTimeMillis();
                String requestData = "{\"tracking_number\": \"" + task.getTrackingNumber() + "\",\"carrier_code\":\"" + task.getCarrierCode() + "\"}";
                TrackmoreAccount trackmoreAccount = accountMapper.selectByAccount(task.getTrackAccount());
                String response = tracker.queryInfo(requestData, trackmoreAccount.getApiKey());
                if (isFinish(response)) {
                    processFinish(task, response);
                } else {
                    processUnFinish(task, response);
                }
            } catch (Exception e) {
                log.error("requesttask task id为 " + task.getId() + " query track more接口出现异常", e);
            }
        }
    }

    @Transactional(value = "transactionManager")
    private void processUnFinish(RequestTask task, String response) {
        //未完结
        //2.2.1 将结果写入或者更新 track_info 和 track_info_detail
        infoService.saveOrUpdateInfoAndDetail(task, response);
        //2.2.2 将结果写入track_source中
        trackSourceService.add(task, response);
        //2.2.3.依据状态表logistics_status中的状态标记,进行标记数据,erp_orders字段`track_status`标记对应的值
        signTrackStatusOnErpOrders(task, response);
    }

    @Transactional(value = "transactionManager")
    private void processFinish(RequestTask task, String response) {
        //已完结
        //2.1.1 request_task标记该条数据task_status=5 表示已经完结
        requestTaskService.updateStatusFinish(task);
        //2.1.2 将结果写入或者更新 track_info 和 track_info_detail
        infoService.saveOrUpdateInfoAndDetail(task, response);
        //2.1.3 将结果写入track_source中
        trackSourceService.add(task, response);
        //2.1.4.依据状态表logistics_status中的状态标记,进行标记数据,erp_orders字段`track_status`标记对应的值;
        signTrackStatusOnErpOrders(task, response);
    }

    */
/**
     * 依据状态表logistics_status中的状态标记,进行标记数据,erp_orders字段`track_status`标记对应的值;
     *//*

    private void signTrackStatusOnErpOrders(RequestTask task, String response) {
        List<LogisticsStatus> statuess = getChannelStatusFromReqestTask(task);
        if (statuess == null)
            return;
        Integer state = null;
        //LogisticsStatus按照 sort字段进行排序 sort字段越大的排在第一位
        Collections.sort(statuess);
        for (LogisticsStatus status : statuess) {
            if (response.contains(status.getKeyword())) {
                state = status.getLogisticsStatus();
                break;
            }
        }

        if (state == null) {
            log.info("该request task 没有查找到合适的状态 (上网 交航等) : request task id为:" + task.getId() + "\n 返回的json串为:" + response);
            return;
        }

        try {
            judeStateException(state, task, response);
        } catch (Exception e) {
            log.error("异常", e);
        }

        ErpOrders erpOrders = new ErpOrders();
        erpOrders.setErpOrdersId(task.getErpOrdersId());
        erpOrders.setTrackStatus(state);
        ordersMapper.updateByPrimaryKeySelective(erpOrders);

    }

    */
/**
     * 判断订单异常
     * 标准 :
     * state=10 未上网 判断response 是否含有Pickup shipment checked in at时间为 48
     * state=11 未封发 判断response 是否含有Shipment designated to时间为 72
     * state=12 未交航 判断response 是否含有Shipment arrived at airport时间为 96
     * state=13 未到目的国 判断response 是否含有Custom cleared and arrived at时间为 120
     * state=14 未妥投 判断response 是否含有DELIVERE DSHIPMENT DELIVERED时间为 144
     *
     * @param state
     * @param task
     * @param response
     *//*

    private void judeStateException(Integer state, RequestTask task, String response) throws Exception {
        ErpOrders erpOrders = ordersMapper.selectByPrimaryKey(task.getErpOrdersId());
        Long minus = System.currentTimeMillis() - erpOrders.getOrdersExportTime().getTime();
        if (state == 1) {
            //state=10 未上网 判断response 是否含有Pickup shipment checked in at时间为 48
            if (response.contains("Pickup shipment checked in at") && minus >= 48 * 3600 * 1000) {
                state = 10;
            }
        } else if (state == 2) {
            //state=11 未封发 判断response 是否含有Shipment designated to时间为 72
            if (response.contains("Shipment designated to") && minus >= 72 * 3600 * 1000) {
                state = 11;
            }
        } else if (state == 3) {
            //state=12 未交航 判断response 是否含有Shipment arrived at airport时间为 96
            if (response.contains("Shipment arrived at airport") && minus >= 96 * 3600 * 1000) {
                state = 12;
            }
        } else if (state == 4) {
            //state=13 未到目的国 判断response 是否含有Custom cleared and arrived at时间为 120
            if (response.contains("Custom cleared and arrived at") && minus >= 120 * 3600 * 1000) {
                state = 13;
            }
        } else if (state == 5) {
            //state=14 未妥投 判断response 是否含有DELIVERE DSHIPMENT DELIVERED时间为 144
            if (response.contains("DELIVERE DSHIPMENT DELIVERED") && minus >= 144 * 3600 * 1000) {
                state = 14;
            }
        }
    }

    private List<LogisticsStatus> getChannelStatusFromReqestTask(RequestTask task) {
//        ErpOrders erpOrders = ordersMapper.selectByPrimaryKey(task.getErpOrdersId());
//        LogisticsChannel filterChannel = new LogisticsChannel();
//        filterChannel.setYksChannelCode(erpOrders.getFreightcode());
//        List<LogisticsChannel> channelList = channelMapper.selectLogisticsChannel(filterChannel);
        LogisticsChannel channel = channelMapper.selectByPrimaryKey(task.getChannelId());
        if (channel == null) {
            log.error("在 物流渠道 LogisticsChannel 不存在 channel id为"+ task.getChannelId());
            return null;
        }
        LogisticsStatus status = new LogisticsStatus();
        status.setChannel(channel.getShortCode());
        return statusMapper.selectLogisticsStatus(status);
    }

    */
/**
     * 判断是否完结
     * 判断标准 返回的json串中的status为delivered或者是exception异常
     *
     * @param response
     * @return
     *//*

    private boolean isFinish(String response) {
        JSONObject rootObject = JSON.parseObject(response);
        JSONArray items = rootObject.getJSONObject("data").getJSONArray("items");
        String status = items.getJSONObject(0).getString("status");
        if (StringUtils.equals(status, "delivered") || StringUtils.equals(status, "exception")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void registerTrackmore() {
        //TODO:现在使用得是new实现
    }

    @Override
    public void queryTrackmore() {
        //TODO:现在使用得是new实现
    }
}


class SplitTaskVo {
    String account;
    String apikey;
    List<RequestTask> tasks = new ArrayList<RequestTask>();

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public List<RequestTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<RequestTask> tasks) {
        this.tasks = tasks;
    }
}
*/
