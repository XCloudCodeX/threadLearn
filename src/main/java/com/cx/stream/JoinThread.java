package com.cx.stream;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/2
 * Time: 20:39
 * Description: No Description
 */
public class JoinThread {

    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        for (int i=0;i<10; i++){
            //每个线程有前一个线程的引用,需要等待前一个线程结束,才能从等待中返回
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getId()+"--主程序--"+Thread.currentThread().getName()+"  "+System.nanoTime()+ " terminate;");
    }

    static class Domino implements Runnable{
        private Thread thread;

        public Domino(Thread thread) {
            this.thread = thread;
        }

        @SneakyThrows
        @Override
        public void run() {
            thread.join();
            System.out.println(Thread.currentThread().getId()+"---"+Thread.currentThread().getName()+"  "+System.nanoTime()+ " terminate;");
        }
    }
}
