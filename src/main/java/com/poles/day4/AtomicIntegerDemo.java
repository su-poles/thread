package com.poles.day4;import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
}
