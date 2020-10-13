package com.threadPool;

import java.util.ArrayList;
import java.util.Random;

public class T08_ForkJoinPool2 {
    public static void main(String[] args) {
        //判断是否为质数
        ArrayList<Integer> list = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < 1000; i++){
            list.add(100_0000 + r.nextInt(100_0000));
        }

        //普通循环判断
        long start = System.currentTimeMillis();
        list.forEach(n->isPrime(n));
        long end = System.currentTimeMillis();
        System.out.println("循环判断耗时:" + (end - start));


        start = System.currentTimeMillis();
        //并行流（底层实现为ForkJoinPool）判断
        list.parallelStream().forEach(n->isPrime(n));
        end = System.currentTimeMillis();
        System.out.println("并行流判断耗时:" + (end - start));

        //执行结果：
        //循环判断耗时:128735
        //并行流判断耗时:36147
    }

    private static boolean isPrime(int num){
        for(int i = 2; i < num / 2; i++){
            if(num % i == 0){
                return false;
            }
        }

        return true;
    }
}
