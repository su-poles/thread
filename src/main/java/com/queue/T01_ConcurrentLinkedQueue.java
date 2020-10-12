package com.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/25 10:13 下午
*
*********************************************************************
*/
public class T01_ConcurrentLinkedQueue {
    public static void main(String[] args) {
        Queue<String> strs = new ConcurrentLinkedQueue<>();

        //获取元素：
        String a = strs.peek();

        System.out.println("a = " + a);

        String b = strs.poll();
        System.out.println("b = " + a);

//        String c = strs.element();                //java.util.NoSuchElementException

        for (int i = 0; i < 10; i++) {
//            strs.add("add_" + i);                 //往队列里塞入值，如果队列满了，塞入失败，则抛异常
            strs.offer("str_" + i);            //往队列里塞入值，添加成功返回true, 如果队列满了，加不进去返回false
        }

        System.out.println("打印队列：" + strs);
        System.out.println("队列长度" + strs.size());

        System.out.println(strs.poll());                        //取第一个值，并且remove掉
        System.out.println("队列长度" + strs.size());

        System.out.println(strs.peek());                        //只取，不remove
        System.out.println("队列长度" + strs.size());


        //队列三个查看首个元素的三个方法：
        //1. element, 队列为空就抛异常NoSuchElementException, 否则返回首个元素，并从队列里移除
        //2. poll, 返回队首元素，并删除队首元素，如果队列为空，则返回null
        //3. peek, 返回队首元素，不删除元素。如果队列为空，则返回null
    }
}
