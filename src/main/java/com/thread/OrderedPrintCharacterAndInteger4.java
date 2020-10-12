package com.thread;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/20 12:35 上午
* 两个线程，线程1打印A~Z, 线程2打印1~26， 要求打印结果为A1B2C3....Y25Z26
*********************************************************************
*/
public class OrderedPrintCharacterAndInteger4 {
    public static void main(String[] args) {
        TransferQueue<String> transferQueue = new LinkedTransferQueue<>();

        //线程1：打印A~Z, 每打印一次就停止，然后通知线程2打印
        new Thread(()->{
            for(int i = 'A'; i <= 'Z'; i++){
                try {

                    //使用一个transferQueue实现，则这里必须要用transfer，不能用put， OrderedPrintCharacterAndInteger2中应该可以用put
                    transferQueue.transfer(String.valueOf((char)i));
                    System.out.println(transferQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //线程2：打印1~26, 每打印一次就停止，然后通知线程1打印
        new Thread(()->{
            for(int i = 1; i <= 26; i++){
                try {
                    System.out.println(transferQueue.take());
                    transferQueue.transfer(String.valueOf(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
