package com.reference;

import java.lang.ref.SoftReference;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/22 11:38 上午
* 软引用：一个变量引用到了软引用对象上，软引用对象指向真正的堆内对象上，当内存不够时，gc才会回收，一般用来做缓存
 *
 * 做测试：
 * 1. -Xms20m  -Xmx20m
 * 2. 创建一个10M的byte软引用数组，手动gc，打印数组，看看是否存在
 * 3. 再创建一个15M的byte数组，此时空间是不够的，看看能否创建成功，创建后，再打印第一个软引用数组，看看是否存在
*********************************************************************
*/
public class SoftReference_02 {
    public static void main(String[] args) throws InterruptedException {
        SoftReference<byte[]> r = new SoftReference<byte[]>(new byte[1024 * 1024 * 10]);
        // r = null;
        System.out.println("------------------------------------------" + r.get());             //打印r对象的内存地址，是否为null
//        System.gc();    //full gc
//        System.out.println("------------------------------------------" + 222);
//        TimeUnit.MILLISECONDS.sleep(200);      //给足够的时间，让gc进行回收
        System.out.println("------------------------------------------" + r.get());            //看看此时，gc是否回收了软引用对象。 如果SoftReference里是一个自定义对象，可以尝试重写自定义对象的finalize()方法，打印一个东西，调用gc看看是否执行，只是观察效果

        byte[] s = new byte[1024 * 1024 * 15];

        //此处，执行前一定要设置堆内存大小为20M,  -Xms20m -Xmx20m -XX:+PrintGCDetails
        // PrintGCDetails已经过时了，使用-Xlog:gc*代替
        System.out.println("------------------------------------------" + r.get());

        //可是为什么还是会偶尔出现堆内存益处呢？
    }
}
