package com.poles.day5;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-27 11:02
* @desc 
*
*********************************************************************
*/
public class SemaphoreDemo implements Runnable{
    //初始化5个许可, permits = 5
    final Semaphore semaphore = new Semaphore(5);


    @Override
    public void run() {
        try {
            //线程执行体：线程执行时，先申请一个许可，然后往下操作，完成之后，释放许可
            semaphore.acquire();   //阻塞式的
            //模拟一个耗时操作
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + ":done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        //通过线程池，创建20个线程
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(20,
                new BasicThreadFactory.Builder().namingPattern("semaphore-schedule-pool-%d").build());
        //创建20个线程
//        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final SemaphoreDemo demo = new SemaphoreDemo();
        for (int i = 0; i < 20; i++) {
            executorService.submit(demo);
        }

        executorService.shutdown();

        /**
         * 总共有20个线程执行，每个线程申请一个许可，因为许可总数是5个，所以刚开始有5个线程瞬间就执行完毕
         * 然后2秒钟以后，有5个线程执行完成然后释放许可，另外5个线程各自获取到1个许可，所有又会有5个线程瞬间执行完毕......
         *
         * 20个线程，5个许可，那当然是5个5个的执行，每个线程执行时间是2秒，所以打印效果就是每2秒执行5个线程
         *
         * 最后执行完成之后，线程没有结束，指的是线程池没有结束，我们的20个线程都是完成了的
         * 如果要结束，最后加一句：executorService.shutdown(); 即可。 可以看看CountDownLatchDemo这个例子。
         *
         * 也可以一个线程同时获取2个许可，可以试试：
         *     @Override
         *     public void run() {
         *         try {
         *             //线程执行体：线程执行时，先申请一个许可，然后往下操作，完成之后，释放许可
         *             semaphore.acquire(2);
         *             //模拟一个耗时操作
         *             Thread.sleep(2000);
         *             System.out.println(Thread.currentThread().getName() + ":done!");
         *         } catch (InterruptedException e) {
         *             e.printStackTrace();
         *         } finally {
         *             semaphore.release(2);
         *         }
         *     }
         *
         * 由于总共5个许可，每个线程执行需要2个许可，所以每次只能执行2个线程，20个线程就是每2两秒执行2个线程，直到执行完20个线程
         */
    }
}
