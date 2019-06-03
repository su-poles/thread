package com.poles.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
*********************************************************************
* 
* @author poles
* @date 2019-06-03 14:02
* @desc 
*
*********************************************************************
*/
public class EchoClient implements Runnable{
    private static ExecutorService tp = Executors.newCachedThreadPool();
    private static final int sleep_time = 1000*1000*1000;  //1秒钟，此处单位为纳秒

    @Override
    public void run() {
        Socket client = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        try {
            client = new Socket();
            client.connect(new InetSocketAddress("localhost", 8000));
            pw = new PrintWriter(client.getOutputStream(), true);
            pw.print("H");
            LockSupport.parkNanos(sleep_time);
            pw.print("E");
            LockSupport.parkNanos(sleep_time);
            pw.print("L");
            LockSupport.parkNanos(sleep_time);
            pw.print("L");
            LockSupport.parkNanos(sleep_time);
            pw.print("O");
            LockSupport.parkNanos(sleep_time);
            pw.print("!");
            LockSupport.parkNanos(sleep_time);
            pw.println();
            pw.flush();

            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("from server: " + br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw != null){
                pw.close();
            }
        }
    }

    public static void main(String[] args) {
        //我当时是new了20个EchoClien，然后就报错了，没好好研究网络编程，不懂怎么处理，所以没管，我先研究多线程吧
        EchoClient ec = new EchoClient();
        int i = 0;
        while (i++ < 20) {
            tp.execute(ec);
            System.out.println("执行第" + i + "个线程！");
        }
    }
}
