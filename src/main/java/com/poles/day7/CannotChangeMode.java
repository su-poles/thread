package com.poles.day7;

/**
 * 不变模式
 * 常见的不变模式的很多对象，针对这些对象的很多操作其实都是生成了新的对象
 * java.lang.Byte
 * java.lang.Short
 * java.lang.Integer
 * java.lang.Long
 * java.lang.Float
 * java.lang.Double
 * java.lang.Boolean
 * java.lang.Character
 * java.lang.String
 */
//确保无子类，不能有个CannotChangeMode ccm = new SubCannotChangeMode(); 可变吧
public final class CannotChangeMode {
    //私有属性，不能被其它对象看到
    private final String no;
    //final属性不能被二次赋值
    private final String name;
    private final double price;

    public CannotChangeMode(String no, String name, double price){
        super();
        this.no = no;
        this.name = name;
        this.price = price;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
