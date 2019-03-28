package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dao.SystemExceptionMapper;
import com.yks.bigdata.dto.system.SystemException;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.dto.trackmore.TrackmoreAccount;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import com.yks.bigdata.service.trackmore.ITrackmoreAccountService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by liuxing on 2017/10/21.
 */
@Service
public class TrackmoreRegisterServiceImpl {

    private static Logger log = LoggerFactory.getLogger(TrackmoreRegisterServiceImpl.class);

    @Autowired
    SystemExceptionMapper systemExceptionMapper;

    @Autowired
    IRequestTaskService requestTaskService;

    @Autowired
    ITrackmoreAccountService trackmoreAccountService;

    @Autowired
    Tracker trackerSite;

    public void registerTrackmore() {
        systemExceptionMapper.insert(new SystemException("trackmore register data begin","" + DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")));
        //0.1获取所有任务池中的所有需要注册的requst_task数据 (request_task 表 查询条件task_status 为1 )
        List<RequestTask> registerTasks = requestTaskService.getAllNeddRegisterRequestTask();
        //注册
        if(!CollectionUtils.isEmpty(registerTasks))
            registerTraceMore40(registerTasks);

    }

    /**
     * 接口一次性只能注册40个物流单
     * @param registerTasks
     */
    private synchronized void registerTraceMore40(List<RequestTask> registerTasks){
        /*ExecutorService executorService = Executors.newFixedThreadPool(5);
        Future<Object> submit = null;*/
        List<List<RequestTask>> lists = splitTraceMore(registerTasks);
        for (List<RequestTask> list:lists) {
            try{
                TrackmoreAccount account = trackmoreAccountService.selectAvailableAccount();
                if(account == null){
                    systemExceptionMapper.insert(new SystemException("trackmore注册失败","没有可用额度账号注册"));
                    return;
                }
                String requestData = createRequestData(list);
                if(trackerSite.registerLogisticsProjectBatch(requestData, account.getApiKey())){
                    //创建成功 更新状态为2 标记是那个账号创建成功
                    requestTaskService.updateStatusRegister1(setRequestTaskAccount(list,account.getAccount()));
                    //减少额度
                    trackmoreAccountService.reduceDataCount(account.getId(),account.getDataCount() - list.size());
                    systemExceptionMapper.insert(new SystemException("trackmore注册成功","账号：" + account.getAccount() + "额度：" + (account.getDataCount() - list.size())));
                }else{
                    //创建物流项目失败 更新状态为4
                    requestTaskService.updateStatusRegisterFailed(list);
                    systemExceptionMapper.insert(new SystemException("trackmore注册失败","账号：" + account.getAccount()));
                }
                //generateFetchChannel();
            }catch (Exception ex){
                log.error("注册异常" + ex.getLocalizedMessage());
                systemExceptionMapper.insert(new SystemException("注册异常",ex.getLocalizedMessage()));
            }
        }
        /*try{submit.get();}catch (Exception ex){}
        executorService.shutdownNow();*/
    }

    /*public void processRegister(String requestData, TrackmoreAccount account, List<RequestTask> list)throws Exception{
        if(trackerSite.registerLogisticsProjectBatch(requestData, account.getApiKey())){
            //创建成功 更新状态为2 标记是那个账号创建成功
            requestTaskService.updateStatusRegister1(setRequestTaskAccount(list,account.getAccount()));
            //减少额度
            trackmoreAccountService.reduceDataCount(account.getId(),account.getDataCount() - list.size());
            systemExceptionMapper.insert(new SystemException("trackmore注册成功","账号：" + account.getAccount() + "额度：" + (account.getDataCount() - list.size())));
        }else{
            //创建物流项目失败 更新状态为4
            requestTaskService.updateStatusRegisterFailed(list);
            systemExceptionMapper.insert(new SystemException("trackmore注册失败","账号：" + account.getAccount()));
        }
    }*/

    /**
     * 切分注册数据
     * 40个账号切分一次
     * @param registerTasks
     * @return
     */
    public List<List<RequestTask>> splitTraceMore(List<RequestTask> registerTasks) {
        if (registerTasks == null || registerTasks.size() == 0) {
            return null;
        }
        ArrayList<List<RequestTask>> result = new ArrayList<List<RequestTask>>();
        ArrayList<RequestTask> ordersList = new ArrayList<RequestTask>();
        for (int i = 0; i < registerTasks.size(); i++) {
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

    /**
     * 生成 track more接口的请求json
     *
     * @param registerTasks
     * @return
     */
    private String createRequestData(List<RequestTask> registerTasks) {
        if(CollectionUtils.isEmpty(registerTasks))
            return "";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < registerTasks.size(); i++) {
            RequestTask task = registerTasks.get(i);
            sb.append("{\"tracking_number\": \"" + task.getTrackingNumber() + "\",\"carrier_code\":\"" + task.getCarrierCode() + "\"}");
            if(i != registerTasks.size() - 1)
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private List<RequestTask> setRequestTaskAccount(List<RequestTask> list, String account){
        for (RequestTask requestTask:list) {
            requestTask.setTrackAccount(account);
        }
        return list;
    }

}
