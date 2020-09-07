package com.aqs;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:48 下午
* 在AQS_1的基础上，使用synchronize来实现m++线程安全的操作
*********************************************************************
*/
public class AQS_2 {
    public static int m = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];

        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                //加锁
                synchronized (AQS_2.class){
                    for(int j = 0; j < 10000; j++){
                        m++;
                    }
                }
            });
        }

        for (Thread t : threads){
            t.start();
        }

        for (Thread t : threads){
            t.join();
        }

        System.out.println(m);
    }
}
