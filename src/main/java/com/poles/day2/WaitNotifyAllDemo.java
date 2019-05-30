package com.poles.day2;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-20 19:31
* @desc 
*
*********************************************************************
*/
public class WaitNotifyAllDemo {
    final static Object object = new Object();

    public static class T1 implements Runnable {

        @Override
        public void run() {
            synchronized (object) {
                System.out.println("T1 start! wait on object");
                try {
                    object.wait();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T1 end! ");
            }
        }
    }

    public static class T2 implements Runnable {

        @Override
        public void run() {
            synchronized (object) {
                System.out.println("T2 start! notify all threads");
                object.notifyAll();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println( "T2 end! ");
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new T1());
        Thread t1_1 = new Thread(new T1());
        t1_1.start();
        t1.start();

        Thread.sleep(1000);
        Thread t2 = new Thread(new T2());
        t2.start();
    }

    /**
     * 这次我写简单点：
     * t1与t1_1开始启动，然后各自进入wait状态，第二个T1线程释放所之后，然后t2开始启动，然后就执行到notifyAll,休息2秒，T2线程释放锁
     * t1与t1_1竞争锁，获取到锁的一方，返回wait()处开始往下执行，等待1秒后执行结束，释放锁
     * 剩下的一个T1线程获取到锁，然后返回wait()处开始往下执行，等待1秒之后，执行结束，释放锁
     */
}
