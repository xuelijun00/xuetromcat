package com.yks.bigdata.dao;

import com.yks.bigdata.dto.trackmore.RequestTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Request_taskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RequestTask record);

    int insertSelective(RequestTask record);

    RequestTask selectByPrimaryKey(Integer id);
    RequestTask selectByErpOrderId(Long erpOrdersId);

    int updateByPrimaryKeySelective(RequestTask record);

    int updateByErpOrderIdSelective(RequestTask record);

    int updateByPrimaryKey(RequestTask record);

    void addBatch(List<RequestTask> tasks);

    List<RequestTask> selectRequestTaskByStatus(Integer status);

    void updateStatusRegister(List<Integer> ids);

    void updateStatusRegister1(List<RequestTask> list);

    void updateStatusRegisterFailed(List<Integer> ids);

    void updateStatusFinish(Integer id);

    List<RequestTask> selectRequestTaskByPickup(@Param("channel") String channel);

    List<RequestTask> selectRequestTasks(RequestTask record);

    Integer selectRequestTasksCount(RequestTask record);

}