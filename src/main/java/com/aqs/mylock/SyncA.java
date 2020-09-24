package com.aqs.mylock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/7 5:49 下午
*   MyLockC 实现了Lock接口，所得具体实现引用了当前类，这个版本来自网络，是有问题的
 *  MyLockD 是是MyLockC的改进，有问题的部分是MyLockC引用的SyncA有问题
 *  MyLockD 引用的Sync作为一个内部类被引用，因为只有自己这个锁用到，所以应该用内部类
*********************************************************************
*/
public class SyncA extends AbstractQueuedSynchronizer {
    @Override
    protected boolean tryAcquire(int arg) {
        if(compareAndSetState(0, 1)){
            //设置排它的拥有者线程为当前线程，即当前线程拥有一个排它锁（对应还有共享锁）
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    @Override
    protected boolean tryRelease(int arg) {
        //这里有问题
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }

    @Override
    protected boolean isHeldExclusively() {    //是否拥有排它锁
        //这里有问题
        return getState() == 1;
    }
}
