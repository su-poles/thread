package com.poles.day5;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.LockSupport;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-27 17:16
* @desc 锁支持
*
*********************************************************************
*/
@Setter
public class LockSupportDemo implements Runnable{
    public static Object obj = new Object();
    private String name;

    public LockSupportDemo(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (obj) {
            System.out.println("in " + name);
            //线程挂起
            LockSupport.park();
            System.out.println("out " + name);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new LockSupportDemo("t1"));
        Thread t2 = new Thread(new LockSupportDemo("t2"));
        t1.start();
        Thread.sleep(1000);
        //线程恢复执行
        LockSupport.unpark(t1);
        //线程t2开启，即使unpark执行在park之前
        t2.start();
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
        /**
         * LockSupport可以响应中断，但不抛出异常, 可以得到中断标识（也就是有人调用terrup()时，可以中断线程）
         * 中断响应的结果是，park()函数的返回，可以从Thread.interrupted()中得到中断标志
         */
    }
}
