package com.aqs.mylock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/6 2:14 下午
 *
*********************************************************************
*/
public class MylockB implements Lock {
    private volatile int i = 0;

    @Override
    public void lock() {
        synchronized (this){
            //这个地方为什么用while ？ 而不是if?  请自己思考，然后文章最后的解释。
            while(i != 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i = 1;
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        //释放锁时，由于synchronized是可重入锁，所以自己可以进来，别的线程是拿不到这个锁的，只有自己可以解锁，也就是i=0
        synchronized (this){
            i = 0;
            this.notifyAll();   //相当于一个非公平锁
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }


    /*
     下面这段代码中，为什么要用while而不是if呢？
     public void lock() {
        synchronized (this){
            while/if(i != 0) {
                try {
                     // A 点
                    this.wait();
                    // B 点
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i = 1;
        }
    }

    为了说清楚，这个道理，代码稍微改一下，不然上面的例子看不太清楚，本来给i=1赋值的线程只能1个，但是如果两个线程同时对i赋值为1，你也看不出来，还以为是正确的。
    咱们把给i=1赋值，换成给i++，如果超过i的值1，就一眼看出错了，这里以消费者和生产者来举例

     public void producer() {
        synchronized (this){
            while(i < 1) {
                try {
                     // A 点
                    this.wait();
                    // B 点
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            notifyAll();
            //other code 1;
        }
    }

   public void consumer() {
        synchronized (this){
            while(i > 0) {
                try {
                     // C 点
                    this.wait();
                    // D 点
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i--;
            notifyAll();
            //other code 2;
        }
    }

    四个线程T1（生产者）、T2（生产者）、T3（消费者）、T4（消费者）同时调用了start开始执行，状态都是RUNNING，不巧的是，T1获得了锁，然后T2、T3、T4然后分别挂起，这三个线程都加入等待队列了，状态都变成了WAITING
    T1继续执行，完成了i++, 然后执行notifyAll(), 此时，T1还没执行完，锁还在自己手里，所以i++和notifyAll()的先后顺序不重要
    当T1的执行完代码，other code 1也执行完，则T2、T3、T4全部被唤醒，啥叫唤醒啊，唤醒就是从等待队列转移到同步队列，T2-T4的线程状态变成了Blocked状态，如果是notify，则随机移动一个线程，其状态改为BLOCKED状态
    也就是说处于Blocked状态的线程（T2、T3、T4）都是在等地获取监视器锁，T1已经执行完了，T1就被销毁了（除非是while循环执行）。很不巧的是，T2被选中了，则T3和T4又被迫移入WaitQueue,从blocked变成了waiting.

    关键：T2从原来的wait()的地方恢复执行，也就是一上来就开始执行 B 点，如果此时不做if判断，则T1执行完已经把i++到1了，T2再i++一下怕是不合适吧，所以代码又得变成：

     public void producer() {
        synchronized (this){
            if(i < 1) {
                try {
                     // A 点
                    this.wait();
                    // B 点
                    if( i < 1 ){
                        this.wait();
                        //C 点
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            notifyAll();
            //other code 1;
        }
    }

    嗯，从B点开始，T2发现i已经变成了1了，则自己主动进入wait状态。
    这个时候，如果其它线程比如生产者消费者来回交互多轮之后，前面一个生产者刚刚执行完，又一次T2被唤醒，我擦，又从C点恢复执行
    又得判断i++能不能进行操作，不能操作的话，还得主动进行wait，代码又成了：

     public void producer() {
        synchronized (this){
            if(i < 1) {
                try {
                     // A 点
                    this.wait();
                    // B 点
                    if( i < 1 ){
                        this.wait();
                        //C 点
                        if( i < 1){
                            this.wait();
                            //D点
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            notifyAll();
            //other code 1;
        }
    }

    我擦，这么写去还怎么写代码...无限嵌套啊， 反正只在最外层有个if一定是错误，因为恢复时还得判断当前能不能执行
    所以，上面这段代码的解决办法就是：while

    public void producer() {
        synchronized (this){
            while(i < 1) {
                try {
                     // A 点
                    this.wait();
                    // B 点, 当前线程从这里恢复时，首先要判断执行的条件是否达标，如果未达标，又得进行wait，只有达标了额，才能执行后面的i++操作。拿到了锁，只能说是有资格执行，但是执行时还得看临界资源状态。
                    // 尤其是多个消费者往buffer里放10个东西，多个消费者去消费buffer的10个资源
                    // 当生产者每次拿到锁，被唤醒之后，第一件事就是判断buffer满了没有，满了的话你想写也不能写啊，还得进入wait啊
                    // 当消费者每次拿到锁，被唤醒以后，第一件事就是判断buffer里有没有资源，你想消费的，那也得buffer里有才行啊，没有的话，你只能wait生产者了
                    // 总结：生产者和消费者是一堆互不干涉的线程，各执行各的，就只依赖临界资源的状态来互相通信（通信就是while判断条件）
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            notifyAll();
            //other code 1;
        }
    }
     */
}
