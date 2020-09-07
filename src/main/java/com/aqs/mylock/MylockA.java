package com.aqs.mylock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
*********************************************************************
* @author poles
* @date 2020/9/6 2:14 下午
 * 本锁，本意是通过一个volatile的int变量来实现，如果信号量等于1，表示有锁，所有线程都不可用，如果信号量等于0，则表示无锁，所有线程都可以竞争
*********************************************************************
*/
public class MylockA implements Lock {
    private volatile int i = 0;         //初始化为0，表示此时无锁

    @Override
    public void lock() {
        //这里先通过synchronized关键字实现一下
        synchronized (this){
            i = 1;
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        synchronized (this){
            i = 0;
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
