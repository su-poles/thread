package com.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/12 3:14 下午
*
*********************************************************************
*/
public class T07_Execturos_newScheduledThreadPool {
    public static void main(String[] args) {
        //1. 普通线程池 + DelayWorkQueue
        //2. 指定线程的执行频率，即配置cron
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

        //1秒钟之后执行一次该任务
        service.schedule(()->{
            System.out.println(Thread.currentThread().getName());
        }, 1, TimeUnit.SECONDS);

        //5秒之后开始执行，每隔1秒执行一次
        service.scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName());
        }, 5 , 1, TimeUnit.SECONDS);
    }
}
