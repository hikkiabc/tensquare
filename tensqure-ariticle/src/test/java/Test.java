import com.mongodb.BasicDBObject;
//import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    @org.junit.Test
    public void test1() {
    new Thread(()->{
        lock();
        try {
            TimeUnit.SECONDS.sleep(3);
            unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    },"a").start();
    new Thread(()->{
        lock();
        try {
            TimeUnit.SECONDS.sleep(3);
            unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    },"b").start();

    }

    public void lock() {
        Thread thread = Thread.currentThread();
        while (!atomicReference.compareAndSet(null, thread)){

        };
        System.out.println(thread.getName()+" lock");
    }
    public void unlock(){
        Thread thread=Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println(thread.getName()+" unlock");
    }
}
