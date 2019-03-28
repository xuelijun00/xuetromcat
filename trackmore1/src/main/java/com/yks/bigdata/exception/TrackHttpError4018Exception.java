package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpError4018Exception extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public TrackHttpError4018Exception(String baseMsg) {
        super("Bad Request - 由于过载风险此功能需要自定义激活。与 service@trackingmore.org 联系更多的信息。baseMsg :"+baseMsg);
    }
}
