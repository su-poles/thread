package com.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executors;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/13 8:04 下午
*
*********************************************************************
*/
public class Disruptor002 {
    //使用λ的方式写代码， 可以不用单独写消费者和工厂类，直接用λ方式指定
    public static void main(String[] args) {
        int ringBufferSize = 1024;
//        Disruptor<Message> disruptor = new Disruptor<>(Message::new, ringBufferSize, Executors.defaultThreadFactory());
        Disruptor<Message> disruptor = new Disruptor<>(Message::new, ringBufferSize, Executors.defaultThreadFactory(),
                ProducerType.MULTI, new BlockingWaitStrategy());

        //设置消费者
        disruptor.handleEventsWith((message, sequence, endOfBatch)->{
            System.out.println("[" + Thread.currentThread().getName() + "]消费消息" + message);
        });

        //启动。 一切准备就绪，就等生产者生产了。
        disruptor.start();


        //----------------------------------
        //生产消息

        //得到环形对象
        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent((message, sequence)->message.setMessage("一个参数的消息"));
        ringBuffer.publishEvent((message, sequence, v1)->message.setMessage(v1), "一个参数的消息_另一种方式");
        //多个参数
        ringBuffer.publishEvent((message, sequence, v1, v2)->message.setMessage(StringUtils.joinWith(",", v1, v2)), "消息1", "消息2");
        ringBuffer.publishEvent((message, sequence, v1, v2, v3)->message.setMessage(StringUtils.joinWith(",", v1, v2, v3)), "消息1", "消息2","消息3");
    }
}
