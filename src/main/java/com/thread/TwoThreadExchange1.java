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
*********************************************************************
*/
public class TwoThreadExchange1 {
    /*volatile*/ List<Object> list = Collections.synchronizedList(new LinkedList<>());

    public void add(Object o){
        list.add(o);
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {
        TwoThreadExchange1 c = new TwoThreadExchange1();

        Thread t1 = new Thread(()->{
            for(int i = 0; i < 10; i++){
                c.list.add(new Object());
                System.out.println("add " + i);
                if(c.list.size() == 5){
                    LockSupport.park();
                }
            }
        }, "t1");
        t1.start();

        new Thread(()->{
            boolean wait = true;
            while (wait){
                if(c.list.size() == 5){
                    wait = false;
                    System.out.println("t2 结束运行");
                    LockSupport.unpark(t1);
                }
            }
        }, "t2").start();
    }
}
