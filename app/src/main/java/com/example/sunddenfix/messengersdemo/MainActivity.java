package com.example.sunddenfix.messengersdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MyServiceConnection mServiceConnection;
    //Messenger实现进程间通信 传输数据主要靠message
    private Messenger           mMessengerHandlServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnBind = (Button) findViewById(R.id.bind);
        Button unBtnBind = (Button) findViewById(R.id.unbind);

        mServiceConnection = new MyServiceConnection();
        mMessengerHandlServer = new Messenger(new MessengerHandler());
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Server.class);
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });
        unBtnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(mServiceConnection);
            }
        });
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("aaa", "链接到服务端了");
            //创建一个Messenger对象
            Messenger messenger = new Messenger(iBinder);
            Message message = Message.obtain(null, Constant.KEY_MESSAGE_FORM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("client", "hello,I am client");
            message.setData(bundle);

            //要接收到server的回复 关键的一句话
            message.replyTo = mMessengerHandlServer;

            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    //也需要处理收到server端的消息
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.KEY_MESSAGE_FORM_SERVER:
                    Log.d("aaa", "accept msg from server:" + msg.getData().getString("server"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
