package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpError4002Exception extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public TrackHttpError4002Exception(String baseMsg) {
        super("Unauthorized - API 密钥已被删除。请确保自己的申请API key的账户与调用API的服务器同时在国内或国外。即如果你的服务器在国外，你需要翻墙登录trackingmore网站进而获取API key。baseMsg :"+baseMsg);
    }
}
