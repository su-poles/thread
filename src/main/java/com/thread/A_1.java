package com.thread;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:34 下午
*
*********************************************************************
*/
public class A_1 {
    public static int m = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];

        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                synchronized (A_1.class){
                    for(int j = 0; j < 1000; j++){
                        m++;
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println(m);
    }
}
