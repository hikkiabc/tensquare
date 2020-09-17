package com.tsq;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ShareData1 {
    private BlockingQueue<String> blockingQueue;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private volatile Boolean flag = true;

    public ShareData1(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void produce() {
        String data=null;
        while (flag) {
            try {
                data=atomicInteger.incrementAndGet() + "";
                boolean offer = blockingQueue.offer(data, 2, TimeUnit.SECONDS);

                if (offer) {
                    System.out.println("offer success " + data);
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("produce stopped");
    }

    public void consume() {
        while (flag) {
            try {
                String poll = blockingQueue.poll(2, TimeUnit.SECONDS);
                if (null == poll || poll.equalsIgnoreCase("")) {
                    System.out.println("got nothing");

                } else {
                    System.out.println("got " + poll);
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("produce stopped");
    }

    public void stop() {
        flag = false;
    }
}

public class BlockingQueueDemo {
    public static void main(String[] args) {
        ShareData1 shareData1 = new ShareData1(new ArrayBlockingQueue<String>(10));
        new Thread(()->{
            shareData1.consume();
        }).start();

        new Thread(()->{
            shareData1.produce();
        }).start();

        try {
            TimeUnit.SECONDS.sleep(3);
            shareData1.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
