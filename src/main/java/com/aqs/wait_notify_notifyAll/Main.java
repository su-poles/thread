package com.aqs.wait_notify_notifyAll;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/6 3:57 下午
*
*********************************************************************
*/
public class Main {
    private volatile boolean isFull = false;
    private volatile boolean isEmpty = false;

    private Buffer buffer = new Buffer();

    //生产者
    public void produce(){
        synchronized (this){
            while(isFull){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            buffer.add();
            this.notifyAll();
        }
    }

    //消费者
    public void consumer(){
        synchronized (this){
            while(isEmpty){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            buffer.remove();
            this.notifyAll();
        }
    }

    private class Buffer{
        public void add(){ }

        public void remove(){ }
    }
}
