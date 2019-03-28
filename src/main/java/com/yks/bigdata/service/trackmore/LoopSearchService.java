package com.yks.bigdata.service.trackmore;

import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.dto.trackmore.RequestTask;

/**
 * Created by zh on 2017/7/5.
 *
 * 循环读取数据request_task中的数据(request_task 表 查询条件task_status 为1 ),对数据做一下内容事情
 *  1.如果状态为 刚创建时 (request_task 表 查询条件task_status 为1 ),进行注册
 *      1.1 注册之后更新为request_task 表 task_status = 2; pickup表中register_status更新为1 ,新增 register_time;
 *          将请求返回的信息的json保存在track_source中
 *      1.2 如果注册失败,更新为 request_task 表 task_status = 4
 *          将请求的数据信息json保存在track_source中
 *
 *  2.如果状态为已注册(request_task 表 查询条件task_status 为2 ) ,则进行查询物流信息
 *      2.1 如果查询tracemore接口返回数据 是完结的 (判断标准 返回的json串中的status为delivered或者是exception异常)
 *          2.1.1 request_task标记该条数据task_status=5 表示已经完结
 *          2.1.2 将结果写入或者更新 track_info 和 track_info_detail
 *          2.1.3 将结果写入track_source中
 *          2.1.4.依据状态表logistics_status中的状态标记,进行标记数据,
 *                  erp_orders字段`track_status`标记对应的值;
 *
 *      2.2 如果查询tracemore接口返回数据 未完结的,则更新信息
 *          2.2.1 将结果写入或者更新 track_info 和 track_info_detail
 *          2.2.2 将结果写入track_source中
 *          2.2.3.依据状态表logistics_status中的状态标记,进行标记数据,
 *                  erp_orders字段`track_status`标记对应的值
 *  3.如果上述trackmore接口出现异常:
 *      暂时不考虑这个情况,以后再修改
 *
 */
public interface LoopSearchService {

    void queryTrackmore();

    void processTrackmoreJson(JSONObject item, RequestTask task)  throws Exception;

}
