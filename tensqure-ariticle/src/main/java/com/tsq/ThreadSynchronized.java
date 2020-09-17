package com.tsq;

public class ThreadSynchronized {
    public static void main(String[] args) throws InterruptedException {
        Demo demo = new Demo();
        Thread thread = new Thread(demo);
        demo.flag=true;
        thread.start();
        Thread.sleep(100);
    demo.flag=false;
        System.out.println("main ends");
    }

    static class Demo implements Runnable{
        public  Boolean flag;

        @Override
        public void run() {
            System.out.println("child starts");
            while (flag){
                synchronized (this){
                }
            }
            System.out.println("child ends");
        }
    }
}
