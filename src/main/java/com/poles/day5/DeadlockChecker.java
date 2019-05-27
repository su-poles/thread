package com.poles.day5;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-26 22:29
* @desc 
*
*********************************************************************
*/
public class DeadlockChecker {
    private final static ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
    //定义一个线程的执行体
    final static Runnable deadlockCheck = (() -> {
        for (; ; ) {
            long[] deadlockedThreadIds = mbean.findDeadlockedThreads();
            if (deadlockedThreadIds != null) {
                ThreadInfo[] threadInfos = mbean.getThreadInfo(deadlockedThreadIds);
                for (Thread t : Thread.getAllStackTraces().keySet()) {
                    for (int i = 0; i < threadInfos.length; i++) {
                        if (t.getId() == threadInfos[i].getThreadId()) {
                            t.interrupt();
                        }
                    }
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public static void check() {
        //单独开启一个线程
        Thread t = new Thread(deadlockCheck);
        //设置为守护线程，如果虚拟机中已经没有非守护线程了，那么守护线程不会影响JVM的销毁
        //当我关闭处于等待中的线程时，守护线程自动也会销毁
        t.setDaemon(true);
        t.start();
    }
}
