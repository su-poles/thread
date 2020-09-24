package com.aqs;

import com.aqs.mylock.MylockD;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:48 下午
*  这个类，也没啥，AQS_7复习了一把CountDownLatch, 所以再想复习一下CyclicBarrier
*********************************************************************
*/
public class AQS_8 {
    public static int m = 0;
    public static Lock lock = new MylockD();

    //等100个线程都执行完了，才能执行BarrierAction
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(100, (()->{
        //此处就是BarrierAction, 可以单独写一个类，然后继承Thread或者实现Runnable即可
        //parties=100表示100个线程执行完成之后，会执行这里的BarrierAction
        //如果parties=10, 则由于总线程数是100个，所以每10次就会执行一次这里BarrierAction，由于这里的打印m时是线程不安全的，所以这10个数，最后一次是正确的，中间的嘛可能都不准确
        System.out.println(m);
    }));

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];

        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                lock.lock();
                try{
                    for(int j = 0; j < 10000; j++){
                        m++;
                    }
                    //当前线程执行到这里，就等待，等待100个满了之后，一起走
                } finally {
                    lock.unlock();
                }
                try {
                    cyclicBarrier.await();

                    //这里打印m，这100个线程执行有快慢，都需要在cyclicBarrier这里等待，然后等100个线程都执行完成之后，一起打印出这个m的结果，就是打印100次1000000
                    //如果这个100个线程都结束后，要做某一件事，就放在BarrierAction里去做就可以了
//                    System.out.println(m);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

            });
        }

        for (Thread t : threads){
            t.start();
        }
    }
}
