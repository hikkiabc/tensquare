package com.tsq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class DemoCountDownLatch {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        List<Thread> threadList=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Thread thread1 = new Thread(new Athlete(cyclicBarrier, "start name " + i, countDownLatch));
            Thread thread = new Thread(new Athlete(null, "second name " + i, countDownLatch));
            threadList.add(thread);
            threadList.add(thread1);

        }
        for (Thread thread : threadList) {
            thread.start();
        }
    }
    static class Athlete implements Runnable{
        public CyclicBarrier cyclicBarrier;
        public String name;
        public CountDownLatch countDownLatch;

        public Athlete(CyclicBarrier cyclicBarrier, String name, CountDownLatch countDownLatch) {
            this.cyclicBarrier = cyclicBarrier;
            this.name = name;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
        if (cyclicBarrier!=null){
            System.out.println(name+" ready");
            try {
                cyclicBarrier.await();
                System.out.println(name+" arrives");
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
        if (cyclicBarrier==null){
            System.out.println(name+" waiting for stick");
            try {
                countDownLatch.await();
                System.out.println(name+" arrives destination");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        }
    }
}
