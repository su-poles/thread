package com.queue;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.*;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/9 2:58 下午
*
*********************************************************************
*/
public class T101_BlockingQueue {
    public static void main(String[] args) {
        //无界队列，最大为Integer.MAX_VALUE, 即0x7fffffff， 0111 1111 1111 1111 1111 1111 1111 1111， 即2^32 - 1
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);       //有界队列

        new Thread(()->{
            while (true){
                try {
                    blockingQueue.put(UUID.randomUUID().toString());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(()->{
            while (true){
                try {
                    System.out.println(blockingQueue.take());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}