package com.thread;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;

import static com.thread.TwoThreadExchange4.t2;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/19 1:27 下午
* 实现一个容器，提供两个方法add、size，写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个，线程2给出提示并结束
 *
 * 使用两个Semaphore
*********************************************************************
*/
public class TwoThreadExchange5 {
    List<Object> list = Collections.synchronizedList(new LinkedList<>());

    public void add(Object o){
        list.add(o);
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {
        TwoThreadExchange5 c = new TwoThreadExchange5();
        Semaphore s1 = new Semaphore(0);
        Semaphore s2 = new Semaphore(0);

        //线程1
        Thread t1 = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                c.list.add(new Object());
                System.out.println("add " + i);
                if(c.size() == 5){
                    s2.release();
                    try {
                        s1.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1");

        //线程2
        Thread t2 = new Thread(()->{
            try {
                s2.acquire();   //上来肯定阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2 结束！");
            s1.release();       //放行
        }, "t2");


        t1.start();
        t2.start();
    }
}
