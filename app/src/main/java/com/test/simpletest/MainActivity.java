package com.test.simpletest;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.simpletest.designe_pattern.factory_pattern.Shape;
import com.test.simpletest.designe_pattern.factory_pattern.ShapeFactory;
import com.test.simpletest.service.TestService;
import com.test.simpletest.thread.TestThread;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_1;
import static android.view.KeyEvent.KEYCODE_2;
import static android.view.KeyEvent.KEYCODE_3;
import static android.view.KeyEvent.KEYCODE_4;
import static android.view.KeyEvent.KEYCODE_5;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;


public class MainActivity extends AppCompatActivity {


    static {
        System.loadLibrary("jnitest");
    }

    public native String getJNIString();
    public native int getJNIInt();
    public native int java_add(int a, int b);
    public native int java_mul(int a, int b);
    public native int getIntFromTestSo();

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
        Button assistButton = (Button) findViewById(R.id.btn4);
        mTxt1 = (TextView)findViewById(R.id.txt1);
        mContext=getApplicationContext();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShapeFactory shapeFactory= new ShapeFactory();
                Shape a =shapeFactory.getShape("circle");
                a.draw();
                a=shapeFactory.getShape("rectangle");
                a.draw();
                a=shapeFactory.getShape("square");
                a.draw();

               /* Log.d(TAG, "click");
                if ( getRequestedOrientation() ==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT )
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/


                /*Log.d(TAG, "getJNIInt : " + getJNIInt());
                Log.d(TAG, "getJNIString : " + getJNIString());
                Log.d(TAG, "java_add : " + java_add(10,10));
                Log.d(TAG, "java_mul : " + java_mul(10,10));
                Log.d(TAG, "getIntFromTestSo : " + getIntFromTestSo());



                try {
                    stub.foo();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                */
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");
                /*Message msg = Message.obtain();
                msg.what=1;
                mHandler.sendMessage(msg);*/


                startActivity(getCustomIntent());

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
        assistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(Intent.ACTION_ASSIST);
                startActivity(intent);
                */
                isCDHomeActivityTop();
            }
        });





        /* Start ChatActivity
        startActivity(new Intent(this, ChatActivity.class));
        Intent serviceIntent = new Intent(this, TestService.class);
        */

        Intent intent = new Intent(MainActivity.this, TestService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);


    }


    private Intent getCustomIntent(){
        final String START_INTENT = "com.test.simpletest.SEARCH";
        final String ACTIVITY_NAME = "com.test.simpletest.IntentActivity";
        final String HOME_PACKAGE_NAME = "com.test.simpletest";
        Intent intent = new Intent(START_INTENT);
        ComponentName name = new ComponentName(HOME_PACKAGE_NAME, ACTIVITY_NAME);
        intent.setComponent(name);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);*/
        return intent;
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    private boolean isCDHomeActivityTop() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String CDHomeActivityName =  "com.canaldigital.ngp.MainActivity";
        List<ActivityManager.RunningTaskInfo> info;
        info = activityManager.getRunningTasks(1);
        String className = info.get(0).topActivity.getClassName();

        if (className.equals(CDHomeActivityName)) {
            Log.d(TAG, " className : " + className);
            return true;
        }
        Log.d(TAG, " className : " + className);
        return false;

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



    /*key handle*/

    private static final int MAX_VOLUME_RESOLUTION = 15;


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if ( keyCode == KEYCODE_1 ){

        } else if ( keyCode == KEYCODE_2) {
            AudioManager mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
            Log.d(TAG, " maxVol="+mAudioManager.getStreamMaxVolume(6));
            //mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0, 0);

        }else if ( keyCode == KEYCODE_3) {
            //getClassNameFromObject();
            //invokeMethod();
            //createObjectFromClassInstance();
            getConstructorAndCreateInstance();
        }else if ( keyCode == KEYCODE_4) {
            callVoiceSearchTest();
        }
        else if ( keyCode == KEYCODE_5) {
            callVoiceSearchTest();
        }
        return false;
    }


    private void sendKeyEvent(){
        /*new Thread(){
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KEYCODE_SEARCH);
                Log.d(TAG, "send KEYCODE_SEARCH");
            }
        }.start();*/

        try
        {
            String keyCommand = "input keyevent " + KeyEvent.KEYCODE_VOLUME_UP;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sendKeyEvent2(){
        new Thread(){
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KEYCODE_VOLUME_UP);
                Log.d(TAG, "send KEYCODE_VOLUME_UP");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /* When Mic activity close */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    String yourResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                    Toast.makeText(mContext, "result : " + yourResult,Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private void callVoiceSearchTest() {
        /* Call Activity for Voice Input */

        final String START_CD_HOME = "com.canaldigital.ngp.settings.action.OPEN_CD_HUB";
        final String CD_HOME_ACTIVITY_NAME = "com.canaldigital.ngp.MainActivity";
        final String CD_HOME_PACKAGE_NAME = "com.canaldigital.ngp";
        Intent intent = new Intent(START_CD_HOME);
        ComponentName name = new ComponentName(CD_HOME_PACKAGE_NAME, CD_HOME_ACTIVITY_NAME);
        intent.setComponent(name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(mContext, "Oops! Your device doesn't support Speech to Text",Toast.LENGTH_SHORT).show();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendKeyEvent();
            }
        },1000);

    }
    private ComponentName SEARCHABLE_ACTIVITY =            new ComponentName("com.android.frameworks.coretests",                 "android.app.activity.SearchableActivity");
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
        startActivity(intent);
    }

    //Get class name from object
    public void getClassNameFromObject(){

        Foo f = new Foo();
        try {
            Log.d(TAG, f.getClass().getName());
        } catch ( Exception e){
            Log.d(TAG, ""+e);
        }
    }

    //Invoke method on unknown object
    public void invokeMethod(){

        Foo f = new Foo();
        Method m;
        try {
           m= f.getClass().getMethod("printFoo",new Class<?>[0]);
           m.invoke(f);
        } catch ( Exception e){
            Log.d(TAG, ""+e);
        }
    }

    //Create object from Class instance
    public void createObjectFromClassInstance(){

        Class<?> c = null;
        Foo f = null;
        //create instance of Class
        try {
            c = Class.forName("com.test.simpletest.Foo");
        } catch ( Exception e){
            Log.d(TAG, ""+e);
        }
        //Create instance of Foo
        try {
            f = (Foo) c.newInstance();
        } catch ( Exception e){
            Log.d(TAG, ""+e);
        }
        f.printFoo();
    }


    //Get constructor and create instance
    public void getConstructorAndCreateInstance(){

        Class<?> c = null;

        //create instance of Class
        try {
            c = Class.forName("com.test.simpletest.Foo");
        } catch ( Exception e){
            Log.d(TAG, ""+e);
        }
        //Create instance of Foo
        Foo f1 = null;
        Foo f2 = null;

        Constructor<?> cons[] = c.getConstructors();

        //get all constructors
        try {
            f1 = (Foo) cons[0].newInstance();
            f2 = (Foo) cons[1].newInstance("Foo!");
        } catch ( Exception e){
            Log.d(TAG, ""+e);
        }
        f1.printS();
        f2.printS();
    }

    //Change array size though reflection

}

class Foo{
    public String F1 = "This is F1.";
    public String F2 = "This is F2.";
    public String F3 = "This is F3";
    String s;
    public Foo(){}
    public Foo(String s){
        this.s=s;
    }
    public void printFoo(){
        Log.d("hello","Foo!");
    }
    public void printS(){
        Log.d("hello", ""+s);
    }
}