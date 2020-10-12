package com.queue;

import java.sql.Time;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 3:15 下午
*
*********************************************************************
*/
public class T081_SynchronousQueue {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger a = new AtomicInteger(65);
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

        //消费者
        new Thread(()->{
            while(true){
                try {
                    System.out.println(synchronousQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        TimeUnit.MILLISECONDS.sleep(100);
//
//        synchronousQueue.add("a");
//        TimeUnit.MILLISECONDS.sleep(100);
//
//        synchronousQueue.offer("b");
//        TimeUnit.MILLISECONDS.sleep(100);

//        synchronousQueue.put("c");


        //生产者
        new Thread(()->{
            while(true){
                try {
                    synchronousQueue.put(String.valueOf((char)a.getAndIncrement()));
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
