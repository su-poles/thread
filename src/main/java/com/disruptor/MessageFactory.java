package com.disruptor;

import com.lmax.disruptor.EventFactory;
/**
*********************************************************************
* 
* @author poles
* @date 2020/10/13 8:08 下午
*
 * Disruptor中存储的自定义对象的生成工厂
 * 这里涉及到一个性能问题：
 * 假设Disruptor的长度为1024，new Disrutptor是会传入这个工厂类
 * 那么当初始化disruptor时，其中的Message(自定义存储对象)也会被new出来，初始化完成
 * 后面更新对象时，不需要再new Message了，只需要通过setMessage设置其中的属性值即可
*********************************************************************
*/
public class MessageFactory implements EventFactory<Message> {

    @Override
    public Message newInstance() {
        //只需要new 一个对象，里面的属性通过set的方式设置
        //由于更新对象只需要更新属性，不需要每次都new，会降低gc的频率，从而提高性能
        return new Message();
    }
}
