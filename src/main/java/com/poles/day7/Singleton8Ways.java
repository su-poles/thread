package com.poles.day7;

/**
 * 实现单例模式的8中写法
 */

public class Singleton8Ways {
}

/**
 * 1. 饿汉式，可用。 加载时就初始化示例，但是没有达到lazy-loading效果，可能存在内存浪费（如果从来不用这个和实例的话）
 */
class Singleton1{
    private final static Singleton1 INSTANCE = new Singleton1();
    private Singleton1(){}

    public static Singleton1 getInstance(){
        return INSTANCE;
    }
}

/**
 * 2.饿汉式（静态代码块）, 可用，跟Singleton1缺点一样
 */
class Singleton2 {

    private static Singleton2 instance;

    static {
        instance = new Singleton2();
    }

    private Singleton2() {}

    public static Singleton2 getInstance() {
        return instance;
    }
}



/**
 * 3.懒汉式(线程不安全)
 */
class Singleton3 {

    private static Singleton3 singleton;

    private Singleton3() {}

    public static Singleton3 getInstance() {
        if (singleton == null) {
            singleton = new Singleton3();
        }
        return singleton;
    }
}

/**
 * 4.懒汉式(线程安全，效率低，所有线程都要竞争锁)
 */
class Singleton4 {

    private static Singleton4 singleton;

    private Singleton4() {}

    public static synchronized Singleton4 getInstance() {
        if (singleton == null) {
            singleton = new Singleton4();
        }
        return singleton;
    }
}

/**
 * 5.懒汉式(第4中改进版，但是改错了，改成线程不安全的了),如果有多个线程同时走到了if (singleton == null)
 * 且都判断为null,那么只有1个线程会进入同步块，其它线程会等待，但是其它线程等待一会儿最终都能执行完同步块
 * 里的代码，这样最开始的时候，如果有多个线程进入了if (singleton == null) ，那么就会创建多个实例
 */
class Singleton5 {

    private static Singleton5 singleton;

    private Singleton5() {}

    public static Singleton5 getInstance() {
        if (singleton == null) {
            synchronized (Singleton5.class){
                singleton = new Singleton5();
            }
        }
        return singleton;
    }
}

/**
 * 6.懒汉式(double-check),线程安全，效率高，延迟加载
 * 这种方式也有个问题，就是两个线程之间的可见性问题
 * AB 两个线程同时去创建实例，然后其中线程A在执行singleton = new Singleton6();时
 * 刚刚从内存中开辟了一段内存区域，但是还没来的及完成构造方法，也就是说还没有来得及完成new这个操作
 * 线程B进来判断singleton != null, 然后直接返回一个instance“实例”，那么这个实例马上去执行方法时
 * 卧槽，出问题了
 */
class Singleton6 {

    private static Singleton6 singleton;

    private Singleton6() {}

    public static Singleton6 getInstance() {
        if (singleton == null) {
            synchronized (Singleton6.class){
                if(singleton == null) {
                    singleton = new Singleton6();
                }
            }
        }
        return singleton;
    }
}

/**
 *7. 静态内部类，推荐使用。Effiective Java也推荐的这种方式
 * 因为静态类，所以采用了JDK的类装载机制来保证初始化实例时只有一个线程。
 * 静态内部类在外部类装载时不会立即初始化，只是在调用的时候，才会初始化，从而完成了lazy-load的效果
 * 由于是静态你内部类，通过JDK机制保证实例化只有一个，所以也是线程安全的
 * JSL规范定义：类的构造方法必须是原子性的，非并发的
 */
class Singleton7 {

    private Singleton7() {}

    private static class SingletonInstance {
        private static final Singleton7 INSTANCE = new Singleton7();
    }

    public static Singleton7 getInstance() {
        return SingletonInstance.INSTANCE;
    }
}
/**
 * 8. 枚举方式，推荐. JVM保证创建枚举实例也是thread-safe的
 * 还能防止反序列化重新创建新的对象
 */
enum Singleton8 {
    INSTANCE;
    public void run() {
        //这里就要通过单例实现的功能
        //调用的时候，这么调用
        //Singleton8 instance = Singleton8.INSTANCE;
        //instance.run();  //为什么叫实例呢，应为这里的instance永远只实例一次，就是你第一次用的时候
        //可以写一个私有构造方法，里面打印个日志瞅瞅，不管这个枚举调用多少次，依然只初始化一次
    }
}