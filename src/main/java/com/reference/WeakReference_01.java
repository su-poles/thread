package com.reference;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/22 11:38 上午
* 弱引用
*********************************************************************
*/
public class WeakReference_01 {
    public static void main(String[] args) throws InterruptedException {
        WeakReference<byte[]> r = new WeakReference<byte[]>(new byte[1024 * 1024 * 10]);

        System.out.println(r.get());             //打印r对象的内存地址，是否为null
//        System.gc();    //full gc
//        TimeUnit.MILLISECONDS.sleep(100);      //给足够的时间，让gc进行回收
        System.out.println(r.get());


        byte[] s = new byte[1024 * 1024 * 15];
        System.out.println(r.get());            //此处，执行前一定要设置堆内存大小为20M

    }
}
