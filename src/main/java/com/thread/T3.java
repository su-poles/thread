package com.thread;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/16 2:20 下午
* 锁不能为null, 如果是null就会报告NullPointException
*********************************************************************
*/
public class T3 {
    private static Object o = null;
    private static int count = 100;

    public static void m(){
        synchronized (o){
            count--;
        }
    }
    public static void main(String[] args) {
        for(int i = 0; i < 100; i++){
            new Thread(T3::m, "Thread-" + i).start();
        }
    }
}
