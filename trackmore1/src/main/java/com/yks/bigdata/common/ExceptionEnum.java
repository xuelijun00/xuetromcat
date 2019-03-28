package com.yks.bigdata.common;

/**
 * Created by liuxing on 2017/7/14.
 */
public enum ExceptionEnum {
    //这些是记录日志的
    ERP_ORDER_SAVE_ERROR(601,"erp_orders数据保存异常")    //erp_orders数据保存异常
    , ERP_ORDER_UPDATE_TRACKED_ERROR(603, "更新老erp-tracked字段异常") //更新老erp-tracked字段异常
    ,GENERATE_REQUEST_TASK_FAILURE(605, "生成request task失败")
    //这些是会更新erp order tracked status 状态的
    , ERP_ORDER_NOT_KEYWORD(602,"erp数据异常关键字段为空")    //erp数据异常关键字段为空
    , REQUEST_TASK_NOT_CHANNEL_CODE(604, "缺失trackmore物流渠道编码")
    ,NOT_FOUND_LOGISTICS_STATUS(607, "没有找到物流状态")

    ,TRACKMORE_NOT_NOT_MATCHED_STATUS(609, "根据关键字未匹配到状态")
    ,IMPORT_ERP_NOT_FOUND(610, "取件导入，erp不存在该订单")

    ,TRACKMORE_STATUS1(1, "上网")
    ,TRACKMORE_STATUS2(2, "封发")
    ,TRACKMORE_STATUS3(3, "交航")
    ,TRACKMORE_STATUS4(4, "落地")
    ,TRACKMORE_STATUS5(5, "妥投")
    ,TRACKMORE_STATUS10(10, "未上网")
    ,TRACKMORE_STATUS11(11, "未封发")
    ,TRACKMORE_STATUS12(12, "未交航")
    ,TRACKMORE_STATUS13(13, "未落地")
    ,TRACKMORE_STATUS14(14, "未妥投")

    ,TRACKMORE_NOT_FOUND_DATA(15, "已请求,但没有物流信息")
    ,TRACKMORE_NOT_FOUND(16, "notfound\t包裹目前查询不到")
    ,trackmore_undelivered(17,"快递员尝试过投递但失败，（这种情况下）通常会留有通知并且会再次试投")
    ,trackmore_exception(18,"包裹出现异常，发生这种情况可能是：包裹已退回寄件人，清关失败，包裹已丢失或损坏等")
    ,trackmore_expired(19,"包裹很长一段时间显示在运输途中，一直没有派送结果")

    //这些是记录日志的
    ,ERP_ORDER_SAVE_SUCCESS(801, "erp_orders数据保存成功")
    , ERP_ORDER_UPDATE_TRACKED_SUCCESS(802, "更新老erp-tracked字段成功") //更新老erp-tracked字段异常
    //这些是会更新erp order tracked status 状态的
    , GENERATE_REQUEST_TASK_SUCCESS(803, "生成request task成功")

    ;

    private int value;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private ExceptionEnum(int value,String err){
        this.value = value;
        this.message = err;
    }
}
