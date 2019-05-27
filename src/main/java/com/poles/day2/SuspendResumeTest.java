package com.poles.day2;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-16 08:21
* @desc 
*
*********************************************************************
*/
public class SuspendResumeTest {
    public static Object u = new Object();

    public static void main(String[] args) throws InterruptedException {
        ChangeObjectThread t1 = new ChangeObjectThread("t1");
        ChangeObjectThread t2 = new ChangeObjectThread("t2");

        t1.start();
        Thread.sleep(100);   //等待一下，确保t1处于挂起状态
        t2.start();

        t1.resume();  //线程t1继续执行
        t2.resume();  //线程t2继续执行

        t1.join();    //主线程等待t1执行完成
        t2.join();    //主线程等待t2执行完成
    }

    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name) {
            super.setName(name);
        }

        @Override
        public void run(){
            synchronized (u){
                System.out.println("in " + getName());
                Thread.currentThread().suspend();
            }
        }
    }
}

/**
 * Thread.suspend() 与  Thread.resume()都是过期方法，不推荐时间
 * Thread.suspend()  线程挂起，但不释放锁
 * Thread.resume()   线程继续执行，对挂起的线程有效
 *
 * 如果线程1获得了一把锁，然后线程1调用suspend()挂起，然后线程2调用线程1的resume()让线程1继续执行。 OK，没问题！
 * 如果线程1获得了一把锁，然后线程2调用了resume()方法让线程1继续执行，这对线程1没有任何影响，但是这时候线程1又调用了suspend()挂起，
 * 挂起后又不释放锁，就成了永久挂起，造成了死锁。不推荐使用，是因为某个线程调用resume()时无法保证这个动作一定是在suspend()之后。 NO，有问题！
 *
 * 请看示例代码，看main方法中的代码，第一主观感受是main方法一定能够执行结束，但实际上呢？
 *
 * 当我执行main方法时，发现主线程在等待t1或者t2的结束，因为程序一直在运行，并没有结束，那么接下里该怎么分析呢？且往下看：
 * 通过jps查看当前系统下的所有java进程，看到进程ID为7476
 * 然后通过jstack 7476来 dump出当前的线程， 发现t2还在这里，t1已经结束了。  这里看到的是所有的7476下的线程。
 * 仔细看t2线程的情况：
 *  "t2" #12 prio=5 os_prio=31 tid=0x00007fbbbe05a000 nid=0xa703 runnable [0x000070000e121000]
 *  java.lang.Thread.State: RUNNABLE                                    //当前线程的状态为：RUNNABLE   据说有点奇怪
 *  at java.lang.Thread.suspend0(Native Method)                         //第一行的at 表示该线程当前在干什么，它卡在suspend0上不动了
 *  at java.lang.Thread.suspend(Thread.java:1032)
 *  at com.poles.day2.SuspendResumeTest$ChangeObjectThread.run(SuspendResumeTest.java:39)
 *  - locked <0x000000076ac8cc48> (a java.lang.Object)
 *
 * 所以说suspend与resume只是为了向下兼容，平时不推荐使用，如果要用，请确保resume一定是在suspend之后执行
 *
 *
 [liyanlong@Pro 好好学习]$ jps
 7476 SuspendResumeTest
 7477 Launcher
 1099
 7485 Jps

 [liyanlong@Pro 好好学习]$ jstack 7476
 2019-05-16 08:44:13
 Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.152-b16 mixed mode):

 "Attach Listener" #13 daemon prio=9 os_prio=31 tid=0x00007fbbc1100000 nid=0xa907 waiting on condition [0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

 "t2" #12 prio=5 os_prio=31 tid=0x00007fbbbe05a000 nid=0xa703 runnable [0x000070000e121000]
 java.lang.Thread.State: RUNNABLE
 at java.lang.Thread.suspend0(Native Method)
 at java.lang.Thread.suspend(Thread.java:1032)
 at com.poles.day2.SuspendResumeTest$ChangeObjectThread.run(SuspendResumeTest.java:39)
 - locked <0x000000076ac8cc48> (a java.lang.Object)

 "Service Thread" #10 daemon prio=9 os_prio=31 tid=0x00007fbbc002e000 nid=0x3b03 runnable [0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

 "C1 CompilerThread3" #9 daemon prio=9 os_prio=31 tid=0x00007fbbbe017000 nid=0x3903 waiting on condition [0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

 "C2 CompilerThread2" #8 daemon prio=9 os_prio=31 tid=0x00007fbbbe016800 nid=0x3e03 waiting on condition [0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

 "C2 CompilerThread1" #7 daemon prio=9 os_prio=31 tid=0x00007fbbbe015800 nid=0x3703 waiting on condition [0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

 "C2 CompilerThread0" #6 daemon prio=9 os_prio=31 tid=0x00007fbbc002d800 nid=0x3f03 waiting on condition [0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

 "Monitor Ctrl-Break" #5 daemon prio=5 os_prio=31 tid=0x00007fbbc0023800 nid=0x3503 runnable [0x000070000d909000]
 java.lang.Thread.State: RUNNABLE
 at java.net.SocketInputStream.socketRead0(Native Method)
 at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
 at java.net.SocketInputStream.read(SocketInputStream.java:171)
 at java.net.SocketInputStream.read(SocketInputStream.java:141)
 at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
 at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
 at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
 - locked <0x000000076adce890> (a java.io.InputStreamReader)
 at java.io.InputStreamReader.read(InputStreamReader.java:184)
 at java.io.BufferedReader.fill(BufferedReader.java:161)
 at java.io.BufferedReader.readLine(BufferedReader.java:324)
 - locked <0x000000076adce890> (a java.io.InputStreamReader)
 at java.io.BufferedReader.readLine(BufferedReader.java:389)
 at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

 "Signal Dispatcher" #4 daemon prio=9 os_prio=31 tid=0x00007fbbc102b800 nid=0x3303 runnable [0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

 "Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007fbbc000d000 nid=0x2f03 in Object.wait() [0x000070000d703000]
 java.lang.Thread.State: WAITING (on object monitor)
 at java.lang.Object.wait(Native Method)
 - waiting on <0x000000076ab08ec8> (a java.lang.ref.ReferenceQueue$Lock)
 at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
 - locked <0x000000076ab08ec8> (a java.lang.ref.ReferenceQueue$Lock)
 at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
 at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

 "Reference Handler" #2 daemon prio=10 os_prio=31 tid=0x00007fbbbe005800 nid=0x4a03 in Object.wait() [0x000070000d600000]
 java.lang.Thread.State: WAITING (on object monitor)
 at java.lang.Object.wait(Native Method)
 - waiting on <0x000000076ab06b68> (a java.lang.ref.Reference$Lock)
 at java.lang.Object.wait(Object.java:502)
 at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
 - locked <0x000000076ab06b68> (a java.lang.ref.Reference$Lock)
 at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

 "main" #1 prio=5 os_prio=31 tid=0x00007fbbc1001800 nid=0x1803 in Object.wait() [0x000070000cbe2000]
 java.lang.Thread.State: WAITING (on object monitor)
 at java.lang.Object.wait(Native Method)
 - waiting on <0x000000076ac91d50> (a com.poles.day2.SuspendResumeTest$ChangeObjectThread)
 at java.lang.Thread.join(Thread.java:1252)
 - locked <0x000000076ac91d50> (a com.poles.day2.SuspendResumeTest$ChangeObjectThread)
 at java.lang.Thread.join(Thread.java:1326)
 at com.poles.day2.SuspendResumeTest.main(SuspendResumeTest.java:27)

 "VM Thread" os_prio=31 tid=0x00007fbbc0008800 nid=0x4c03 runnable

 "GC task thread#0 (ParallelGC)" os_prio=31 tid=0x00007fbbbe809800 nid=0x2007 runnable

 "GC task thread#1 (ParallelGC)" os_prio=31 tid=0x00007fbbbf801000 nid=0x1f03 runnable

 "GC task thread#2 (ParallelGC)" os_prio=31 tid=0x00007fbbbe80a000 nid=0x5403 runnable

 "GC task thread#3 (ParallelGC)" os_prio=31 tid=0x00007fbbbe001800 nid=0x5203 runnable

 "GC task thread#4 (ParallelGC)" os_prio=31 tid=0x00007fbbc0809800 nid=0x5103 runnable

 "GC task thread#5 (ParallelGC)" os_prio=31 tid=0x00007fbbbe80b000 nid=0x2c03 runnable

 "GC task thread#6 (ParallelGC)" os_prio=31 tid=0x00007fbbc0007000 nid=0x4f03 runnable

 "GC task thread#7 (ParallelGC)" os_prio=31 tid=0x00007fbbc0008000 nid=0x4d03 runnable

 "VM Periodic Task Thread" os_prio=31 tid=0x00007fbbc0032000 nid=0x5503 waiting on condition

 JNI global references: 33

 [liyanlong@Pro 好好学习]$

 */