package com.aqs.mylock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/6 2:14 下午
 *
*********************************************************************
*/
public class MylockC implements Lock {
    private volatile int i = 0;

    private SyncA sync = new SyncA();

    @Override
    public void lock() {
        sync.acquire(1);
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
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
