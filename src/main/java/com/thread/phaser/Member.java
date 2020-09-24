package com.thread.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/18 4:00 下午
*
*********************************************************************
*/
public class Member implements Runnable{
    //每个成员有一个阶段器，有点像行程单或者节目单，看下一个阶段了，要做什么赶紧去做
    private Phaser phaser;

    //每个成员的名字
    private String name;

    public Member(Phaser phaser, String name){
        this.phaser = phaser;
        this.name = name;
    }

    @Override
    public void run() {
        //每个成员要做的事情

        //去公司，完成阶段一
        goToCompany();

        //去吃饭，完成阶段二
        goToCanteen();


        //去玩游戏，完成阶段三
        goToGame();

        //老板去演讲
        lecture();

        leave();
    }

    //离开
    private void leave() {
        System.out.println(name + "离开演讲厅...");
        phaser.arriveAndDeregister();
    }

    private void lecture(){
        if(name.equals("老板")){
            try {
                System.out.println(name + "走向演讲厅，准备去演讲...");
                TimeUnit.SECONDS.sleep((long)(Math.random()*3));
                phaser.arriveAndAwaitAdvance();     //老板到了就等一下其它人
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            //其它人
            try {
                System.out.println(name + "走向演讲厅，去听老板演讲...");
                TimeUnit.SECONDS.sleep((long)(Math.random()*3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();     //到了之后，等一下其它人和老板
        }
    }

    private void goToGame() {
        try {
            System.out.println(name + "去大厅，准备跟大家一起玩游戏...");
            TimeUnit.SECONDS.sleep((long) (Math.random()*3));
            phaser.arriveAndAwaitAdvance();     //到了之后就等人齐
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //去食堂
    private void goToCanteen() {
        try {
            System.out.println(name + "去食堂，准备吃饭...");
            TimeUnit.SECONDS.sleep((long) (Math.random()*3));
            phaser.arriveAndAwaitAdvance();     //到了之后就等人齐
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void goToCompany() {
        try {
            System.out.println(name + "去公司...");
            TimeUnit.SECONDS.sleep((long) (Math.random()*3));
            phaser.arriveAndAwaitAdvance();     //到了之后就等待，等人齐
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
