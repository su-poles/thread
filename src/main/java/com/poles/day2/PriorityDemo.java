package com.poles.day2;

import javafx.scene.layout.Priority;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-20 11:09
* @desc 
*
*********************************************************************
*/
public class PriorityDemo {
    public static class HighPriority implements Runnable {
        public static int count = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (PriorityDemo.class) {
                    count++;
                    if (count > 10000000) {
                        System.out.println("HighPriority is complete!");
                        break;
                    }
                }
            }
        }
    }

    public static class LowPriority implements Runnable {
        public static int count = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (PriorityDemo.class) {
                    count++;
                    if (count > 10000000) {
                        System.out.println("LowPriority is complete!");
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new HighPriority());
        Thread t2 = new Thread(new LowPriority());
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t2.start();
        t1.start();

        /**
         * 设置优先级，只能说增加优先执行的概率，不能确保都是有限执行
         */
    }

}
