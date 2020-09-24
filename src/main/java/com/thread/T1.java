package com.thread;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/16 11:36 上午
*
*********************************************************************
*/
public class T1 implements Runnable {

    private /*volatile*/ int count = 100;

    @Override
    public /*synchronized*/ void run() {            //synchronized既保证了原子性，又保证了可见性，所以count不要加volatile
        count--;
        System.out.println(Thread.currentThread().getName() + ": count = " + count);
    }

    public static void main(String[] args) {
        T1 t = new T1();
        for(int i = 0; i < 100; i++){
            new Thread(t, "Thread-" + i).start();
        }
    }
}
