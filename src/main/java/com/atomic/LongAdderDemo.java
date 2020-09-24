package com.atomic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/17 10:24 下午
* 分别使用三种方式进行++操作, 开100个线程，每个线程做10万次累加，对比哪个效率更高一些
 * 这种方式测试比较简单，但也能一定程度说明问题，更专业的性能测试方式有：jmh,  Java Microbenchmark Harness
 *
 * 结论是：LongAdder效率更高
 * 当线程数多的情况下，比如线程数1000，每个线程累计10万， 那么AtomicLong效率会比Synchronized差很多，因为会有大量线程进行CAS操作，无故消耗CPU
 * 当线程数很少，比如线程数10，每个线程累加1000万的时候，AtomicLong效率会比Synchronized高一倍多
 *
 * 当线程数很多的时候，竞争激烈的时候，CAS是不可取的，应该直接使用锁
 * 如果线程数很少，则可以考虑用CAS的方式，但是如果每个线程执行时间很长，依然应该使用锁
 *
 * LongAdder在线程数并发程度非常高的时候，效率是最高的，反之也不一定。 LongAdder使用了分段锁的思想，内部实现也是CAS，直说思路：比如1000个线程，可以分4部分，没250个分别累加，最后4个再累加即为总值。
*********************************************************************
*/
public class LongAdderDemo {
    private long counter1;
    private AtomicLong counter2 = new AtomicLong(0L);
    private LongAdder longAdder = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        LongAdderDemo lad = new LongAdderDemo();
        final int threadNumber = 10;
        final int loopTimes = 1000_0000;

        /*
         * 方式一：使用Synchronized
         */
        Thread[] threads = new Thread[threadNumber];
        for(int i = 0; i < threads.length; i++){
            Thread ti = new Thread(()->{
                for(int j = 0; j < loopTimes; j++){
                    synchronized (lad){
                        lad.counter1++;
                    }
                }
            });
            threads[i] = ti;
        }

        long start = System.currentTimeMillis();
        for (Thread ti : threads) {
            ti.start();
        }

        for (Thread ti : threads){
            ti.join();
        }

        System.out.println("使用Synchronized方式，计算结果："+lad.counter1+", 耗时：" + (System.currentTimeMillis() - start) + "毫秒！");


        /*
         * 方式二：使用AtomicLong
         */
        for(int j = 0; j < threads.length; j++){
            threads[j] = new Thread(()->{
                for(int m = 0; m < loopTimes; m++){
                    lad.counter2.incrementAndGet();     //线程安全，直接累计
                }
            });
        }
        long start2 = System.currentTimeMillis();
        for (Thread ti : threads) {
            ti.start();
        }

        for (Thread ti : threads){
            ti.join();
        }

        System.out.println("使用AtomicLong方式，计算结果："+lad.counter2.get()+", 耗时：" + (System.currentTimeMillis() - start2) + "毫秒！");


        /*
         * 方式三：使用LongAdder
         */
        for(int k = 0; k < threads.length; k++){
            threads[k] = new Thread(()->{
                for(int m = 0; m < loopTimes; m++){
                    lad.longAdder.increment();     //线程安全，直接累计
                }
            });
        }
        long start3 = System.currentTimeMillis();
        for (Thread ti : threads) {
            ti.start();
        }

        for (Thread ti : threads){
            ti.join();
        }

        System.out.println("使用LongAdder方式，计算结果："+lad.longAdder.longValue()+", 耗时：" + (System.currentTimeMillis() - start3) + "毫秒！");
    }
}
