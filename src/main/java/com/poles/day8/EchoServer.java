package com.poles.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
*********************************************************************
* 
* @author poles
* @date 2019-06-03 14:01
* @desc 
*
*********************************************************************
*/
public class EchoServer {
    private static ExecutorService tp = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        ServerSocket echoServer = null;
        Socket clientScoket = null;
        try {
            echoServer = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                //这里accept是阻塞的，每监听到一个客户端就开始往下响应
                //如果使用NIO编程，可以通过ssd.congigureBlocking(false)来设置此处是非阻塞的, 客户端直接返回，等客户端数据准备好之后，会返回selectorKey,然后服务器端继续处理
                clientScoket = echoServer.accept();
                System.out.println(clientScoket.getRemoteSocketAddress() + "connected!");
                tp.execute(new HandleMsg(clientScoket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
