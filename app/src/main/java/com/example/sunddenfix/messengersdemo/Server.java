package com.example.sunddenfix.messengersdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author wangchengmeng
 */

public class Server extends Service {

    private Messenger mMessenger;

    //处理客户端发送过来的消息
    private static class MessegerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.KEY_MESSAGE_FORM_CLIENT:
                    Log.d("aaa", "收到了客户端的消息了");
                    Log.d("aaa", "server has accepted" + msg.getData().getString("client"));

                    //收到客户端的消息后  并回应客户端
                    Messenger server = msg.replyTo;//***需要用这个属性来进行创建Messenger对象进行回应
                    //同理客户端发送消息逻辑
                    Message message = Message.obtain(null, Constant.KEY_MESSAGE_FORM_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString("server", "I  have accepted your message,client");
                    message.setData(bundle);
                    try {
                        server.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //根据handler类创建一个Messenger对象
        mMessenger = new Messenger(new MessegerHandler());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMessenger = null;
    }
}
