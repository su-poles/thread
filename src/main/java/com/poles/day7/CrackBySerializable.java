package com.poles.day7;

import java.io.*;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-31 17:48
* @desc
* 通过反序列化方式来破解单例
*********************************************************************
*/
public class CrackBySerializable {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //1. 获取单例对象
        Singleton instance = Singleton.getInstance();

        //2. 通过反序列化获取单例Singleton对象
        //2.1 序列化单例对象
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/Users/liyanlong/Desktop/temp.txt"));
        oos.writeObject(instance);
        oos.flush();
        oos.close();

        //2.2反序列化单例对象
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/Users/liyanlong/Desktop/temp.txt"));
        Singleton instance2 = (Singleton) ois.readObject();

        System.out.println(instance);
        System.out.println(instance2);

        /**
         * 明显，反序列化非常轻易就破解了单例模式
         * 如果要解决该问题，需要在Single中增加readResolve()方法。
         */
    }
}
