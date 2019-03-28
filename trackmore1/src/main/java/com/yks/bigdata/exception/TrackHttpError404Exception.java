package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpError404Exception extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public TrackHttpError404Exception(String baseMsg) {
        super("Not Found - 找不到资源。baseMsg :"+baseMsg);
    }
}
