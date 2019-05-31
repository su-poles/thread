package com.poles.day6;

import javafx.concurrent.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-31 13:49
* @desc 
* forkjoin原理
* 将一个大任务分解成为很多小任务，然后最后再收集所有小任务的结果，汇总出一个最终结果
 *
 * 本例是求一个0-200000的所有数字的累加和
 * 先分解任务，分解到什么粒度就开始执行呢，分解到当end - stard < THRESHOLD时就开始执行任务，否则继续分解
 *
 * 在执行之前要创建一个超级线程，这个超级线程就是等待其它线程执行完成之后，收集和汇总结果的线程
*********************************************************************
*/
public class CountTask extends RecursiveTask<Long> {
    private static final Log logger = LogFactory.getLog(CountTask.class);
    private static final int THRESHOLD = 1000;
    private long start;
    private long end;

    public CountTask(long start, long end){
        this.start = start;
        this.end = end;
    }

    @Override
    public Long compute(){
        long sum = 0;
        boolean canCompute = (end - start) < THRESHOLD;
        if (canCompute) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }

        //分成100个小任务
        long step = (start + end) / 100;
        ArrayList<CountTask> subTasks = new ArrayList<>();
        long pos = start;
        for(int i = 0; i < 100; i++){
            long lastIndex = start + (i * step + step);
            if(lastIndex > end){
                break;
            }
//            System.out.println(pos + ", " + lastIndex);
            CountTask ct = new CountTask(pos, lastIndex);
            pos = lastIndex + 1;
            subTasks.add(ct);
            ct.fork();
        }
        if(pos <= end){
            CountTask ct = new CountTask(pos, end);
//            System.out.println(pos + ", " + end);
            subTasks.add(ct);
            ct.fork();
        }

        for (CountTask ct : subTasks) {
            sum += ct.join();
        }

        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //一定要注意，CountTask是继承自RecursiveTask<Long>, 递归任务
        CountTask task = new CountTask(0, 20001L);
        ForkJoinTask<Long> result = forkJoinPool.submit(task);
        long retValue = 0;
        try {
            retValue = result.get();
            System.out.println("sum = " + retValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
