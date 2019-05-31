package com.poles.day7;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-31 17:49
* @desc
 * 通过反射的方式来破解单例
*
*********************************************************************
*/
public class CrackByReflect {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //1. 先获取单例对象
        Singleton instance = Singleton.getInstance();

        //2. 通过反射再获取一次
        Class cls = Singleton.class;
        Constructor constructor = cls.getDeclaredConstructor();
        //关闭安全检查，就可以调用私有的构造器
        constructor.setAccessible(true);
        //通过反射构造一个对象
        Singleton instance2 = (Singleton) constructor.newInstance(null);  //调用无参构造
        System.out.println(instance);
        System.out.println(instance2);
    }

    /**
     * 明显Singleton这种方式是无法防止反射创建新对象的，能创建第二个，就能创建无数个
     */
}
