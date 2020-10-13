package com.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/12 3:14 下午
*
*********************************************************************
*/
public class T07_Execturos_newCachedThreadPool {
    public static void main(String[] args) {
        //核心线程数为0，执行任务时如果没有空闲线程，则创建一个新的线程
        ExecutorService threadPool = Executors.newCachedThreadPool();
        System.out.println("线程池初始状态：" + threadPool);

        for(int i = 0; i < 5; i++){
            final int j = i;
            threadPool.execute(()->{
                System.out.println("线程 " + Thread.currentThread().getName() + " 执行任务 " + j);
            });
            System.out.println(threadPool);
        }

        System.out.println("线程池最终状态：" + threadPool);
        threadPool.shutdown();
    }
}
