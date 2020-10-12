package com.threadPool;

import java.util.concurrent.*;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 6:47 下午
*
*********************************************************************
*/
public class T03_Callable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<Integer> c = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for(int i = 0; i < 100; i++){
                    sum += i;
                }
                TimeUnit.SECONDS.sleep(5);
                return sum;
            }
        };


        ExecutorService service = Executors.newCachedThreadPool();
        Future<Integer> future = service.submit(c);      //异步提交，所以提交完之后，下面的语句可以立马执行
        System.out.println("这里会立马执行，而不需要等待5秒钟！");

        System.out.println("future.get()是阻塞的，这里会阻塞5秒，直到future.get()获取到结果：" + future.get());

        service.shutdown();
    }
}
