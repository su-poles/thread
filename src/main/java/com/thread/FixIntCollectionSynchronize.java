package com.thread;

import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/19 11:18 下午
*   面试题：写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 *  能够支持2个生产者线程以及10个消费者线程的阻塞调用
*********************************************************************
*/
public class FixIntCollectionSynchronize {
    private final int maxSize = 10;
    private int size;

    int[] collect = new int[maxSize];

    public synchronized int getCount(){
        return size;
    }

    public synchronized void put(int value){
        //线程任何是被唤醒，第一件事就是判断是否满了，如果满了，则唤醒其它消费者线程，自己睡眠
        while(size == maxSize){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        collect[size++] = value;
        System.out.println(Thread.currentThread().getName() + "添加了新元素：" + value + ", 当前容器容量：" + size);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyAll();
    }

    public synchronized int get(){
        //当前线程被唤醒，无论任何时候被唤醒，醒来的第一件事就是判断size是否等于0，如果size==0，唤醒生产者，自己睡眠，否则返回collect[--size];
        while(size == 0){
            try {
                wait();             //消费者线程等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //每次取最新插入的，size作为该容器的容量，也作为指针，指针之后的数据不提供读取的API
        int value = collect[--size];
        System.out.println(Thread.currentThread().getName() + "消费了元素：" + value + ", 当前容器容量：" + size);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyAll();        //唤醒其它线程（主要唤醒生产者）
        return value;
    }

    public static void main(String[] args) {
        FixIntCollectionSynchronize c = new FixIntCollectionSynchronize();

        //2个生产者线程，10个消费者线程
        for(int i = 0; i < 10; i++){
            new Thread(()->{
                while(true){
                    int value = (int)(Math.random()*100);
                    c.put(value);        //随机生成一个2位数的数字，不断添加
                }
            }, "producer_" + i).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while(true){
                    int value = c.get();
                }
            }, "consumer_" + i).start();
        }
    }
}
