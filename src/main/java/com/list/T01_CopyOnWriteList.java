package com.list;

import java.util.concurrent.CopyOnWriteArrayList;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/25 9:58 下午
*
*********************************************************************
*/
public class T01_CopyOnWriteList {
    public static void main(String[] args) {
        /*
         * CopyOnWriteArrayList 适用于读多写少，读不加锁，写加锁
         *
         * 扩容时，每次只增加1位，因为内部是用数组实现的，所以每次新增，需要增加一位，并且进行数据拷贝
         */
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        System.out.println(list.get(4));
        System.out.println(list.size());

        list.add(5);
        System.out.println(list.size());
    }
}
