package com.test.simpletest.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class TestThread extends Thread {

    static final String TAG = "hello";
    private Handler mMainHandler;

    public TestThread(Handler handler){
        mMainHandler=handler;

    }

    public TestThread(){

    }


    @Override
    public void run() {
        Looper.prepare();


        Looper.loop();
    }
    public Handler mLocalHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Message m = Message.obtain();
            m.what=10;
            mMainHandler.sendMessage(m);
        }
    };


}
