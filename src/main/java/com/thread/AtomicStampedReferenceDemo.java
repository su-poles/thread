package com.thread;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/8 6:26 下午
* AtomicStampedReference里面除了数值字段以外，还维护了一个int类型的状态字段，可以使时间戳，可以是其它版本字段
 * 当AtomitStampedReference中对应的数值改变时，还必须要更新其状态字段，改变数值字段的值是，对象值以及状态字段都必须满足期望才能写入成功
 * 因此AtomicStampedReference能够防止ABA问题（增加了一个版本或者状态字段）
*********************************************************************
*/
public class AtomicStampedReferenceDemo {
    //初始化一个Integer对象，初始版本号为1， 模拟某一个用户的账户金额，只有19元钱了，只有线程启动执行时账户初始金额<20的才会增加20元
    private static AtomicStampedReference<Integer> atomic = new AtomicStampedReference<>(19, 1);

    public static void main(String[] args) {
        //生产者，只要金额小于20元，就再送20元，但是，只送一次。 因为生产者属于类似定时任务的东西，我们模拟有10个线程同时在跑
        //消费者，只要金额大于10元，就不停消费，每次消费10元，直到余额小于10元。消费者只有一个线程，模拟一个用户的消费过程

        //生产者
        for(int i = 0; i < 10; i++){
            final int version = atomic.getStamp();   //获取初始版本号，只有等于初始版本号的才可以增加20
            new Thread(){
                @Override
                public void run() {
                    while(true){
                        int reference = atomic.getReference();
                        if(reference < 20 && atomic.compareAndSet(reference, reference + 20, version, version + 1)){
                            System.out.println("用户初始化金额为" + reference + "，系统增加了20元，当前余额" + atomic.getReference());
                        }else{
//                            System.out.println("用户余额为" + reference + ", 但是版本号不是初始化版本号，无需处理！");
                        }
                    }
                }
            }.start();
        }


        //用户消费行为
        new Thread(){
            @Override
            public void run() {
                while(true){
                    int reference = atomic.getReference();
                    int version = atomic.getStamp();
                    if(reference > 10){
                        //开始消费

                        if(atomic.compareAndSet(reference, reference - 10, version, version + 1)){
                            System.out.println("用户成功消费10元，当前余额" + (reference - 10));
                        }
                    }else{
                        System.out.println("金额不足10元，请充值！");
                    }
                }
            }
        }.start();
    }
}
