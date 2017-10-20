package com.example.simpleclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity {
    private EditText show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (EditText) findViewById(R.id.editText);

        try {
            Socket socket = new Socket("192.168.12.100",30000);
            socket.setSoTimeout(10000);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = br.readLine();
            show.setText("来自服务器的数据:"+line);
            br.close();
            socket.close();
        }catch (UnknownHostException e){
            Log.e("UnknownHost","来自服务器的数据");
            e.printStackTrace();
        }catch (IOException e){
            Log.e("IOException","来自服务器的数据");
            e.printStackTrace();
        }
    }
}
