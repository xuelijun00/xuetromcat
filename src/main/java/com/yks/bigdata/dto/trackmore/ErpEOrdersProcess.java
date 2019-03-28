package com.yks.bigdata.dto.trackmore;

import java.util.Date;
/**
 * Dec：订单物流状态时间节点实体类
 * @Author
 */
public class ErpEOrdersProcess {
    /**
     * 标识
     */
    private Integer id;
    /**
     * 订单标识
     */
    private Long erpOrdersId;
    /**
     * 时间节点
     */
    private Date processTime;
    /**
     * 对应物流状态
     */
    private Integer nodeStatus;
    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getErpOrdersId() {
        return erpOrdersId;
    }

    public void setErpOrdersId(Long erpOrdersId) {
        this.erpOrdersId = erpOrdersId;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Integer getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(Integer nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}