package com.example.simpleclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 潘硕 on 2017/10/20.
 */

public class SimpleServer {
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(30000);
        while (true){
            Socket s = ss.accept();
            OutputStream os = s.getOutputStream();
            os.write("你好，你收到了来自系统发来的消息!\n".getBytes("utf-8"));
            os.close();
            s.close();
        }
    }
}
