package com.threadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 9:19 下午
*
*********************************************************************
*/
public class T06_FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(()->{
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            TimeUnit.SECONDS.sleep(2);
            return sum;
        });                 // new Callable(){ Integer call()}

        new Thread(task).start();

        System.out.println(task.get());     //阻塞等待Callable的执行结果
    }
}
