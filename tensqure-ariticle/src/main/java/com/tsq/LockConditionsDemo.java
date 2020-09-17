package com.tsq;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData{
    private int num=1;
    private Lock lock = new ReentrantLock();
    private Condition c1=lock.newCondition();
    private Condition c2=lock.newCondition();
    private Condition c3=lock.newCondition();
    public void print5() throws InterruptedException {
        lock.lock();
        System.out.println("a");
       while (num!=1){
            c1.await();
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName()+i);
        }
        num=2;
        c2.signal();
        lock.unlock();
    }
    public void print10() throws InterruptedException {
        lock.lock();
        System.out.println("b");
        while (num!=2){
            c2.await();
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+i);
        }
        num=3;
        c3.signal();
        lock.unlock();
    }
    public void print15() throws InterruptedException {
        lock.lock();
        System.out.println("c");

        while (num!=3){
            c3.await();
        }

        for (int i = 0; i < 15; i++) {
            System.out.println(Thread.currentThread().getName()+i);
        }
        num=1;
        c1.signal();
        lock.unlock();
    }
}

public class LockConditionsDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{

                try {
                    shareData.print5();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"a").start();
        }


        for (int i = 0; i < 5; i++) {
            new Thread(()->{

                try {
                    shareData.print10();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"b").start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(()->{

                try {
                    shareData.print15();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"c").start();
        }


    }
}
