package com.poles.day5;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-27 14:59
* @desc 
* 读-读 不互斥：读读之间不阻塞
* 读-写 互斥：  读阻塞写，写阻塞读  （读写互斥是因为，不能读到写了一半的数据，要么没开始写，要么已经写完了，不能读取写了一部分的数据）
* 写-写 互斥：  写写阻塞
 * 使用方式与ReentrantLock是一样的
*********************************************************************
*/
public class ReadWriteLockDemo implements Runnable {
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //初始化读锁
    private static Lock readLock = readWriteLock.readLock();
    //初始化写锁
    private static Lock writeLock = readWriteLock.writeLock();

    @Override
    public void run() {

    }
}
