package com.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/12 3:14 下午
*
*********************************************************************
*/
public class T07_Execturos_newSingleThreadExecutor {
    public static void main(String[] args) {
        //单个线程的线程池，可以保证多个任务能顺序执行
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        for(int i = 0; i < 5; i++){
            final int j = i;
            threadPool.execute(()->{
                System.out.println("线程 " + Thread.currentThread().getName() + " 执行任务 " + j);
            });
        }

        threadPool.shutdown();
    }
}
