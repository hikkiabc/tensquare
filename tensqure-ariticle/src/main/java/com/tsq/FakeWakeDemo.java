package com.tsq;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FakeWakeDemo {

    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws InterruptedException {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " in inc");
        while (number != 0) {
            condition.await();
//            System.out.println("inc "+number);
        }
        number++;
        System.out.println(Thread.currentThread().getName() + number);
        condition.signalAll();
        lock.unlock();

    }

    public void decrement() throws InterruptedException {

        lock.lock();
        System.out.println(Thread.currentThread().getName() + " in dec");

        while (number == 0) {
            condition.await();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + number);
        condition.signalAll();
        lock.unlock();
    }


    public static void main(String[] args) throws InterruptedException {
        FakeWakeDemo data = new FakeWakeDemo();
   
        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    data.decrement();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "a").start();
        new Thread(() -> {
            try {
                for (int i = 0; i <5; i++) {
                    data.increment();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "b").start();
        new Thread(() -> {
            try {
                for (int i = 0; i <5; i++) {
                    data.decrement();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "a1").start();
        TimeUnit.SECONDS.sleep(2);
        new Thread(() -> {
            try {
                for (int i = 0; i <5; i++) {
                    data.increment();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "b1").start();
    }
}

