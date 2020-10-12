package com.thread;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TransferQueue;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/20 12:35 上午
* 两个线程，线程1打印A~Z, 线程2打印1~26， 要求打印结果为A1B2C3....Y25Z26
*********************************************************************
*/
public class OrderedPrintCharacterAndInteger2 {
    public static void main(String[] args) {
        TransferQueue<String> transferQueue1 = new LinkedTransferQueue<>();
        TransferQueue<String> transferQueue2 = new LinkedTransferQueue<>();

        //线程1：打印A~Z, 每打印一次就停止，然后通知线程2打印
        new Thread(()->{
            for(int i = 'A'; i <= 'Z'; i++){
                try {
//                    transferQueue1.put(String.valueOf((char)i));   //用put也行？由于用了两个transferQueue，应该没问题，OrderedPrintCharacterAndInteger4中就必须用transfer,不能用put方法了

                    transferQueue1.transfer(String.valueOf((char)i));
                    System.out.println(transferQueue2.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //线程2：打印1~26, 每打印一次就停止，然后通知线程1打印
        new Thread(()->{
            for(int i = 1; i <= 26; i++){
                try {
                    System.out.println(transferQueue1.take());
                    transferQueue2.transfer(String.valueOf(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
