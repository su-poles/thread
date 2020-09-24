package com.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/21 12:43 下午
*
*********************************************************************
*/
public class ReentrantLockDemo {
    public static void main(String[] args) {
        int i = 0;
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try{
            i++;
        }finally {
            lock.unlock();
        }
    }
}
