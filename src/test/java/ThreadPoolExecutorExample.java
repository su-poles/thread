package java;import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/3/6 8:43 下午
* @desc 
*
*********************************************************************
*/
public class ThreadPoolExecutorExample {
        public static void main(String[] args) {
                //最对可以处理8个任务，最大线程数时3，阻塞队列有5个
                //最后一个参数是拒绝策略
                ThreadPoolExecutor executors = new ThreadPoolExecutor(2,
                        3,
                        60,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<>(5),
                        Executors.defaultThreadFactory());



                //现在创建9个任务
                for(int i = 1; i <= 9; i++){

                }


                //有界队列：
                // 1. 先将任务分配给核心线程，
                // 2. 然后进入队列，先入先出
                // 3. 当队列满之后，创建非核心线程，将任务分配给非核心线程
                // 4. 如果非核心线程也满了，此时触发拒绝策略
                // 5. 参见图片
        }
}
