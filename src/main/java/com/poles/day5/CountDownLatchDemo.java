package com.poles.day5;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-27 15:06
* @desc 倒数计数器，类似于发射火箭，先不断完成各个检查项，完成一项，计数器就倒数一个数，直到计数器为0，那么就"发射"。 发射的意思就是唤醒await方法，继续执行
 * 可以这么立即，countDownLatch.await()就是阻塞线程，然后其他线程不断的做倒计时，当倒计数为0时，就唤醒了countDownLatch所在的线程。
*
*********************************************************************
*/
public class CountDownLatchDemo implements Runnable{
    //我们设置10个倒计数
    static final CountDownLatch cdl = new CountDownLatch(10);

    @Override
    public void run() {
        try {
            //模拟任务处理过程
            Thread.sleep(new Random().nextInt(1000));
            System.out.println("线程" + Thread.currentThread().getName() + "完成操作！");
            //任务处理完完成，倒计数减1, 当然了，cdl.countDown()这方法跟释放锁本质一样，所以应该放在finally里
            //cdl.countDown();
//            System.out.println("线程" + Thread.currentThread().getName() + "完成第二部分操作！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cdl.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //创建10个线程，分别处理完10个任务后，主线程开始。 每个任务完成时要倒计数一个数
        CountDownLatchDemo demo = new CountDownLatchDemo();
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(10, new BasicThreadFactory.Builder().namingPattern("CountDownLatch-demo-%d").build());
        for (int i = 0; i < 10; i++) {
            service.submit(demo);
        }

        //我这里需要阻塞一下，等待倒计时结束
        cdl.await();
        System.out.println("Rocket Fire!");
        service.shutdown();
    }
}
