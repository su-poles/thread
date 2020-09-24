package com.aqs.mylock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
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
public class MylockD implements Lock {
    private volatile int i = 0;

    private Sync sync = new Sync();

    private class Sync extends AbstractQueuedSynchronizer{
        @Override
        //acquire会调用tryAcquire，当前线程如何竞争锁，如何加入队列，如何保障下面的代码是原子的，这些都是AQS框架来实现的，我们只需要实现最简单的tryAcquire即可
        protected boolean tryAcquire(int arg) {
            assert arg == 1;      //API里面要求调用acquire时要传入一个1，否则不可用，所以这里要判断一下是否为1
            if(compareAndSetState(0, 1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            assert arg == 1;
            if(isHeldExclusively()){
                //如果当前线程持有当前锁，才能释放
                setExclusiveOwnerThread(null);
                setState(0);
                return true;
            }

            //否则抛异常
            throw new IllegalMonitorStateException();
        }

        @Override
        protected boolean isHeldExclusively() {
            //先获取真正持有排它锁的线程，然后看看与当前线程是不是同一个
            return getExclusiveOwnerThread() == Thread.currentThread();
        }
    }

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
