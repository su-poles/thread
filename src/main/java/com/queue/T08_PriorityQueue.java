package com.queue;

import java.util.PriorityQueue;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 3:02 下午
* 可排序的队列
*********************************************************************
*/
public class T08_PriorityQueue {
    public static void main(String[] args) {
        PriorityQueue<String> priorityQueue = new PriorityQueue<>();

        priorityQueue.add("t");
        priorityQueue.add("c");
        priorityQueue.add("z");
        priorityQueue.add("a");
        priorityQueue.add("b");
        priorityQueue.add("f");

        while (!priorityQueue.isEmpty()) {
            System.out.println(priorityQueue.poll());
        }
    }
}
