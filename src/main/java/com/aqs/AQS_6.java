package com.aqs;

import com.aqs.mylock.MylockB;

import java.util.concurrent.locks.Lock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:48 下午
*
*********************************************************************
*/
public class AQS_6 {
    public static int m = 0;
    public static Lock lock = new MylockB();

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
