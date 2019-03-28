package com.yks.bigdata.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zh on 2017/7/4.
 */
public class A {


    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(2);
        Collection task = new ArrayList();
        task.add(new TwoThread());
        task.add(new OneThread());
        List list = es.invokeAll(task);
        Future<String> f1 = (Future<String>) list.get(0);
        Future<String> f2 = (Future<String>) list.get(1);
        System.out.println(f1.get());
        System.out.println(f2.get());
    }



}

class TwoThread implements Callable<String>{

    @Override
    public String call() throws Exception {
        Thread.sleep(100);
        System.out.println("TwoThread stop");
        return "twoThread";
    }
}

class OneThread implements Callable<String>{

    @Override
    public String call() throws Exception {
        Thread.sleep(20000);
        System.out.println("OneThread stop");
        return "OneThread";
    }
}
