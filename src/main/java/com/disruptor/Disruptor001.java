package com.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/13 8:04 下午
*
*********************************************************************
*/
public class Disruptor001 {
    public static void main(String[] args) {
        MessageFactory messageFactory = new MessageFactory();
        int ringBufferSize = 1024;
        Disruptor<Message> disruptor = new Disruptor<Message>(messageFactory, ringBufferSize, Executors.defaultThreadFactory());

        //设置消费者，多个消费者表示对于同一个消息，多个消费者都要消费
//        disruptor.handleEventsWith(new MessageHandler());
        MessageHandler h1 = new MessageHandler();   //消费者1
        MessageHandler h2 = new MessageHandler();   //消费者2
        disruptor.handleEventsWith(h1, h2);

        //异常处理，如果消费者消费异常怎么办，需要重写下面三个方法
//        disruptor.handleExceptionsFor(h1).with(new ExceptionHandler<Message>() {
//            @Override
//            public void handleEventException(Throwable throwable, long l, Message message) {
//                //产生异常怎么办
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void handleOnStartException(Throwable throwable) {
//                //h1启动的时候出异常怎么办
//                System.out.println("启动时出现异常了...");
//            }
//
//            @Override
//            public void handleOnShutdownException(Throwable throwable) {
//                //shutdown的时候出异常怎么办
//                //TODO
//            }
//        });

        //启动。 一切准备就绪，就等生产者生产了。
        disruptor.start();

        //----------------------------------
        //上面消费者也设置好了，一切准备就绪了，现在生产一条消息看看运行效果

        //得到环形对象
        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        //获取下一个可用的序号（sequence)
        long sequence = ringBuffer.next();

        //获取序号对应的消息对象，并这是消息内容
        Message message = ringBuffer.get(sequence);
        message.setMessage("消息1");

        //发布消息
        ringBuffer.publish(sequence);
    }
}
