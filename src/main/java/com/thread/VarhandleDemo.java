package com.thread;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/21 4:38 下午
* Varhandle 是一个新的引用，比如long x = 200L, 然后定义一个varhandley也可以指向9
 * 如果用x操作，比如新增或者修改等可能就不是原子操作的，
 * 但是使用varhandle操作，可以用到cas的一些操作
*********************************************************************
*/
public class VarhandleDemo {
    int x = 10;
    static VarHandle varHandle;

    static {
        try {
            varHandle = MethodHandles.lookup().findVarHandle(VarhandleDemo.class, "x", int.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        VarhandleDemo v = new VarhandleDemo();

        System.out.println(varHandle.get(v));
        varHandle.set(v, 11);
        System.out.println(v.x);


        varHandle.compareAndSet(v, 11, 12);   //通过CAS将x的值改为12， 期望值为11
        System.out.println(v.x);

        varHandle.getAndAdd(v, 13);          //在原来的值（12）的基础上，加上13，并返回原来的值，先返回后做加法
        System.out.println(v.x);

    }
}
