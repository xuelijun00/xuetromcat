package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/6/26.
 */
public class UpdateExeption extends RuntimeException {

    public UpdateExeption(String url) {
        super("elasticsearch 更新错误 可能原因是版本号不对,"+url);
    }
}
