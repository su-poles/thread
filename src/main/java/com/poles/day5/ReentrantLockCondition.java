package com.poles.day5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-27 09:57
* @desc 
*
*********************************************************************
*/
public class ReentrantLockCondition implements Runnable{
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lock();
            condition.await();
            System.out.println("Thread is going on!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new ReentrantLockCondition());
        t.start();
        System.out.println("请等待3秒钟！");
        Thread.sleep(3000);
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    /**
     * main方法中先启动线程，睡眠1000毫秒是保证让线程先启动，并且执行到await这一步。  先搞清楚，await是谁的方法？
     * 当线程await之后，就挂起了
     */
}
