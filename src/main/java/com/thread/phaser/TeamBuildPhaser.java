package com.thread.phaser;

import java.util.concurrent.Phaser;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/18 3:41 下午
* 定义一个阶段器，并指明，每个阶段大家到齐后要干什么，即执行方法， 重写onAdvance方法即可
 *
 *  *  阶段一：出发
 *  *  阶段二：吃饭
 *  *  阶段三：玩游戏
 *  *  阶段四：老板演讲
 *  *  阶段五：所有人离开
*********************************************************************
*/
public class TeamBuildPhaser extends Phaser {
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase){
            case 0:
                setOut(registeredParties);
                return false;
            case 1:
                eat(registeredParties);
                return false;
            case 2:
                game(registeredParties);
                return false;
            case 3:
                bossLecture();
                return false;
            case 4:
                leave(registeredParties);
                return true;
            default:
                return true;
        }
    }

    //阶段一：出发
    private void setOut(int parties) {
        System.out.println("大家都到齐了，一共" + parties + "个人，出发...");
        System.out.println("\n---------------------------------------------------------\n");
        //        return false;
    }

    //阶段二：吃饭
    private void eat(int parties){
        System.out.println("大家都到齐了，一共" + parties + "个人，开始吃饭...");
        System.out.println("\n---------------------------------------------------------\n");
    }

    //阶段三：游戏
    private void game(int parties){
        System.out.println("大家都到齐了，一共" + parties + "个人，开始玩游戏...");
        System.out.println("\n---------------------------------------------------------\n");
    }

    //阶段四：老板演讲
    private void bossLecture(){
        System.out.println("老板到了，开始讲话...");
        System.out.println("\n---------------------------------------------------------\n");
    }

    //阶段五：离开
    private void leave(int parties){
        System.out.println("所有人都已经离开！");
    }

}
