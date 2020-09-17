package com.tsq;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WriteReadLockDemo {
    private volatile Map<String,Object>map =new HashMap<>();
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock();
    public void put(String key, Object v){
        reentrantReadWriteLock.writeLock().lock();
        System.out.println(Thread.currentThread().getName()+" start write");
        map.put(key,v);
        System.out.println(Thread.currentThread().getName()+" finish write");
        reentrantReadWriteLock.writeLock().unlock();
    }
    public void get(String k){
        
        reentrantReadWriteLock.readLock().lock();
        System.out.println(Thread.currentThread().getName()+" start read");
        Object o = map.get(k);
        System.out.println(Thread.currentThread().getName()+" read "+o);
        reentrantReadWriteLock.readLock().unlock();
    }

    public static void main(String[] args) {
        WriteReadLockDemo writeReadLockDemo = new WriteReadLockDemo();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(()->{
                writeReadLockDemo.put(finalI +"", finalI);
            },"write-t"+i).start();
        }
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(()->{
                writeReadLockDemo.get(finalI+"");
            },"read-t"+i).start();
        }
    }
}
