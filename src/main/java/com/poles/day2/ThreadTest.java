package com.poles.day2;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-05 20:35
* @desc 
*
*********************************************************************
*/
public class ThreadTest  {
    public static void main(String[] args) {
        Thread t1 = new Thread(new CreateThread3()){
            @Override
            public void run() {
                System.out.println("Hello, I am t1!");
            }
        };
        //t1.start()的意思是开启一个新的线程（注意：此处会有两个线程，一个是主线程，一个是新创建的线程）
        t1.start();
        /*
         * t2.run()的意思是调用t1对象的run()方法。如果在new Thread的时候，传入了一个Runnable对象，即Thread t1 = new Thread(new CreateThread3())，
         * 那么run方法就会被传入的target的执行体所覆盖，如果没有传入，那么target==null,那么就什么都不做
         *
         * 但是，在本示例中，在new Thread对象时，虽然没有传入任何的Runnable对象，但是这里也重写了run方法体
         *
         * 思考题：如果new一个线程时既传入Runnable对象，又重写了run方法，到底会保留那个run方法体呢？
         */
        t1.run();
    }
}

class CreateThread3 implements Runnable{

    @Override
    public void run() {
        System.out.println("Hello, I am t3");
    }
}
