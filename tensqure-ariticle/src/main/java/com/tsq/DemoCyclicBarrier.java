package com.tsq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DemoCyclicBarrier {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        List<Thread> threadList=new ArrayList<>();
        for (int i = 0; i <5 ; i++) {

            threadList.add(   new Thread(new Athlete(cyclicBarrier,"a"+i)));
        }
        for (Thread thread : threadList) {
            thread.start();
        }
    }

    static class Athlete implements Runnable{
        private CyclicBarrier cyclicBarrier;
        private String name;

        public Athlete(CyclicBarrier cyclicBarrier, String name) {
            this.cyclicBarrier = cyclicBarrier;
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name+" ready");
            try {
                cyclicBarrier.await();
                System.out.println(name+" arrives");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
