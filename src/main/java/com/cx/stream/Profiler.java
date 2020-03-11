package com.cx.stream;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/2
 * Time: 22:01
 * Description: 线程方法执行时间类工具
 * 根据ThreadLocal的特性创建一个工具类,包含begin()和end()方法,begin记录执行时间,end返回从begin执行到end方法执行的时间差;
 */
public class Profiler {
    /**
     * 初始化调用时间
     * ThreadLocal 线程变量,是一个以ThreadLocal对象为键,任意对象为值的存储结构
     * 此结构被附带在线程之上,也就是一个线程可以根据一个ThreadLocal对象查询到绑定在这个线程上的一个值
     * 有一个set(T)方法来设置一个值,一个get()方法获取原先设置的值
     */
    private static final ThreadLocal<Long>  TIME_THREADLOCAL = ThreadLocal.withInitial(() -> System.currentTimeMillis());

    public static final void begin(){
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final Long end(){
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws InterruptedException {
        Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("cost: "+ Profiler.end() + " mills");
    }
}
