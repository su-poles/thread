package com.aqs;

import com.aqs.mylock.MylockA;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:48 下午
* 在AQS_1的基础上，使用自定义的一个Lock（继承自AQS类）来实现m++的线程安全的操作
 * 但是，这个锁，好不好用呢？我相信你是有办法判断这个锁对不对的，所以这里不给出答案
*********************************************************************
*/
public class AQS_4 {
    public static int m = 0;
    public static Lock lock = new MylockA();

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
