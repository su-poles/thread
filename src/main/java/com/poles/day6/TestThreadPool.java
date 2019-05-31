package com.poles.day6;

public class TestThreadPool {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("线程执行体:" + Thread.currentThread().getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ThreadPool instance = ThreadPool.getInstance();
        instance.start(t);
    }
}
