package com.test.simpletest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.test.simpletest.service.TestService;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "hello";
    ITestAidlCallBack.Stub callback = new ITestAidlCallBack.Stub() {
        @Override
        public void CallActivityByService() throws RemoteException {
            Toast.makeText(getApplicationContext(),
                    "CallActivityByService!!",
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(TAG, "CallActivityByService");

        }
    };
    ITestAidlInterface stub;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG, "onServiceConnected : ");
            stub=ITestAidlInterface.Stub.asInterface(service);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn= (Button)findViewById(R.id.btn);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"click");


                try {
                    stub.foo();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        btn.setOnClickListener( onClickListener);
        /* Start ChatActivity
        startActivity(new Intent(this, ChatActivity.class));
         */

        //Intent serviceIntent = new Intent(this, TestService.class);

        Intent intent = new Intent(MainActivity.this, TestService.class);
        bindService(intent , connection, Context.BIND_AUTO_CREATE);


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
