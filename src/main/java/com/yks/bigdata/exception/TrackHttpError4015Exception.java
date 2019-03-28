package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpError4015Exception extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public TrackHttpError4015Exception(String baseMsg) {
        super("Bad Request - 'Carrier_code' 的值是无效的。baseMsg :"+baseMsg);
    }
}
