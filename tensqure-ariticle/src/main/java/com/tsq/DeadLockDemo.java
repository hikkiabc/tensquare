package com.tsq;


// look for deadlock,1: jps -l 2: jstack + process Number
class ShareData2 implements Runnable{
    private String lockA;
    private String lockB;

    public ShareData2(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+" has " +lockA+" want "+lockB );
            synchronized (lockB){
                System.out.println("got "+lockB);
            }
        }
    }
}
public class DeadLockDemo {
    public static void main(String[] args) {
        new Thread(new ShareData2("lockA","lockB"),"1").start();
        new Thread(new ShareData2("lockB","lockA"),"2").start();
    }
}
