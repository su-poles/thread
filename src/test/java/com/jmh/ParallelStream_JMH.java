package com.jmh;

import com.jmh.ParallelStreamDemo;
import org.openjdk.jmh.annotations.*;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/13 10:14 上午
*
*********************************************************************
*/
public class ParallelStream_JMH {
    @Benchmark
    @Warmup(iterations = 1, time = 3)       //预热，JVM先启动，先调用一次，并且等待3秒（比如可能会使用到JIT编译以提升性能）
    @Fork(5)                                //启动多少个线程去执行
    @BenchmarkMode(Mode.Throughput)         //性能指标：吞吐量
    @Measurement(iterations = 1, time = 3)  //测试要经过几轮迭代，多次迭代取平均值，为了快速出报告，这里只执行1次
    public void testParallelStream(){
        ParallelStreamDemo.foreach();
    }
}
