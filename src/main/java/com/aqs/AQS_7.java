package com.aqs;

import com.aqs.mylock.MylockB;
import com.aqs.mylock.MylockD;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:48 下午
*  这个类，没啥，就是100个线程，每次都循环然后t.join看着不爽，然后使用CountDownLatch秀一把
 *  CountDownLatch, 倒数的门栓
*********************************************************************
*/
public class AQS_7 {
    public static int m = 0;
    public static Lock lock = new MylockD();
    public static CountDownLatch cdl = new CountDownLatch(100);

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];

        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                lock.lock();
                try{
                    for(int j = 0; j < 10000; j++){
                        m++;
                    }
                }finally {
                    lock.unlock();
                }
                cdl.countDown();
            });
        }

        for (Thread t : threads){
            t.start();
        }

        //等待倒数，100个线程执行完了，才能往后继续执行
        cdl.await();

        System.out.println(m);
    }
}
