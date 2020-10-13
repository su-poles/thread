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
public class T07_Execturos_newFixedThreadPool {
    public static void main(String[] args) {
        //线程数固定的线程数，核心线程数与最大线程数是同一个，都是固定，所以超时回收时间为0，因为没有可被回收的线程
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        for(int i = 0; i < 5; i++){
            final int j = i;
            threadPool.execute(()->{
                System.out.println("线程 " + Thread.currentThread().getName() + " 执行任务 " + j);
            });
        }

        threadPool.shutdown();
    }
}
