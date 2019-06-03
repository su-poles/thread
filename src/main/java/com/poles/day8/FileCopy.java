package com.poles.day8;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
*********************************************************************
* 
* @author poles
* @date 2019-06-03 10:37
* @desc 
*
*********************************************************************
*/
public class FileCopy {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(new File("/Users/liyanlong/IdeaProjects/zlearn/thread/src/main/java/com/poles/day8/tempA/Java并发工具包concurrent用户指南.pdf"));
        FileOutputStream fos = new FileOutputStream(new File("/Users/liyanlong/IdeaProjects/zlearn/thread/src/main/java/com/poles/day8/tempB/Java并发工具包concurrent用户指南.pdf"));
        FileChannel readChannel = fis.getChannel();
        FileChannel writeChannel = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(true){
            buffer.clear();
            int len = readChannel.read(buffer);
            if (len == -1) {
                System.out.println("复制完成！");
                break;
            }
            //做一个读写转化，说白了就是讲position, limit等指针重置一下
            buffer.flip();
            writeChannel.write(buffer);
        }

        readChannel.close();
        writeChannel.close();
        fos.close();
        fis.close();
    }
}
