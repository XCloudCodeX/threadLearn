package com.cx.stream;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/1/31
 * Time: 11:14
 * Description: No Description
 */
public class Pied {
    public static void main(String[] args) throws IOException {
        PipedReader in = new PipedReader();
        PipedWriter out = new PipedWriter();
        //必须连接输入输出流
        out.connect(in);

        Thread printThread = new Thread(new Print(in),"PrintThread");
        printThread.start();

        int received = 0;
        while ((received = System.in.read()) != -1){
          out.write(received);
        }
        out.close();
        in.close();
    }

    static class Print implements Runnable{
        private PipedReader in;

        public Print(PipedReader in) {
            this.in = in;
        }

        @lombok.SneakyThrows
        @Override
        public void run() {
            int receive = 0;
            while ((receive = in.read())!= -1){
                //转字符输出
                System.out.print((char) receive);
            }
        }
    }
}

