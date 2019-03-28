package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpError4021Exception extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public TrackHttpError4021Exception(String baseMsg) {
        super("Bad Request - 你的余额不够,所以你不能调用API请求数据。baseMsg :"+baseMsg);
    }
}
