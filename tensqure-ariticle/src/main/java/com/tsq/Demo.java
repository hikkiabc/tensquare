package com.tsq;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();
    public void lock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName()+"want lock");
        while (!atomicReference.compareAndSet(null, thread)){

        };

    }
    public void unlock(){
        Thread thread=Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println(thread.getName()+" unlock");
    }
    public static void main(String[] args) {
        Demo demo = new Demo();
        new Thread(()->{
            demo. lock();
            try {
                TimeUnit.SECONDS.sleep(3);
                demo. unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"a").start();
        new Thread(()->{
            demo.  lock();
            try {
                TimeUnit.SECONDS.sleep(3);
                demo. unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"b").start();
    }



}
