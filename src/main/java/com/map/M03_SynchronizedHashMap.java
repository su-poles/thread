package com.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/24 10:33 上午
 * 使用同步hashMap代替hashtable做个测试
*********************************************************************
*/
public class M03_SynchronizedHashMap {
    static Map<UUID, UUID> map = Collections.synchronizedMap(new HashMap<UUID, UUID>());
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


        System.out.println("读取耗时：" + (System.currentTimeMillis() - start) + " 毫秒！");
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
