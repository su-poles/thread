package com.threadPool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/12 8:40 下午
* 实现1-10000的加法运算
 * 首先分解，每次分成2半，如果每一半数量小于100个，则开始计算
 * 最后把每一组的值加起来，然后返回。 这就是递归的思路
 *
 * 关键：分而治之 + 递归
*********************************************************************
*/
public class T08_ForkJoinPool {
    static int[] nums = new int[1000];
    static {
        Random random = new Random();
        for(int i = 0; i < nums.length; i++){
            nums[i] = random.nextInt(100);
        }

        System.out.println("初始数组之和 = " + Arrays.stream(nums).sum());
    }

    public static void main(String[] args) {
        //定义一个线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        //声明任务, 当任务量小于100时开始计算，否则继续分解任务
        SumTask task = new SumTask(nums, 0, nums.length - 1, 100);

        //开始执行任务
        forkJoinPool.execute(task);

        //综合所有任务的执行结果
        long result = task.join();
        System.out.println("forkJoin计算结果：" + result);
    }

    static class SumTask extends RecursiveTask<Long> {
        //原数组
        int[] nums;

        //分段开始索引
        private int start;
        //分段结束索引
        private int end;
        //临界点，小于临界点，就开始计算，否则继续分解任务
        private int threshold;

        public SumTask(int[] nums, int start, int end, int threshold){
            this.nums = nums;
            this.start = start;
            this.end = end;
            this.threshold = threshold;
        }

        @Override
        protected Long compute() {
            //如果分解后的任务小于临界值，则开始计算
            if(end - start <= threshold){
                long sum = 0;
                for(int i = start; i <= end; i++){
                    sum += nums[i];
                }
                return sum;
            }

            //否则，继续分解任务：劈成两半即可
            int middle = start + (end - start) / 2;
            SumTask task1 = new SumTask(nums, start, middle, threshold);
            SumTask task2 = new SumTask(nums, middle + 1, end, threshold);
            task1.fork();
            task2.fork();

            return task1.join() + task2.join();
        }
    }
}
