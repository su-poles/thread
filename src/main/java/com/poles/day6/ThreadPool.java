package com.poles.day6;

import java.util.List;
import java.util.Vector;

public class ThreadPool {
    private static ThreadPool instance = null;

    //空闲的线程队列
    private List<Worker> idleThreads;
    //已有的线程总数
    private int threadCounter;
    private boolean isShutDown = false;

    private ThreadPool() {
        this.idleThreads = new Vector<>(5);
        threadCounter = 0;
    }

    public int getCreatedThreadsCount() {
        return threadCounter;
    }

    //取得线程池的实例
    public synchronized static ThreadPool getInstance() {
        if (instance == null) {
            instance = new ThreadPool();
        }
        return instance;
    }

    //将线程放入池中
    protected synchronized void repool(Worker repoolingThread) {
        if (!isShutDown) {
            idleThreads.add(repoolingThread);
        } else {
            //关闭
            repoolingThread.shutDown();
        }
    }

    //停止池中所有线程
    public synchronized void shutdown() {
        isShutDown = true;
        for (int threadIndex = 0; threadIndex < idleThreads.size(); threadIndex++) {
            Worker idleThread = (Worker) idleThreads.get(threadIndex);
            idleThread.shutDown();
        }
    }

    //执行任务
    public synchronized void start(Runnable target) {
        Worker thread = null;
        if (idleThreads.size() > 0) {
            int lastIndex = idleThreads.size() - 1;
            thread = (Worker) idleThreads.get(lastIndex);
            idleThreads.remove(lastIndex);
            //立即执行这个任务
            thread.setTarget(target);
        } else {
            //没有空闲线程，则创建新线程
            threadCounter++;
            //创建新线程
            thread = new Worker(target, "PThread #" + threadCounter, this);
            //启动这个线程
            thread.start();
        }
    }
}