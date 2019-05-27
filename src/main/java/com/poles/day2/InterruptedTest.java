package com.poles.day2;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-15 23:26
* @desc 
*
*********************************************************************
*/
public class InterruptedTest{
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new InterruptedThread());
        t1.start();
        /**
         * 我们来说说这个interrupt
         * interrupt 的意思是设置这个线程的中断标志位为0，非常类似于我常用的flag
         * 一般在线程执行体的第一行会判断：线程的中断标志位是否为0，如果是0，则退出，否则线程继续
         *
         * 目的是：在线程循环体里，一个线程如果正在处理某个业务，如果你使用interrupt去中断，则在下一次
         * 循环时会判断这个中断位，如果是true,则优雅的退出。
         *
         * 这里要说的是：如果使用了t1.stop()，表示线程里面终止，那么线程当前的操作可能还没有做完，只做了一半，可能会导致数据的不一致，所以线程的stop()已经不建议使用了
         */
        Thread.sleep(5000);
        t1.interrupt();
    }
}

class InterruptedThread implements Runnable{
    @Override
    public void run() {
//        while(true){
//            //do something
//            System.out.println(Math.random());
//            Thread.yield();
//        }

        while (true) {
            if(Thread.interrupted()){
                break;
            }
            //do something
            System.out.println(Math.random());
            Thread.yield();
        }
    }
}
