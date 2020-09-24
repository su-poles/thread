package com.thread;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/19 1:27 下午
* 实现一个容器，提供两个方法add、size，写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个，线程2给出提示并结束
 *
 * 使用两个countDownLatch
 * 线程2开始执行，通过一个门闩拴住自己
 * 线程1开始执行，添加到5个元素时，拴住自己，然后打开线程2的门栓
 * 现成继续执行，然后提示内容，打开线程1的门闩，线程2执行完毕
*********************************************************************
*/
public class TwoThreadExchange3 {
    List<Object> list = Collections.synchronizedList(new LinkedList<>());

    public void add(Object o){
        list.add(o);
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {
        TwoThreadExchange3 c = new TwoThreadExchange3();
        CountDownLatch cd1 = new CountDownLatch(1);
        CountDownLatch cd2 = new CountDownLatch(1);

        //线程2
        new Thread(()->{
            if(c.list.size() != 5){
                try {
                    cd1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("t2 结束！");
            cd2.countDown();        //打开线程1的门闩
        }, "t1").start();

        //线程1
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                c.list.add(new Object());
                System.out.println("add " + i);
                if(c.size() == 5){
                    cd1.countDown();    //打开线程2的门闩
                    try {
                        cd2.await();        //自己用门闩拴住自己，等待线程2执行完毕是打开自己的门闩
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, "t2").start();
    }
}
