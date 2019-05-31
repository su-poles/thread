package com.poles.day6;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.*;

/**
 * ********************************************************************
 *
 * @author poles
 * @date 2019-05-31 10:31
 * @desc ********************************************************************
 */
public class RejectThreadPoolDemo {
    private static final Log logger = LogFactory.getLog(RejectThreadPoolDemo.class);

    public static class MyTask implements Runnable {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ": Thread Id:" + Thread.currentThread().getId());
            try {
                //每个线程执行时间为100毫秒
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //拒绝策略
                System.out.println(r.toString() + "is discard!");
            }
        });

        for (int i = 0; i < 10000; i++) {
            es.submit(task);
            //每隔10毫秒就提交一个任务，而每个任务需要执行100毫秒，所以提交速度大于执行速度，总会有一些线程会进入"拒绝策略"
            Thread.sleep(10);
        }
    }

}
