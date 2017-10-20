package com.example.socketdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity implements Runnable {
    private TextView tv_msg = null;
    private EditText ed_msg = null;
    private Button btn_send = null;
    private static final String HOST = "192.168.12.100";
    public static final int PORT = 3000;
    private Socket socket =null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_msg.setText(content);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_msg = (TextView) findViewById(R.id.textView);
        ed_msg = (EditText) findViewById(R.id.editText);
        btn_send = (Button) findViewById(R.id.btnSend);
        try {
            socket = new Socket(HOST,PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        } catch (IOException e) {
            e.printStackTrace();
            ShowDialog("Login exception"+e.getMessage());
        }
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = ed_msg.getText().toString();
                if (socket.isConnected()){
                    if (!socket.isOutputShutdown()){
                        out.println(msg);
                    }
                }
            }
        });
        new Thread(MainActivity.this).start();
    }

    private void ShowDialog(String msg) {
        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public void run() {
        try {
            while (true){
                if (!socket.isClosed()){
                    if (socket.isConnected()){
                        if (!socket.isInputShutdown()){
                            if ((content = in.readLine())!=null){
                                content+="\n";
                                mHandle.sendMessage(mHandle.obtainMessage());
                            }else{

                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
