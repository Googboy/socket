package com.example.simplesocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 潘硕 on 2017/10/20.
 */

public class AndroidService {
    public static void main(String[] args) throws IOException{
        ServerSocket service = new ServerSocket(30000);
        while (true){
            Socket socket  = service.accept();
            new Thread(new AndroidRunable(socket)).start();
        }
    }
}
