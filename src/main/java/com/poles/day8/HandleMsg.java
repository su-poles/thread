package com.poles.day8;

import java.io.*;
import java.net.Socket;

/**
*********************************************************************
* 
* @author poles
* @date 2019-06-03 14:11
* @desc 
*
*********************************************************************
*/
public class HandleMsg implements Runnable {
    private Socket clientSocket;
    public HandleMsg(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(clientSocket.getOutputStream(), true);
            String line = null;
            long b = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                pw.println(line);
            }
            long e = System.currentTimeMillis();
            System.out.println("spend:" + (e - b) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
