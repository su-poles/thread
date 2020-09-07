package com.aqs;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:48 下午
* 在AQS_1的基础上，使用重入锁re-entrant-lock来实现m++线程安全的操作
*********************************************************************
*/
public class AQS_3 {
    public static int m = 0;
    public static Lock lock = new ReentrantLock(false);

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];

        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                //加锁，一定要try...finally
                lock.lock();  //底层用的就是AQS
                try{
                    for(int j = 0; j < 10000; j++){
                        m++;
                    }
                }finally {
                    lock.unlock();
                }
            });
        }

        for (Thread t : threads){
            t.start();
        }

        for (Thread t : threads){
            t.join();
        }

        System.out.println(m);
    }
}
