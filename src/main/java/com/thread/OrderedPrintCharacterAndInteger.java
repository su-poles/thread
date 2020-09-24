package com.thread;

import java.util.concurrent.Semaphore;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/20 12:35 上午
* 两个线程，线程1打印A~Z, 线程2打印1~26， 要求打印结果为A1B2C3....Y25Z26
*********************************************************************
*/
public class OrderedPrintCharacterAndInteger {
    public static void main(String[] args) {
        Semaphore s1 = new Semaphore(1);    //线程1优先打印
        Semaphore s2 = new Semaphore(0);

        //线程1：打印A~Z, 每打印一次就停止，然后通知线程2打印
        new Thread(()->{
            for(int i = 'A'; i <= 'Z'; i++){
                try {
                    s1.acquire();                           //s1获得许可，第一次时由于许可已经是1了，所以'A'可以直接打印
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print((char)i);                  //打印
                s2.release();                               //然后，s2可以继续了，此时s1的下一个循环因为得不到许可而处于阻塞状态
            }
        }).start();

        //线程2：打印1~26, 每打印一次就停止，然后通知线程1打印
        new Thread(()->{
            for(int i = 1; i <= 26; i++){
                try {
                    s2.acquire();                       //s2，得到许可之后开始打印， 第一次时，本身就没有许可数量，所以处于阻塞状态，只有线程1给一个许可之后，线程2才可以继续
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print(i);
                s1.release();                           //打印完成后，放行s1, 让s1继续， 而s2的下一循环因为得不到许可处于阻塞状态
            }
        }).start();
    }
}
