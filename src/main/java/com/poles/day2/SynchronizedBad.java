package com.poles.day2;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-20 14:20
* @desc 
*
*********************************************************************
*/
public class SynchronizedBad implements Runnable{
//    static SynchronizedDemo2 instance = new SynchronizedDemo2();
    static int i = 0;
    /*
     * 使用synchronized修饰对象中的实例方法，那么锁其实是放在当前对象的实例上，说白了，锁是放在instance这实例上
     * */
    public synchronized void increase(){
        i++;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000000; i++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread t1 = new Thread(instance);
//        Thread t2 = new Thread(instance);
//        SynchronizedBad synchronizedBad = new SynchronizedBad();
//        Thread t1 = new Thread(synchronizedBad);
//        Thread t2 = new Thread(synchronizedBad);
        Thread t1 = new Thread(new SynchronizedBad());
        Thread t2 = new Thread(new SynchronizedBad());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

    /**
     * 该类中，synchronized修饰了increase实例方法，但是，在main方法中创建线程时，使用的锁不是同一把锁，而是new了两把不同的锁，所以这里的synchronized是无效的
     */
}
