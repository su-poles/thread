package com.poles.day2;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-20 11:25
* @desc
* synchronized 有三种加锁方式；
 * 1. 指定加锁对象：对给定对象加锁，进入同步代码块之前要获得给定对象的锁
 * 2. 直接加载实例方法上：加载普通的实例方法上，相对与对当前对象加锁，进入同步块之前要获得当前对象的锁
 * 3. 直接加载静态方法上：加在静态方法上，因为该方法是类的方法，不是对象的方法，所以实际上等于给当前类加锁，进入同步代码块之前要获得当前类的锁
 *
 * synchronized: 这个关键字对应的所有实现，比如获取锁、挂起等都是虚拟机内置的实现，锁，也叫做监视器，Monitor。 个人感觉，监视器比较合理，锁这个东西不太好理解，可能是还不习惯吧！
*********************************************************************
*/
public class SynchronizedDemo implements Runnable{
    static SynchronizedDemo instance = new SynchronizedDemo();
    static int i = 0;

    @Override
    public void run() {
        int j = 0;
        while (j++ < 100) {
            synchronized (instance) {
                i++;
            }
            System.out.println(Thread.currentThread().getName() + "----" + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //由于instance是类对象，所以相当于给你
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        //等待线程执行完成之后，再打印i的值
//        t1.join();
//        t2.join();
        System.out.println(i);
    }
}
