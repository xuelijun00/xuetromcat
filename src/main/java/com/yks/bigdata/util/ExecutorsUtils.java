package com.yks.bigdata.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zh on 2017/7/4.
 * 线程池 单例
 */
public class ExecutorsUtils {

    private static ExecutorService es = null;

    public static ExecutorService  getExecutors(){
        if (es == null){
            es = Executors.newFixedThreadPool(10);
        }
        return es;
    }


}
