package com.tsq;

import java.util.concurrent.*;

public class ThreadPoolDemo {

public static void main(String[] args) {
    ExecutorService threadPool=new ThreadPoolExecutor(
            2,5,1, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );
//    ExecutorService pool= Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
        threadPool.execute(()->{
            System.out.println(Thread.currentThread().getName()+" start");
        });
    }
    threadPool.shutdown();
}
}
