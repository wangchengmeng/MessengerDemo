package com.example.sunddenfix.messengersdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @author wangchengmeng
 * @desc
 * @更新时间
 */

public class MudexApllication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
