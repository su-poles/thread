package com.poles.day5;import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-26 23:20
* @desc
 * 可限时，也就是尝试获取锁，如果超时还没有获取到锁，则进行获取锁失败后的处理
*
*********************************************************************
*/
public class ReentrantLockTimeLockDemo implements Runnable{
    public static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                Thread.sleep(6000);
            }else {
                System.out.println("get lock failed!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        //我这里创建两个线程，由于lock是static变量，所以相当于这个锁是加载了类上，是否创建多个实例没关系
        Thread t1 = new Thread(new ReentrantLockTimeLockDemo());
        Thread t2 = new Thread(new ReentrantLockTimeLockDemo());
        //我启动两个线程，当获取锁之后，会等待6秒，所以另一个线程获取锁最多只能等待5秒，5秒之类获取锁不成功，则会打印get lock failed!
        t1.start();
        t2.start();
    }
}
