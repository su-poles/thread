package com.thread;

import java.util.concurrent.Exchanger;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/18 7:54 下午
* 两个线程交换数据，可以使用Exchanger交换，只能两个线程交换，多个线程交换，需要明确交换规则是啥，然后两两之间想办法用Exchanger倒是有可能
*********************************************************************
*/
public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(()->{
            String s = "x";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程：" + Thread.currentThread().getName() + " 中s的值为：" + s);
        },"t1").start();

        new Thread(()->{
            String s = "y";
            try {
                s = exchanger.exchange(s);
                System.out.println("线程：" + Thread.currentThread().getName() + " 中s的值为：" + s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}
