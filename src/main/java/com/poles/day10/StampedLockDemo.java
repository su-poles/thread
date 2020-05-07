package com.poles.day10;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

/**
*********************************************************************
* 
* @author poles
* @date 2019-06-04 17:34
* @desc
 * 对读写锁的改进：
 *      普通给的读写锁是：读读不
*
*********************************************************************
*/
public class StampedLockDemo implements Runnable{
    //定义个坐标
    private int x,y;
    //定义一把锁
    private final StampedLock sl = new StampedLock();

    public StampedLockDemo(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        Random random = new Random();
        int deltaX = random.nextInt(10);
        int deltaY = random.nextInt(10);
        int x1 = x;
        int y1 = y;
        int deltaX1 = deltaX;
        int deltaY1 = deltaY;
        move(deltaX, deltaY);
        System.out.println("("+x1+","+y1+")("+(x1+deltaX1)+","+(y1+deltaY1 )+"):" + distanceFromOrigin());
    }

    void move(int deltaX, int deltaY){  //an exclusively locked method
        long stamp = sl.writeLock();
        try{
            x += deltaX;
            y += deltaY;
        }finally {
            //请注意这stamp这个参数，stamp其实就一个乐观锁
            sl.unlockWrite(stamp);
        }
    }

    double distanceFromOrigin() { // a read-only method
        long stamp = sl.tryOptimisticRead();
        int currentX = x, currentY = y;
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            }finally {
                sl.unlockWrite(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        //定义原点坐标
        int x = 0;
        int y = 0;
        StampedLockDemo stampedLockDemo = new StampedLockDemo(x, y);
        for (int i = 0; i < 100; i++) {
            es.execute(stampedLockDemo);
        }
    }
}
