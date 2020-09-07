package com.aqs;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/5 5:48 下午
* 不加锁，实现100个线程分别进行1000次m++操作，会出现线程不安全的情况
 * 由于我的mac本比较牛逼，100个线程分别进行100次m++操作时，每次都正确，简直了，只能换成1000次m++操作了
*********************************************************************
*/
public class AQS_1 {
    public static int m = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];

        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                for(int j = 0; j < 1000; j++){
                    m++;
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
