package com.test.simpletest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.simpletest.service.TestService;
import com.test.simpletest.thread.TestThread;

public class MainActivity extends AppCompatActivity {


    static {
        System.loadLibrary("jnitest");
    }

    public native String getJNIString();
    public native int getJNIInt();
    public native int java_add(int a, int b);
    public native int java_mul(int a, int b);

    static final String TAG = "hello";
    ITestAidlCallBack.Stub callback = new ITestAidlCallBack.Stub() {
        @Override
        public void CallActivityByService() throws RemoteException {
            SimpleTestApplication.showToast("CallActivityByService");
            Log.d(TAG, "CallActivityByService");

        }
    };

    Handler mHandler = new Handler( ){

        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 10) {
                SimpleTestApplication.showToast("Hi");
                mTxt1.setText("hi");
            }

        }
    };

    ITestAidlInterface stub;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG, "onServiceConnected : ");
            stub = ITestAidlInterface.Stub.asInterface(service);
            try {
                stub.registerCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                stub.unregisterCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };


    public TextView mTxt1;
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btn);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        mTxt1 = (TextView)findViewById(R.id.txt1);
        mContext=getApplicationContext();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");


                Log.d(TAG, "getJNIInt : " + getJNIInt());
                Log.d(TAG, "getJNIString : " + getJNIString());
                Log.d(TAG, "java_add : " + java_add(10,10));
                Log.d(TAG, "java_mul : " + java_mul(10,10));

                try {
                    stub.foo();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");
                Message msg = Message.obtain();
                msg.what=1;
                mHandler.sendMessage(msg);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TestThread t = new TestThread(mHandler);
                //t.setDaemon(true);
                t.start();
                t.mLocalHandler.sendMessage(Message.obtain());
;

            }
        });

        /* Start ChatActivity
        startActivity(new Intent(this, ChatActivity.class));
        Intent serviceIntent = new Intent(this, TestService.class);
        */

        Intent intent = new Intent(MainActivity.this, TestService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);


    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
