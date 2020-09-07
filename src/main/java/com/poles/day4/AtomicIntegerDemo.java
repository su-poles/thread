package com.poles.day4;

import java.util.concurrent.atomic.AtomicInteger;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-22 16:42
* @desc 
*
*********************************************************************
*/
public class AtomicIntegerDemo {
    static AtomicInteger val = new AtomicInteger();
//    static int val;

    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                val.incrementAndGet();
//                val++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(new AddThread());
        }

        //让所有线程都启动
        for(int i = 0; i < ts.length; i++){
            ts[i].start();
        }

        //等线程全都启动完成之后，让主线程等一等，等所有线程执行完成之后，主线程再往下执行，然后打印val, 不然val打印时，其它线程还没有执行完，打印的数值不是最终的数值
        for(int i = 0; i < ts.length; i++){
            ts[i].join();
        }

        System.out.println(val);
    }

    /*
     * AtomicInteger 中声明了一个volatile的属性value, JVM启动时，会计算出value在内存中的偏移量valueOffset
     * 现在定了一个AtomicInteger这么一个对象，然后再给你一个valueOffset就可以计算出这个对象里value的值是多少，value是个int类型的
     * 再说一遍，我要找到AtomicInteger中某个对象的属性值，java中直接就是AtomicInteger.val即可，实际上这个val指向的就是valueOffset的位置，因为从valueOffset开始取4个字节，也就是32位，这个就是val对象中包裹的那个value的值
     * 由于value是使用volatile修饰的，所以保障了其内存可见性，任何人要读该属性，只能去主内存中读取
     *
     * 言归正传：
     * 有多个线程，要同时对val对象做+1操作，其中大多数线程先去读取其内存的数值：
     *          var5 = this.getIntVolatile(var1, var2);      //这个就是，给一个val对象，然后给出val对象中的value的偏移量，就能直接从内存中取出value的旧值
     *
     * 然后这些线程都对val的value属性重新设置一个新值， 但是要判断当前内存中的值（通过val对象及value属性的偏移量来确定）跟旧值相等，也就是=var5，则更新成功，否则什么都不做，直接返回false
     *          this.compareAndSwapInt(this, valueOffset, var5, var5+var4)      //根据this和valueOffset确定内存中的数值与var5是否相等（x86中C++调用CMPXCHG执行），如果相等则将value赋值为新值（这整个是个原子操作）
     *
     * 其它线程，发现内存中的值不等于自己的var5，则设值失败，重新读取value的值，再设值一次，反正这么多线程，每次总有一个能成功设值value的值，以下是源码：
     *
             public final int getAndIncrement() {
                return unsafe.getAndAddInt(this, valueOffset, 1);
            }

            //unsafe.getAndAddInt
            public final int getAndAddInt(Object var1, long var2, int var4) {
                int var5;
                do {
                    var5 = this.getIntVolatile(var1, var2);
                } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

                return var5;
            }
     *
     */
}
