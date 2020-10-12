package com.queue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 3:06 下午
*
*********************************************************************
*/
public class T09_TransferQueue {
    public static void main(String[] args) {
        AtomicInteger a = new AtomicInteger(65);
        LinkedTransferQueue<String> linkedTransferQueue = new LinkedTransferQueue<>();

        //消费者
        new Thread(()->{
            while(true){
                try {
                    System.out.println(linkedTransferQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //生产者
        new Thread(()->{
            while(true){
                try {
                    //put, 只要put结束就不管了，如果队列满了才会阻塞，但是LinkedTransferQueue是个无界队列
                    //synchronousQueue可以看做一个有界队列，但其容量为0，只能传递，不能存储任何东西，对synchronousQueue是用put可以看做等价于transferQueue的transfer方法
                    linkedTransferQueue.put(String.valueOf((char)a.getAndIncrement()));

                    //transfer是个阻塞方法，必须要等待添加的内容被消费者take之后才会结束
                    linkedTransferQueue.transfer(String.valueOf((char)a.getAndIncrement()));
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
