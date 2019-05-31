package com.poles.day7;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-31 17:29
* @desc
 *
 * 单例的创建需要注意的几点：
 *  1. 不要一上来就创建，什么时候用，什么时候创建
 *  2. 普通的饱汉式创建方式：
 *      2.1 如果不使用单例对象，而仅仅使用单例中的otherFields，此时也会实例化整个对象，导致对象提前创建出来
 *      2.2 如果多个线程同时创建，解决不了并发问题，如果增加synchronized到时可以解决并发问题，但是在多线程环境下却会降低性能
 *  3. 如果单例对象时实现了Serializable接口，那么反序列化时，也会创建对象
 *      解决办法是：增加addResolve()方法，直接返回创建好的实例
 * 4. 通过反射获取实例对象，反射代码中关闭安全检查（constructor.setAccessible(true)），就可以直接调用构造方法，如此又创建了一个实例
 *      解决办法是：在构造方法中，判断实例是否存在，如果存在，直接抛出异常，反射调用时也会直接抛出异常的
*
*********************************************************************
*/
public class Singleton implements Serializable {
    public static int otherFields = 10;
    private Singleton(){
          //防止反射的方式调用构造方法创建示例对象。
        // 目的很简单，就是任何情况下，不能调用构造方法，增加私有构造是防止普通方法调用构造方法，但是无法防止通过反射的方式调用构造方法，
        // 所以这里抛个异常，不管什么情况，只要通过构造方法来创建对象，必须不行，所以只要有人调用构造方法，我就抛异常
//        if (SingletonHolder.instance != null) {
//            throw new RuntimeException("该实例已经存在，请直接通过Singleton.getInstance()获取");
//        }
    }

    public static Singleton getInstance() {
        return SingletonHolder.instance;
    }

    //使用静态内部类的方式，只有在使用时才会初始化创建对象
    private static class SingletonHolder{
        private static Singleton instance = new Singleton();
    }

    //该方法用来解决反序列化导致的重新创建单例对象的问题
//    private Object readResolve() throws ObjectStreamException{
//        return SingletonHolder.instance;
//    }
}
