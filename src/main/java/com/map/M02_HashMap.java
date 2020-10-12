package com.map;

import java.util.HashMap;
import java.util.UUID;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/24 10:33 上午
 * hashMap不是线程安全的，所以多线程插入没意义，这里只是看一下，多线程写入是有问题的
 *
 *  多线程写入hashMap的运行结果：
 *  1. 可能会报错，提示Node转TreeNode会出现类型转换异常，同时伴随着程序进入死循环
 *  1. 可能回导致死循环
 *  2. 如果正常结束，则最后的size几乎不可能正确
*********************************************************************
*/
public class M02_HashMap {
    static HashMap<UUID, UUID> map = new HashMap<UUID, UUID>();
    static UUID[] keys = new UUID[Constants.ElementCount];
    static UUID[] values = new UUID[Constants.ElementCount];

    static {
        for(int i = 0; i < Constants.ElementCount; i++){
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //插入，使用100个线程，分别插入10000个内容
        putElement();  //先写入
        //读取，使用100个线程，每个线程读取1000万次的某个元素
        getElement();
    }

    private static void putElement() throws InterruptedException {
        Thread[] threads = new Thread[Constants.ThreadCount];

        //分段装，每段装1w个元素
        for(int i = 0; i < Constants.ThreadCount; i++){
            threads[i] = new MThread(i);
        }

        long start = System.currentTimeMillis();
        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            thread.join();
        }

        System.out.println("写入耗时：" + (System.currentTimeMillis() - start) + " 毫秒！");
        System.out.println("写入元素个数：" + map.size());
    }

    private static void getElement() throws InterruptedException {
        Thread[] threads = new Thread[Constants.ThreadCount];

        for(int i = 0; i < Constants.ThreadCount; i++){
            threads[i] = new Thread(()->{
                for(int j = 0; j < 1000_0000; j++){
                    UUID result = map.get(keys[20]);
                }
            });
        }

        long start = System.currentTimeMillis();
        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            thread.join();
        }

        System.out.println("耗时：" + (System.currentTimeMillis() - start) + " 毫秒！");
    }


    private static class MThread extends Thread{
        private int start;

        public MThread(int start){
            this.start = start;
        }

        @Override
        public void run() {
            int step = Constants.ElementCount / Constants.ThreadCount;
            for(int i = start * step; i < start * step + step; i++){
                map.put(keys[i], values[i]);
            }
        }
    }
}
