package com.thread.phaser;

import java.util.concurrent.Phaser;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/18 3:28 下午
* Java提供的Phaser是个阶段器，CyclicBarrier是个循环栅栏，而Phaser是一个固定的栅栏，比如我可以设置5个栅栏，其中前四个栅栏要所有人都到齐了才开始执行，最后一个栅栏，要某几个人到齐就可以执行
 *
 * 举个团建的例子：
 *  有10个员工+老板，也就是有11个线程，当11个线程都到齐，才能开车出发到目的地，然后11个人到齐开饭，11个人到齐开始游戏，下一个环节老板演讲，最后一个环节，大家离开
 *
 *  阶段一：出发
 *  阶段二：吃饭
 *  阶段三：玩游戏
 *  阶段四：老板演讲
 *  阶段五：所有人离开
 *
 *  phaser.register() 就是 + 1
 *  phaser.xxxdegister() 就是 - 1
*********************************************************************
*/
public class PhaserDemo {
    public static void main(String[] args) {
        final int  N = 5;  //一共5个人
        Phaser phaser = new TeamBuildPhaser();

        //4个员工，1个老板，一起开开心心去团建
        Member[] members = new Member[N];
        phaser.bulkRegister(N);
        for(int i = 0; i < N - 1; i++){
            members[i] = new Member(phaser, (i+1) + "号员工");
//            phaser.register();
        }
        members[N - 1] = new Member(phaser, "老板");    //phaser.register();

        Thread[] threads = new Thread[members.length];
        for(int i = 0; i < members.length; i++){
            threads[i] = new Thread(members[i]);
        }

        for(Thread thread : threads){
            thread.start();
        }
    }
}
