package com.poles.day2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-15 23:42
* @desc 
*
*********************************************************************
*/
public class InterruptedSleepTest {
    private static final Log logger = LogFactory.getLog(InterruptedSleepTest.class);

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptedSleepThread());
        thread.start();
        //停留5秒钟
        Thread.sleep(5000);
        thread.interrupt();  //中断线程
    }
}

class InterruptedSleepThread implements Runnable{
    @Override
    public void run() {
        while (true) {
            if(Thread.interrupted()){
                break;
            }
            //do something
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
//                break;
            }
            System.out.println("虽然你进入异常了，在异常里标志位也增加上了，但是这句打印语句还是要执行的");
            Thread.yield();
        }
    }
}
/**
 * 我们这里再说说Thread.sleep()为什么要抛出一个异常，而且是 InterruptedException异常呢
 *
 * 是因为，我线程正在sleep的过程中，然后有人将中断标志位设置为true了，则就不能继续sleep了，这个时候睡眠就会被interrupt
 * 有人让这个线程中断，你还在这里sleep，是没有意义的，所以sleep是要立马响应interrupt的, 怎么响应呢？那就在InterruptedException的
 * 异常catch块中处理，可以选择继续执行后面的，或者立即执行退出，或者关闭资源等等操作
 *
 * 此时在异常处理中，需要进行中断异常处理，因为一旦发生异常，中断标志位会被清空！！！中断标志位会被清空！！！中断标志位会被清空！！！
 *
 * 所以如果要退出，还得再次设置中断标志位为true
 *
 */


