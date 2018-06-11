package com.test.simpletest;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class SimpleTestApplication extends Application {


    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext=getApplicationContext();

    }

    public static Context getAppContext(){
        return mContext;
    }
    public static void showToast(String msg){
        Toast.makeText(mContext,"DEBUG:"+msg,Toast.LENGTH_SHORT).show();
    }
}
