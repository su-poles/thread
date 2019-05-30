package com.poles.day5;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-27 15:49
* @desc 循环栅栏
*
* 对于CountDownLatch来说，倒计时只有一次，倒计时结束，主线程开始执行，然后执行结束，然后就没有人然后了
* 对于CyclicBarrier 循环栅栏来说，可以理解为CountDownLatch的循环操作。 来第一批10个线程，倒计时结束，主线程开始执行，然后执行结束；第二批又10个线程，然后又倒计时结束，主线程开始执行，然后执行结束...s
*
* Cyclic意为栅栏，也就是说这个计数器可以反复使用。比如，假设我们将计数器设置为10（parties），那么凑齐第一批10个线程后，计数器就会归零，然后接着凑下一批10个线程
* 两种理解方式，本质一模一样，仔细想想
* public Cyclicbarrier(int parties, Runnable barrierAction)
 *  当所有的parties都到达这个栅栏之后，就开始执行barrierAction
 *
 *  ---->               |
 *      ------>         |
 *   ----->             |
 *    ------------>     |
 *  -------->           |
 *                    栅栏，barrierAction
 *  比如我们要开始做早操，然后每个parties就是每个士兵，当第一班的士兵都列队完成之后，开始做操（barrierAction），然后开始集合第二班的士兵...
*********************************************************************
*/
public class CyclicBarrierDemo{
    private static class Soldier implements Runnable {
        private String soldierName;
        private final CyclicBarrier cyclicBarrier;

        Soldier(String soldierName, CyclicBarrier cyclicBarrier) {
            this.soldierName = soldierName;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                soldiersGather();
                //等待所有士兵到齐,到齐之后才能执行任务
                cyclicBarrier.await();
                doWork();
                //等待所有士兵任务都完成，这里复用了cyclicBarrier
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        //士兵集合
        private void soldiersGather() throws InterruptedException {
            //Thread.sleep(Math.abs(new Random().nextInt() % 10000));
            System.out.println("\t" + soldierName + "完成列队！");
        }

        //士兵出任务
        private void doWork() throws InterruptedException {
            Thread.sleep(Math.abs(new Random().nextInt() % 10000));
            System.out.println("\t" + soldierName + "完成工作！");
        }
    }

    public static class BarrierAction implements Runnable {
        boolean flag;
        int N;

        public BarrierAction(boolean flag, int N) {
            this.flag = flag;
            this.N = N;
        }
        @Override
        public void run() {
            if (flag) {
                System.out.println("班长：【士兵" + N + "个， 任务完成！】");
            }else {
                System.out.println("班长：【士兵" + N + "个， 集合完毕！】");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;
        Thread[] allSoldier = new Thread[N];
        boolean flag = false;
        //创建CyclicBarrier，这个对象自始至终都只有一个，因为其是个final的变量，所以只能被赋值一次
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N, new BarrierAction(flag, N));
        //设置屏障点，主要是为了执行这个方法
        System.out.println("班长：集合！！！");
        for (int i = 0; i < N; i++) {
//            System.out.println("\t我是士兵" + i);
            //每个士兵开一个线程
            allSoldier[i] = new Thread(new Soldier(("士兵" + i), cyclicBarrier));
            allSoldier[i].start();

            /**
             * 说明一下，cyclicBarrier.await() 会抛出一个InterruptedException
             * 防止因为某些线程除了问题，导致永久性等待，一旦出现等待时间过程，我们会通过一些辅助方式来唤醒这些处于await状态中的线程
             *
             * 同时还抛出：BrokenBarrierException， 表示这个栅栏坏掉了。
             * 举个例子，比如该示例中，必须要等10个士兵都列队完毕，CyclicBarrier才能开始执行任务，但是如果其中一个士兵线程坏掉了，
             * 那么就永远不会有10个线程士兵都踩到这个栅栏上去，如果出现这种情况，那么其它线程就会抛出BrokenBarrierException
             *             if (i == 5) {
             *                 allSoldier[0].interrupt();
             *             }
             * 增加如上代码，第一个线程就会抛出  InterruptedException， 而其它线程会抛出   BrokenBarrierException
             * 为了避免发出中断信号时，soldiersGather方法中的sleep抛出InterruptedException，我把soldiersGather中的sleep这一行也注释掉
             *   //Thread.sleep(Math.abs(new Random().nextInt() % 10000));
             *
             * 这么理解：每个士兵都开了新的线程去执行，开始执行run方法体中的代码
             * 然后每个线程到达栅栏处就开始等待，即CyclicBarrier.await()处等待，结果某个线程中断了，那么凡是到达栅栏出的线程都会抛出BrokenBarrierException
             * 那个中断的线程就会抛出：
             */

//              if (i == 5) {
//                  allSoldier[0].interrupt();
//              }

        }
    }
}
