package com.map;
/**
*********************************************************************
* 
* @author poles
* @date 2020/9/24 10:36 上午
*
*********************************************************************
*/
public class Constants {
    static final int ThreadCount = 100;                      //100个线程
    static final int ElementCount = ThreadCount * 50000;    //100万，保证能整除，后面分段存储就比较简单
}
