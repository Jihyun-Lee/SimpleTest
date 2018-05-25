package com.test.simpletest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.test.simpletest.ITestAidlCallBack;
import com.test.simpletest.ITestAidlInterface;

public class TestService extends Service {
    private static final String TAG = "hello";
    ITestAidlCallBack mCallback;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,    "service onBind");
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,    "service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,    "service onStartCommand");
        Toast.makeText(this, "service onStartCommand", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
    private ITestAidlInterface.Stub stub = new ITestAidlInterface.Stub() {

        @Override
        public void CallServiceFunction(int a) throws RemoteException {
            Log.d(TAG, "CallServiceFunction : ");
        }

        @Override
        public void foo() throws RemoteException {
            Log.d(TAG, "foo : ");
            mCallback.CallActivityByService();
        }

        @Override
        public boolean registerCallback(ITestAidlCallBack callback) throws RemoteException {
            Log.d(TAG, "registerCallback ");
            mCallback=callback;
            return true;
        }

        @Override
        public boolean unregisterCallback(ITestAidlCallBack callback) throws RemoteException {
            Log.d(TAG, "unregisterCallback");
            mCallback=null;
            return true;
        }

    };

    private void goo(){
        return;
    }
    private void foo(){
        goo();
    }

}
