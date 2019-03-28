package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpError4013Exception extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public TrackHttpError4013Exception(String baseMsg) {
        super("Bad Request - Tracking_number 是必需的。baseMsg :"+baseMsg);
    }
}
