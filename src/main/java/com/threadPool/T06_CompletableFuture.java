package com.threadPool;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 10:18 下午
*
*********************************************************************
*/
public class T06_CompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //异步执行任务1，执行的结果也会存在CompletableFuture中，即存在future1中，下同
        CompletableFuture<Double> future1 = CompletableFuture.supplyAsync(()->task1());
        //异步执行任务2
        CompletableFuture<Double> future2 = CompletableFuture.supplyAsync(()->task2());
        //异步执行任务3
        CompletableFuture<Double> future3 = CompletableFuture.supplyAsync(T06_CompletableFuture::task3);


        //使用CompletableFuture管理多个future，allOf是个阻塞方法，当下面三个future全部执行完之后，才能继续往后走
//        CompletableFuture.allOf(future1, future2, future3).join();
//        System.out.println("当future1, future2, future3全部执行完成后，才会打印这句话，需要等3秒钟");
//        System.out.println(future1.get());
//        System.out.println(future2.get());
//        System.out.println(future3.get());


        //其中任意一个执行完成，就继续往后走
        CompletableFuture.anyOf(future1, future2, future3).join();
        if(future1.isDone() || future2.isDone() || future3.isDone()){
            System.out.println("程序执行完成！");
        }

        //这个跟上面没关系，就是λ表达式的一些写法
        CompletableFuture.supplyAsync(T06_CompletableFuture::task1)
                .thenApply(String::valueOf)
                .thenApply(str -> "price：" + str)
                .thenAccept(System.out::println)
                .get();
    }

    //任务1
    private static Double task1() {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("耗时1秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 100d;
    }

    //任务2
    private static Double task2() {
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("耗时5秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 200d;
    }

    //任务3
    private static Double task3() {
        try {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("耗时10秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 300d;
    }
}
