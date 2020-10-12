package com.queue;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/9 2:58 下午
*
*********************************************************************
*/
public class T100_SimpleQueue {
    public static void main(String[] args) {
        //普通队列
//        commonQueueDemo();

        //双端队列，两端都可以添加和获取元素
//        dequeueDemo();

        //栈（Stack已经过时，Java官方推荐使用Deque代替Stack）
        stackDemo();
    }

    //普通队列，FIFO
    private static void commonQueueDemo() {
//        Queue<String> queue = new LinkedList<>();
        Deque<String> deque = new LinkedList<>();

        //FIFO
        deque.offer("a");
        deque.offer("b");
        deque.offer("c");

        while(!deque.isEmpty()){
            System.out.println(deque.poll());
        }
    }

    //双端队列
    private static void dequeueDemo() {
        Deque<String> deque = new LinkedList<>();

        //尾部插入
        deque.offer("a");
        deque.offer("b");
        deque.offer("c");
        deque.offerLast("last");

        //队列头部插入
        deque.offerFirst("head");

        //获取头部元素
        System.out.println("头部元素：" + deque.peekFirst());
        //获取尾部元素
        System.out.println("尾部元素：" + deque.peekLast());

        while(!deque.isEmpty()){
            System.out.println(deque.poll());
        }
    }

    private static void stackDemo() {
        Deque<String> deque = new LinkedList<>();

        deque.push("a");
        deque.push("b");
        deque.push("c");

        while(!deque.isEmpty()){
            System.out.println(deque.pop());
        }
    }
}
