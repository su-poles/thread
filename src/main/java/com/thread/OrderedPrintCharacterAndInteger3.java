package com.thread;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TransferQueue;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/20 12:35 上午
* 两个线程，线程1打印A~Z, 线程2打印1~26， 要求打印结果为A1B2C3....Y25Z26
*********************************************************************
*/
public class OrderedPrintCharacterAndInteger3 {
    public static void main(String[] args) {
        SynchronousQueue<String> synchronousQueue1 = new SynchronousQueue<>();
        SynchronousQueue<String> synchronousQueue2 = new SynchronousQueue<>();

        //线程1：打印A~Z, 每打印一次就停止，然后通知线程2打印
        new Thread(()->{
            for(int i = 'A'; i <= 'Z'; i++){
                try {
                    synchronousQueue1.put(String.valueOf((char)i));     //put，阻塞等待take
                    System.out.println(synchronousQueue2.take());       //上面被take后，这里阻塞等待2中的内容
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //线程2：打印1~26, 每打印一次就停止，然后通知线程1打印
        new Thread(()->{
            for(int i = 1; i <= 26; i++){
                try {
                    System.out.println(synchronousQueue1.take());      //上来就take, 显然会阻塞等待
                    synchronousQueue2.put(String.valueOf(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
