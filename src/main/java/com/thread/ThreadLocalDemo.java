package com.thread;

import java.util.concurrent.TimeUnit;

/**
*********************************************************************
* 
* @author poles
* @date 2020/9/21 10:52 下午
* 定义一个ThreadLocal的变量，然后两个线程，一个设值，一个获取值
 *
 * 为什么另一个线程获取不到值？请看源码。
 *
 * ThreadLocal使用案例：Spring的声明式事务
 * 对于多方方法（一个线程），第一个方法会去数据库连接池获取Connection,然后存放在ThreadLocalMap中，后面的方法用Connection时，
 * 都是从当前线程的ThreadLocalMap中获取Connection。 所以说多个方法，都用的同一个Connection, 才能形成一个事务。
 *
 * 所谓不同的事务，也就是不同的数据库连接而已
*********************************************************************
*/
public class ThreadLocalDemo {
    static ThreadLocal<Person> t = new ThreadLocal<Person>();

    public static void main(String[] args) {
        //一个线程设置值
        new Thread(()->{
            t.set(new Person(1));         //给当前线程的ThreadLocalMap中添加值，ThreadLocalMap.set(ThreadLocal, Person)
        }).start();

        //另一个线程获取值
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t.get());        //从当前线程的ThreadLocalMap中获取值, ThreadLocalMap.get(ThreadLocal) 当然取不到值
        }).start();
    }


    static private class Person{
        private int id;
        public Person(int id){
            this.id = id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
