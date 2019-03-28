package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpError4012Exception extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public TrackHttpError4012Exception(String baseMsg) {
        super("Bad Request - 请求不能理解或缺少必需的参数。baseMsg :"+baseMsg);
    }
}