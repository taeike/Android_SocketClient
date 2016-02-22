package com.example.taeik.android_socketclient;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity implements OnClickListener{

    Button btn;
    TextView textView;
    EditText editText;
    Button btn_send;

    Socket client;
    String ip = "192.168.0.11";
    int port = 7777;

    Thread thread;

    ClientThread clientThread;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.btn);
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        btn_send = (Button)findViewById(R.id.btn_send);

        handler = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                textView.append(bundle.getString("msg")+"\n");

            }
        };
        btn.setOnClickListener(this);
        btn_send.setOnClickListener(this);

    }

    public void connect(){

        thread = new Thread(){
            public void run() {
                super.run();
                try {
                    client = new Socket(ip, port);
                    clientThread = new ClientThread(client, handler);
                    clientThread.start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn){
            connect();
        }
        if(v.getId()==R.id.btn_send){
            clientThread.send(editText.getText().toString());
            editText.setText("");
        }
    }
}