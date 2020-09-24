package com.thread;

import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/17 11:03 下午
* 这里由于m1和m2都要竞争同一把锁，main方法启动时，先睡1秒，让m1优先拿到锁，此时启动m2线程，由于获取不到锁，则一直处于阻塞状态
*********************************************************************
*/
public class ReentrantLock_Synchronized {
    synchronized void m1(){
        for (int i = 0; i < 10; i++){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
            if(i == 3){
                //这里直接调用m2,是可以，这里就体现了Synchronized的可重入性
                m2();
            }
        }
    }

    synchronized void m2(){
        System.out.println("m2 ...");
    }

    public static void main(String[] args) throws InterruptedException {
        //启动两个线程，分别调用 m1 和 m2
        ReentrantLock_Synchronized rs = new ReentrantLock_Synchronized();
        new Thread(rs::m1).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(rs::m2).start();
    }
}
