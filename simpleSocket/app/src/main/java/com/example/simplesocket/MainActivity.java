package com.example.simplesocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainActivity extends Activity {
    Socket socket = null;
    String buffer = "";
    TextView txt1;
    Button send;
    EditText ed1;
    String geted1;
    private Handler myHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11){
                Bundle bundle = msg.getData();
                txt1.append("server:"+bundle.getString("msg")+"\n");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt1 = (TextView) findViewById(R.id.txt1);
        send = (Button) findViewById(R.id.send);
        ed1 = (EditText) findViewById(R.id.ed1);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geted1 = ed1.getText().toString();
                txt1.append("client:"+geted1+"\n");
                new MyThread(geted1).start();
            }
        });
    }

    private class MyThread extends Thread{
        public String txt1;
        public MyThread(String str) {
            txt1 = str;
        }

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.12.100",30000),5000);
                OutputStream output = socket.getOutputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = null;
                buffer = "";
                while ((line = buff.readLine())!=null){
                    buffer = line +buffer;
                }
                output.write("android客户端".getBytes("gbk"));
                output.flush();
                bundle.putString("msg",buffer.toString());
                msg.setData(bundle);
                myHandle.sendMessage(msg);
                buff.close();
                output.close();
                socket.close();
            } catch (SocketTimeoutException aa) {
                bundle.putString("msg","服务器连接失败！请检查网络是否打开");
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
}
