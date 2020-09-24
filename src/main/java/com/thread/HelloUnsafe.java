package com.thread;

//import sun.misc.Unsafe;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/17 10:03 下午
*
*********************************************************************
*/
public class HelloUnsafe {
    public static void main(String[] args) {
        //jdk11中编译就会失败，jdk8中运行会失败
//        Unsafe unsafe = Unsafe.getUnsafe();
//        unsafe.allocateMemory(10L);
//        unsafe.setMemory(3L, 3L, (byte)5);
//        System.out.println(unsafe.getAddress(10L));
//
//        unsafe.freeMemory(10L);
    }
}
