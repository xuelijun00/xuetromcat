package com.yks.bigdata.exception;

/**
 * Created by zh on 2017/7/8.
 */
public class TrackHttpException extends Exception {

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public TrackHttpException(String baseMsg,Exception e) {
        super("http访问tracemore接口错误! 访问baseMsg为: "+baseMsg,e);
    }
}
