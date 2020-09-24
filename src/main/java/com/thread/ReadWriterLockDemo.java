package com.thread;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/18 5:14 下午
* 读写锁，多个线程可以同时读，但是写线程与其它线程是互斥的，只要有写，其它任何线程包括读线程都要等待
*********************************************************************
*/
public class ReadWriterLockDemo {

    //模拟读取x、y的值，写入时也写入x,y的值
    private int x;
    private int y;

    public void read(Lock lock){
        if(lock == null){
            try {
                TimeUnit.SECONDS.sleep(2);
                //不加锁执行
                int x = this.x;
                int y = this.y;
                System.out.printf("read: x,y = %s,%s\n", x, y);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            //加锁执行
            try{
                lock.lock();
                int x = this.x;
                int y = this.y;
                System.out.printf("read: x,y = %s,%s\n", x, y);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void writer(Lock lock, int x, int y){
        try{
            lock.lock();
            this.x = x;
            TimeUnit.SECONDS.sleep(1);   //写入x,y时比较耗时，需要1秒才能写完
            this.y = y;
            System.out.printf("write: x,y = %s,%s\n", x, y);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriterLockDemo obj = new ReadWriterLockDemo();
        //定义一个普通锁
        ReentrantLock lock = new ReentrantLock();

        ReadWriteLock readWriterLock = new ReentrantReadWriteLock();
        Lock readLock = readWriterLock.readLock();      //定义一个读锁
        Lock writeLock = readWriterLock.writeLock();    //定义一个写锁

        //有a个线程并发读，b个线程并发写入
        int a = 50;
        int b = 50;
        Thread[] threads = new Thread[a + b];

        //写线程
        for(int i = 0; i < a; i++){
            threads[i] = new Thread(()->{
                int x = (int)(Math.random()*10);
                int y = x * x;
                //obj.writer(lock, x, y);     //使用普通锁
                obj.writer(writeLock, x, y);     //使用读写锁--写锁
            });
        }


        //读线程
        for(int i = 0; i < b; i++){
            threads[a + i] = new Thread(()->{
//                obj.read(lock);  //传入普通锁
                obj.read(readLock);     //使用读锁
//                obj.read(null);   //不加锁， 如果不加锁，那么当写入写了一半时，则读线程会读取到一半的数据，属于脏读。应该是读与写互斥，当有正在写时，是不能读的，即读写互斥，所以读要加读锁
            });
        }

        //启动所有线程
        List<Thread> collect = Arrays.stream(threads).collect(Collectors.toList());
        Collections.shuffle(collect);

        for(Thread thead : collect){
            thead.start();
        }
    }
}
