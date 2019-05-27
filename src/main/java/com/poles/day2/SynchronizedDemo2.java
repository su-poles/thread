package com.poles.day2;import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-20 11:38
* @desc 
*
*********************************************************************
*/
public class SynchronizedDemo2 implements Runnable{
    static SynchronizedDemo2 instance = new SynchronizedDemo2();
    static int i = 0;
    /*
    * 使用synchronized修饰对象中的实例方法，那么锁其实是放在当前对象的实例上，说白了，锁是放在instance这实例上
    *
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
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
