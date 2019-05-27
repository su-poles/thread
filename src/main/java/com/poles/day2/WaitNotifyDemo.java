package com.poles.day2;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-20 14:26
* @desc
 * 一定要注意一点：这个object.wait和object.notify必须要在object获取到锁以后才能使用，如果一上来就调用object.wait就会报错：java.lang.IllegalMonitorStateException
 * 换句话说，object.wait就得在同步块或者锁块里执行, 同样的，object.notify也是必须是拿到object这把锁之后才能执行
 *
 * 一句话：wait() 与 notify() 谁能够调用，就是拥有这个对象监视器的对象才可以调用
 * This mothod should only be called by a thread that is the owner of this object's monitor.
 * 请查看Object类的wart/notify方法的解释。
 *
 * 另一点一定要注意：object.wait 会释放当前锁，否则会造成其他竞争这把所得线程都处于阻塞状态
 *
 * The current thread must own this object's monitor. The thread
 * releases ownership of this monitor and waits until another thread
 * notifies threads waiting on this object's monitor to wake up
 * either throught a call to the notify method or the notifyAll method.
 *
 * 我自己的翻译：当前线程必须拥有这对象的锁。 这个线程会释放这个所得所有权并且处于等待状态，
 * 直到另一个线程通过调用notify或者notifyAll方法来通知并唤醒等待在这个monitor上的所有线程
**********************************************************************
*/
public class WaitNotifyDemo {

    final static Object object = new Object();

    public static class T1 implements Runnable {

        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ":T1 start!");
                try {
                    System.out.println(System.currentTimeMillis() + ":T1 wait for object...");
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis()+":T1 end! ");
            }
        }
    }

    public static class T2 implements Runnable {

        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ":T2 start!");
                System.out.println(System.currentTimeMillis() + ":T2 wait for object...");
                object.notify();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis()+":T2 end! ");
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new T1());
        Thread t2 = new Thread(new T2());
        t1.start();
        t2.start();
    }

    /**
     * 解释一下这段代码，在看这段代码之前，先看该类最上面的注释，先明白原理：
     *
     * 好，t1启动，t2启动
     * 两个线程开始竞争：如果t2先获取到锁，t2优先执行到object.notify()， 然后t1再执行到object.wait()， 那就惨了，那么就t1就会永远等下去
     *
     * 如果t1先拿到锁，然后执行到wait(), 然后释放锁，那么t2就会拿到锁，t2执行到notify, 此时，注意，此时t2还没有释放锁，因为同步块代码还没有执行完
     *
     * 那么t1线程想接着wait往下执行，前提是得先获取到object这把锁。 因此得等到t2中的同步块执行完成，等那个3000毫秒执行完成，T2 end打印完成之后，t1才能接着wait后面的部分执行。
     *
     * 如果有很多个T1线程，那么就说会有多个线程等到在object这把锁上面，T2线程执行了notify()之后，会随机唤醒一个，随机唤醒一个，随机唤醒一个线程，
     * 等T2执行完释放锁之后，这个被唤醒的线程竞争到锁之后才能往下继续执行。
     *
     * 如果T2使用的是object.notifyAll() 则会唤醒等待在object这把锁上的所有线程， 可以看一下WaitNotifyAllDemo
     *
     */
}
