package com.map;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/24 10:33 上午
 *
*********************************************************************
*/
public class M04_ConcurrentHashMap {
    static Map<UUID, UUID> map = new ConcurrentHashMap<>();
    /*
     * ConcurrentXXX都是通过CAS的方式实现的，按理说应该有ConcurrentHashMap、ConcurrentTreeMap,但是为了实现上的简单（插入效率不至于太低），且
     * 各个元素还能保持有序性，所以通过ConcurrentSkipListMap来代替ConcurrentTreeMap
     *
     * 在数据量比较大的情况下，线程较多的情况下，ConcurrentHashMap写入效率没有Synchrnized高
     *
     * 说白了，就是CAS与Synchronized比较性能，当线程多，竞争激烈，执行时间长，当然用锁，线程少执行速度快用CAS
     * 如果竞争激烈，执行时间非常短，则CAS效率要比Synchronized高很多。
     * 至于选择哪种，需要经过测试，压测，JMH性能测试最终决定用哪种
     *
     * 高并发，一定要明白高并发的程度是多少。
     */
//    static Map<UUID, UUID> map = new ConcurrentSkipListMap<>();
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
