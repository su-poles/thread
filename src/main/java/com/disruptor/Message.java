package com.disruptor;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/13 8:05 下午
* Disruptor 中存储的自定义对象
*********************************************************************
*/
public class Message {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" + "message=" + message + '}';
    }
}
