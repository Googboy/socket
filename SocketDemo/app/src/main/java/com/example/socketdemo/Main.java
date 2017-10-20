package com.example.socketdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 潘硕 on 2017/10/20.
 */

public class Main {
    private static final int PORT = 9999;
    private List<Socket> mList = new ArrayList<Socket>();
    private ServerSocket serverSocket = null;
    private ExecutorService mExecutorService = null;

    public static void main(String[] args){
        new Main();
    }
    public Main(){
        try {
            serverSocket = new ServerSocket(PORT);
            mExecutorService = Executors.newCachedThreadPool();
            System.out.println("服务器已经启动....");
            Socket client = null;
            while (true){
                client = serverSocket.accept();
                mList.add(client);
                mExecutorService.execute(new Service(client));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";
        public Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                msg = "服务器地址:"+this.socket.getInetAddress()+"come toal:"+mList.size()+"(服务器发送)";
                this.sendmsg();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendmsg() {
            System.out.println(msg);
            int num = mList.size();
            for (int index = 0;index<num;index++){
                Socket mSocket = mList.get(index);
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())),true);
                    pout.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            try {
                while (true){
                    if ((msg = in.readLine())!=null){
                        if (msg.equals("exit")){
                            System.out.println("ssssss");
                            mList.remove(socket);
                            in.close();
                            msg = "user:"+socket.getInetAddress()+"exit total:"+mList.size();
                            socket.close();
                            this.sendmsg();
                            break;
                        }else {
                            msg = socket.getInetAddress()+":"+msg+"(服务器发送)";
                            this.sendmsg();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
