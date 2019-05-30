package com.poles.day4;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ********************************************************************
 *
 * @author poles
 * @date 2019-05-22 18:25
 * @desc ********************************************************************
 */
public class AtomicStampedReferenceDemo {
    //初始化一个Integer对象
    static AtomicStampedReference<Integer> money = new AtomicStampedReference<>(19, 0);

    //模拟多个线程同时更新后台数据库，为用户充值
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            final int version = money.getStamp();

            //模拟充值线程
            new Thread(() -> {
                Integer m = money.getReference();
                //小于20就充值20块
                if (m < 20) {
                    //compareAndSet(V expectedReference, V newReference, int expectedStamp, int newStamp)
                    boolean success = money.compareAndSet(m, m + 20, version, version + 1);
                    if (success) {
                        System.out.println("余额小于20元，充值20元成功！当前余额：" + money.getReference() + "元！");

                        //只能充值一次，所以充值成功就退出
                    }
                } else {
                    //余额大于20元，无需充值
                }
            }).start();
        }

        //模拟消费线程
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                int version = money.getStamp();
                Integer m = money.getReference();
                //余额大于10块就开始消费
                if (m > 10) {
                    boolean success = money.compareAndSet(m, m - 10, version, version + 1);
                    if (success) {
                        System.out.println("余额大于10元，成功消费10元！ 当前余额：" + money.getReference());
                    }
                }else{
                    //没有足够的金额，不允许消费
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
