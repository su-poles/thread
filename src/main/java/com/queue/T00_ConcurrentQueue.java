package com.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/24 12:54 下午
* 模拟一个买票的过程，原理就是redis的队列
*********************************************************************
*/
public class T00_ConcurrentQueue {
    static Queue<String> queue = new ConcurrentLinkedQueue<>();

    static {
        for(int i = 0; i < 1000; i++){
            queue.add("票号：" + i);
        }
    }

    public static void main(String[] args) {
        //开10个线程卖票
        for(int i = 0; i < 10; i++){
            new Thread(()->{
                while(true){
                    String ticket = queue.poll();
                    if(ticket == null){
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + " 售卖票号：" + ticket);
                }
            }, "线程" + i).start();
        }
    }
}
