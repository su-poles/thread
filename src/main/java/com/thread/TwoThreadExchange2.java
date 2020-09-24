package com.thread;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/19 1:27 下午
* 实现一个容器，提供两个方法add、size，写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个，线程2给出提示并结束
 *
 * 保证先启动线程2，现成2判断list.size() != 5，进入等待状态
 * 启动线程1，开始添加元素，如果等于5，则唤醒线程2，然后提示，并运行结束，之后线程1继续添加元素直到10个元素添加完成
 * 重点：wait与notify方法必须得到锁之后才能执行。object.wait()方法会释放锁，但是（面试重点），object.notify()方法并不释放锁，所以最后的实现过程如下
*********************************************************************
*/
public class TwoThreadExchange2 {
    List<Object> list = Collections.synchronizedList(new LinkedList<>());

    public void add(Object o){
        list.add(o);
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {
        TwoThreadExchange2 c = new TwoThreadExchange2();

        //线程1
        new Thread(()->{
            synchronized (c){
                for(int i = 0; i < 10; i++){
                    c.list.add(new Object());
                    System.out.println("add " + i);
                    if(c.list.size() == 5){
                        c.notify();                             //随机唤醒一个等待的线程（但实际上只有线程2处于等待状态，等待拿到锁对象c)）
                        try {
                            c.wait();                           //当前线程进入等待队列
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, "t1").start();

        //线程2
        new Thread(()->{
            synchronized (c){
                if(c.list.size() != 5){
                    try {
                        c.wait();                               //当前线程进入等待队列
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2 结束！");
                c.notify();                                    //随机唤醒一个等待状态的队列，实际上此时其它处于等待状态的只可能有线程1
            }
        }, "t2").start();
    }
}
