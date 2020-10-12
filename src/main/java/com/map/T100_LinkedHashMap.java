package com.map;

import java.util.LinkedHashMap;

/**
*********************************************************************
* 
* @author poles
* @date 2020/10/10 12:30 下午
* LinkedHashMap extents HashMap, 且 LinkedHashMap.Entry<K,V>  extends HashMap.Node<K, V>
* 实现原理为HashMap + 双向链表
*********************************************************************
*/
public class T100_LinkedHashMap {
    final static int MAX_CACHE = 5;
    //利用accessOrder=true实现LRUCache
    static LinkedHashMap<String, String> map = new LinkedHashMap<>(0, 0.75f, true);

    public static void main(String[] args) {
        //测试
        addToCache("fk", "fv");
        System.out.println("增加值：fk, vk\n当前容器内的元素有：");
        printAll();
        System.out.println("---------------------------------------------------");

        getFromCache("ck");
        System.out.println("获取值：ck\n当前容器内的元素有：");
        printAll();
        System.out.println("---------------------------------------------------");

        getFromCache("zk");
        System.out.println("获取值：zk\n当前容器内的元素有：");
        printAll();
        System.out.println("---------------------------------------------------");

        addToCache("bbk", "bbv");
        System.out.println("增加值：bbk, bbk\n当前容器内的元素有：");
        printAll();
        System.out.println("---------------------------------------------------");

        addToCache("fk", "fv");
        System.out.println("增加值：fk, vk\n当前容器内的元素有：");
        printAll();
        System.out.println("---------------------------------------------------");


        addToCache("ak", "av");
        addToCache("bk", "bv");
        addToCache("ck", "cv");
        addToCache("dk", "dv");
        addToCache("ek", "ev");

        System.out.println("增加一组值，当前容器内的元素有：");
        printAll();
        System.out.println("---------------------------------------------------");

    }

    public static synchronized void addToCache(String key, String value){
        if(map.size() == MAX_CACHE){
            //删除最早的元素
            for(String sk : map.keySet()){
                map.remove(sk);
                break;
            }
        }

        map.put(key, value);
    }

    public static synchronized String getFromCache(String key){
        return map.get(key);
    }

    private static void printAll(){
        map.forEach((x,y)->{
            System.out.println(x + ", " + y);
        });
    }
}
