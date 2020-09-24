package com.thread;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/16 11:44 上午
*
*********************************************************************
*/
public class T2 {
    public synchronized void m1(){
        System.out.println(Thread.currentThread().getName() + " m1 start...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m1 end!");
    }

    public void m2(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " m2");
    }

    public static void main(String[] args) {
        T2 t = new T2();
        new Thread(t::m1, "thread-t1").start();
        new Thread(t::m2, "thread-t2").start();
    }
}
