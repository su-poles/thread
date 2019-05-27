package com.poles.day5;

import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-25 17:27
* @desc 
* ReenterLock:
 * 可重入：单线程可以重复进入，但要重复退出
 * 可中断：
 * 可限时：
 * 公平锁：
*********************************************************************
*/
public class ReentrantlockDemo implements Runnable{
    public static ReentrantLock lock = new ReentrantLock();
    public static int count = 0;

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            //重入锁的特点，加锁加了几次，那么一定要解除锁几次，因为每次加锁，重入锁锁的许可数量就+1，两次加锁，这个线程获取的重入锁的许可数量就是2，后面就一定要解锁2次
            lock.lock();
            lock.lock();
            try {
                count++;
            }finally {
                //这个手动释放的，释放的地方和时间都是人为控制的，如果使用Synchronized，那么就不能控制其释放时机
                lock.unlock();
                //lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantlockDemo demo = new ReentrantlockDemo();
        Thread t1 = new Thread(demo);
        Thread t2 = new Thread(demo);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }

    /**
     * 当程序卡住之后，怎么看呢？
     * [liyanlong@Pro 好好学习]$ jps
     * 31955
     * 33302 KotlinCompileDaemon
     * 33307 Launcher
     * 33308 ReentrantlockDemo
     * 33309 Jps
     * [liyanlong@Pro 好好学习]$
     * [liyanlong@Pro 好好学习]$ jstack 33308
     * 2019-05-25 23:20:47
     * Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.152-b16 mixed mode):
     *
     * "Attach Listener" #13 daemon prio=9 os_prio=31 tid=0x00007fd528064800 nid=0x5607 waiting on condition [0x0000000000000000]
     *    java.lang.Thread.State: RUNNABLE
     *
     * "Thread-1" #12 prio=5 os_prio=31 tid=0x00007fd528007800 nid=0xa803 waiting on condition [0x00007000058ec000]
     *    java.lang.Thread.State: WAITING (parking)
     *         at sun.misc.Unsafe.park(Native Method)
     *         - parking to wait for  <0x000000076ac944d8> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
     *         at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
     *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
     *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
     *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
     *         at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
     *         at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
     *         at com.poles.day5.ReentrantlockDemo.run(ReentrantlockDemo.java:26)
     *         at java.lang.Thread.run(Thread.java:748)
     *
     * "Service Thread" #10 daemon prio=9 os_prio=31 tid=0x00007fd52b032800 nid=0x3c03 runnable [0x0000000000000000]
     *    java.lang.Thread.State: RUNNABLE
     *
     * "C1 CompilerThread3" #9 daemon prio=9 os_prio=31 tid=0x00007fd52b012000 nid=0x3f03 waiting on condition [0x0000000000000000]
     *    java.lang.Thread.State: RUNNABLE
     *
     * "C2 CompilerThread2" #8 daemon prio=9 os_prio=31 tid=0x00007fd52b011000 nid=0x4003 waiting on condition [0x0000000000000000]
     *    java.lang.Thread.State: RUNNABLE
     *
     * "C2 CompilerThread1" #7 daemon prio=9 os_prio=31 tid=0x00007fd52b010800 nid=0x3a03 waiting on condition [0x0000000000000000]
     *    java.lang.Thread.State: RUNNABLE
     *
     * "C2 CompilerThread0" #6 daemon prio=9 os_prio=31 tid=0x00007fd52b025800 nid=0x3803 waiting on condition [0x0000000000000000]
     *    java.lang.Thread.State: RUNNABLE
     *
     * "Monitor Ctrl-Break" #5 daemon prio=5 os_prio=31 tid=0x00007fd52b02e800 nid=0x3703 runnable [0x00007000050d4000]
     *    java.lang.Thread.State: RUNNABLE
     *         at java.net.SocketInputStream.socketRead0(Native Method)
     *         at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
     *         at java.net.SocketInputStream.read(SocketInputStream.java:171)
     *         at java.net.SocketInputStream.read(SocketInputStream.java:141)
     *         at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
     *         at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
     *         at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
     *         - locked <0x000000076adcc2b8> (a java.io.InputStreamReader)
     *         at java.io.InputStreamReader.read(InputStreamReader.java:184)
     *         at java.io.BufferedReader.fill(BufferedReader.java:161)
     *         at java.io.BufferedReader.readLine(BufferedReader.java:324)
     *         - locked <0x000000076adcc2b8> (a java.io.InputStreamReader)
     *         at java.io.BufferedReader.readLine(BufferedReader.java:389)
     *         at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)
     *
     * "Signal Dispatcher" #4 daemon prio=9 os_prio=31 tid=0x00007fd529022000 nid=0x4403 runnable [0x0000000000000000]
     *    java.lang.Thread.State: RUNNABLE
     *
     * "Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007fd52a802000 nid=0x4c03 in Object.wait() [0x0000700004ece000]
     *    java.lang.Thread.State: WAITING (on object monitor)
     *         at java.lang.Object.wait(Native Method)
     *         - waiting on <0x000000076ab08ec8> (a java.lang.ref.ReferenceQueue$Lock)
     *         at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
     *         - locked <0x000000076ab08ec8> (a java.lang.ref.ReferenceQueue$Lock)
     *         at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
     *         at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)
     *
     * "Reference Handler" #2 daemon prio=10 os_prio=31 tid=0x00007fd528807000 nid=0x3203 in Object.wait() [0x0000700004dcb000]
     *    java.lang.Thread.State: WAITING (on object monitor)
     *         at java.lang.Object.wait(Native Method)
     *         - waiting on <0x000000076ab06b68> (a java.lang.ref.Reference$Lock)
     *         at java.lang.Object.wait(Object.java:502)
     *         at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
     *         - locked <0x000000076ab06b68> (a java.lang.ref.Reference$Lock)
     *         at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)
     *
     * "main" #1 prio=5 os_prio=31 tid=0x00007fd52a800800 nid=0x2503 in Object.wait() [0x00007000043ad000]
     *    java.lang.Thread.State: WAITING (on object monitor)
     *         at java.lang.Object.wait(Native Method)
     *         - waiting on <0x000000076ac94798> (a java.lang.Thread)
     *         at java.lang.Thread.join(Thread.java:1252)
     *         - locked <0x000000076ac94798> (a java.lang.Thread)
     *         at java.lang.Thread.join(Thread.java:1326)
     *         at com.poles.day5.ReentrantlockDemo.main(ReentrantlockDemo.java:45)
     *
     * "VM Thread" os_prio=31 tid=0x00007fd52901d000 nid=0x3003 runnable
     *
     * "GC task thread#0 (ParallelGC)" os_prio=31 tid=0x00007fd529014000 nid=0x1b07 runnable
     *
     * "GC task thread#1 (ParallelGC)" os_prio=31 tid=0x00007fd52b000000 nid=0x2a03 runnable
     *
     * "GC task thread#2 (ParallelGC)" os_prio=31 tid=0x00007fd528001000 nid=0x5303 runnable
     *
     * "GC task thread#3 (ParallelGC)" os_prio=31 tid=0x00007fd528002000 nid=0x5103 runnable
     *
     * "GC task thread#4 (ParallelGC)" os_prio=31 tid=0x00007fd528002800 nid=0x2b03 runnable
     *
     * "GC task thread#5 (ParallelGC)" os_prio=31 tid=0x00007fd52b001000 nid=0x4e03 runnable
     *
     * "GC task thread#6 (ParallelGC)" os_prio=31 tid=0x00007fd528800800 nid=0x4d03 runnable
     *
     * "GC task thread#7 (ParallelGC)" os_prio=31 tid=0x00007fd52b001800 nid=0x2e03 runnable
     *
     * "VM Periodic Task Thread" os_prio=31 tid=0x00007fd52b033000 nid=0x5503 waiting on condition
     *
     * JNI global references: 18
     */

    /**
     * 其中有这么一句：at com.poles.day5.ReentrantlockDemo.run(ReentrantlockDemo.java:26)
     * 然后这个线程时处于waiting(parking)状态，所以卡就卡在了ReentrantlockDemo.java:26行
     */
}