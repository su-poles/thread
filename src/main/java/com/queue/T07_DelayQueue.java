package com.queue;

import java.sql.Time;
import java.util.Queue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 2:30 下午
*
*********************************************************************
*/
public class T07_DelayQueue {
    DelayQueue<Task> queue = new DelayQueue<>();

    public static void main(String[] args) {
        T07_DelayQueue dq = new T07_DelayQueue();

        //往延时队列里添加内容
        long now = System.currentTimeMillis();
        dq.queue.put(new Task("t1", now + 1000));
        dq.queue.put(new Task("t2", now + 2000));
        dq.queue.put(new Task("t3", now + 1500));
        dq.queue.put(new Task("t4", now + 500));
        dq.queue.put(new Task("t5", now + 10000));
        dq.queue.put(new Task("t5", now + 7000));

        //打印队列里所有的任务
        System.out.println(dq.queue);


        //循环遍历执行任务，当时间到点之后，就可以take到任务，并开始执行
        while (!dq.queue.isEmpty()) {
            try {
                System.out.println(dq.queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Task implements Delayed{
        String name;
        long startRunningTime;

        public Task(String name, long startRunningTime){
            this.name = name;
            this.startRunningTime = startRunningTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            //给定一个时间段，并且给定时间段的单位为毫秒，转成以unit为单位的数值
            return unit.convert(startRunningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "Task{" + "name='" + name + '\'' + ", startRunningTime=" + startRunningTime + '}';
        }
    }
}
