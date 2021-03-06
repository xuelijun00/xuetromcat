package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.RequestTask;

import java.util.List;

/**
 * Created by zh on 2017/6/29.
 */
public interface IRequestTaskService {

    public void aggregation();

    /**
     * 获取所有任务池中的所有需要注册的requst_task数据 (request_task 表 查询条件task_status 为1 )
     * @return
     */
    public List<RequestTask> getAllNeddRegisterRequestTask();

    /**
     * 获取所有任务池中的所有需要查询的requst_task数据 (request_task 表 查询条件task_status 为2 )
     * @return
     */
    public List<RequestTask> getAllNeedQueryRequestTask();

    /**
     * 更新状态为 status=2
     * 表示为已注册
     * @param registerTasks
     */
    public void updateStatusRegister(List<RequestTask> registerTasks);

    void updateStatusRegister1(List<RequestTask> list);

    int updateByTrackNumSelective(RequestTask record);

    /**
     *注册失败,更新为 request_task 表 task_status = 4
     * @param registerTasks
     */
    public void updateStatusRegisterFailed(List<RequestTask> registerTasks);

    /**
     * request_task标记该条数据task_status=5 表示已经完结
     * @param id
     */
    public void updateStatusFinish(Integer id);

    /**
     * 生成request task
     * @param list
     */
    void generateRequestTask(List<ErpOrders> list);

    /**
     * 更新request task
     * @param task
     */
    void updateByPrimaryKeySelective(RequestTask task);

    RequestTask selectByErpOrderId(Long erpOrdersId);

    List<RequestTask> selectRequestTaskByPickup();

    int updateByErpOrderIdSelective(RequestTask record);

    List<RequestTask> selectRequestTasks(RequestTask record);

    Integer selectRequestTasksCount(RequestTask record);

    List<RequestTask> selectByErpOrderIdBatch(String erpOrdersId);

}
