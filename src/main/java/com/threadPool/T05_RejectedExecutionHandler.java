package com.threadPool;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/12 1:17 下午
*
*********************************************************************
*/
public class T05_RejectedExecutionHandler implements Runnable {
    //任务编号
    private int i;

    public T05_RejectedExecutionHandler(int i){
        this.i = i;
    }

    @Override
    public String toString() {
        return "task" + i;
    }

    @Override
    public void run() {
        System.out.println("线程 " + Thread.currentThread().getName() + " 执行任务 Task " + i);
        try {
            //让线程阻塞，这样就可以通过打印来观察多个线程该由谁来执行，
            // 什么时候加入阻塞队列，什么时候创建非核心线程来执行，什么时候触发拒绝策略
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //通过线程池来执行任务
        ThreadPoolExecutor executors = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4), Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy()              //抛异常
//                new ThreadPoolExecutor.DiscardPolicy()              //丢弃
//                new ThreadPoolExecutor.DiscardOldestPolicy()      //丢弃最早
                new ThreadPoolExecutor.CallerRunsPolicy()         //主线程调用该线程池，则这个任务交给主线程执行
        );

        //执行8个任务，编号从0到7（4个最大线程 + 4个阻塞队列里的任务）
        for(int i = 0; i < 8; i++){
            executors.execute(new T05_RejectedExecutionHandler(i));
        }

        //查看阻塞队列里的任务
        System.out.println(executors.getQueue());

        executors.execute(new T05_RejectedExecutionHandler(99));        //额外再来一个任务
        System.out.println(executors.getQueue());

        executors.shutdown();
    }
}
