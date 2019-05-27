package com.poles.day4;

import java.util.concurrent.atomic.AtomicReference;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-22 17:07
* @desc AtomicReference 针对引用类型的CAS操作， 但是这玩意儿解决不了ABA的问题， ABA的问题可以通过AtomicStampedReference来解决
 * 即在做CAS操作时，既比较原来的值是否相等，还比较有个戳是否相等。这个戳就是乐观锁里的version， 每次+1就可以了
*
*********************************************************************
*/
public class AtomicReferenceDemo {
    //初始化一个atomic的字符，字符串的值为abc
    private final static AtomicReference<String> atomicStr = new AtomicReference<>("abc");

    //然后我开10个线程同时将abc修改为bcd, 只会有一个线程修改成功，其它线程都失败
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run(){
                    try {
                        Thread.sleep(Math.abs((int)(Math.random() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    boolean success = atomicStr.compareAndSet("abc", "bcd");
                    if(success){
                        System.out.println("Thread:" + Thread.currentThread().getId() + " Change value Success!");
                    }else {
                        System.out.println("Thread:" + Thread.currentThread().getId() + " Change value FAILURE!");
                    }
                }
            }.start();
        }
    }
}
