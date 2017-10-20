package com.example.simplesocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by 潘硕 on 2017/10/20.
 */

class AndroidRunable implements Runnable {
     Socket socket = null;

    public AndroidRunable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String line = null;
        InputStream input;
        OutputStream output;
        String str = "Hello world";
        try {
            output = socket.getOutputStream();
            input = socket.getInputStream();
            BufferedReader buff = new BufferedReader(new InputStreamReader(input));
            output.write(str.getBytes("gbk"));
            output.flush();
            socket.shutdownOutput();
            while ((line = buff.readLine())!=null){
                System.out.println(line);
            }
            output.close();
            buff.close();
            input.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
