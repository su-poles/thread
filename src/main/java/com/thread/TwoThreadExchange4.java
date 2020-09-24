package com.thread;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/19 1:27 下午
* 实现一个容器，提供两个方法add、size，写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个，线程2给出提示并结束
 *
 * 使用两个LockSupport
*********************************************************************
*/
public class TwoThreadExchange4 {
    List<Object> list = Collections.synchronizedList(new LinkedList<>());

    public void add(Object o){
        list.add(o);
    }

    public int size(){
        return list.size();
    }

    static Thread t1,t2;

    public static void main(String[] args) {
        TwoThreadExchange4 c = new TwoThreadExchange4();

        //线程1
        t1 = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                c.list.add(new Object());
                System.out.println("add " + i);
                if(c.size() == 5){
                    LockSupport.unpark(t2);   //通知t2线程继续
                    LockSupport.park();     //当前线程进入阻塞
                }
            }
        }, "t1");

        //线程2
        t2 = new Thread(()->{
//            if(c.list.size() != 5){
                LockSupport.park();             //当前线程阻塞
//            }

            System.out.println("t2 结束！");
            LockSupport.unpark(t1);            //t2执行完之前先通知t1继续
        }, "t2");


        t1.start();
        t2.start();
    }
}
