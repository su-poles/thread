package com.reference;

import java.lang.ref.SoftReference;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/22 11:38 上午
* 软引用：一个变量引用到了软引用对象上，软引用对象指向真正的堆内对象上，当内存不够时，gc才会回收，一般用来做缓存
*********************************************************************
*/
public class SoftReference_03 {
    public static void main(String[] args) throws InterruptedException {
        SoftReference<byte[]> r = new SoftReference<byte[]>(new byte[1024 * 1024 * 10]);

        byte[] s = new byte[1024 * 1024 * 15];

        //设置 -Xms20m -Xmx20m 并执行
        System.out.println("------------------------------------------" + r.get());
    }
}
