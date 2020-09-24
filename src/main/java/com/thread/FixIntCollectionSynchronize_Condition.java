package com.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/19 11:18 下午
*   面试题：写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 *  能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *  在FixIntCollectionSynchronize类中使用wait/notifyAll时，会唤醒所有线程，比如当某个消费者线程发现容器为空时，自己等待，此时应该值唤醒生产者即可
 *  所以这里使用ReentrantLock的Condition，  new一个Condition本质上就是new了一个等待队列
*********************************************************************
*/
public class FixIntCollectionSynchronize_Condition<T> {
    ReentrantLock lock = new ReentrantLock(false);
    Condition producer = lock.newCondition();
    Condition consumer = lock.newCondition();

    private final int maxSize = 10;
    private int size = 0;

    List<T> list = new ArrayList<>();

    public int getCount(){
        return size;
    }

    public void put(T value) {
        try{
            lock.lock();
            //如果满了，则唤醒消费者
            while(size == maxSize){
                producer.await();
            }

            list.add(size++, value);
            System.out.println(Thread.currentThread().getName() + "添加了新元素：" + value + ", 当前容器容量：" + size);
//            TimeUnit.MILLISECONDS.sleep(5);
            consumer.signalAll();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public T get(){
        try{
            lock.lock();
            //当前线程被唤醒，无论任何时候被唤醒，醒来的第一件事就是判断size是否等于0，如果size==0，唤醒生产者，自己睡眠，否则返回collect[--size];
            while(size == 0){
                consumer.await();
            }

            T value = list.get(--size);
            System.out.println(Thread.currentThread().getName() + "消费了元素：" + value + ", 当前容器容量：" + size);
            TimeUnit.MILLISECONDS.sleep(100);
            producer.signalAll();
            return value;
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

        return null;
    }

    public static void main(String[] args) {
        FixIntCollectionSynchronize_Condition<Integer> c = new FixIntCollectionSynchronize_Condition<>();

        //2个生产者线程, 每个线程往里写5个数
        for(int i = 0; i < 10; i++){
            new Thread(()->{
                for(int j = 0; j < 20; j++){
                    int value = (int)(Math.random()*100);
                    c.put(value);        //随机生成一个2位数的数字，不断添加
                }
            }, "producer_" + i).start();
        }

        //消费者线程，每个线程消费2个数
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for(;;){
                    int value = c.get();
                }
            }, "consumer_" + i).start();
        }
    }
}
