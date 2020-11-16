package com.disruptor;

import com.lmax.disruptor.EventHandler;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/13 8:22 下午
* 自定义消费者类
*********************************************************************
*/
public class MessageHandler implements EventHandler<Message> {
    public static long count;

    @Override
    public void onEvent(Message message, long sequence, boolean endOfBatch) throws Exception {
        count++;
        System.out.println("[" + Thread.currentThread().getName() + "]消费消息" + message + ", 消息序号" + count);
    }
}
