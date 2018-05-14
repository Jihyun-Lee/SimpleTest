// ITestAidlInterface.aidl
package com.test.simpletest;

// Declare any non-default types here with import statements
import com.test.simpletest.ITestAidlCallBack;
interface ITestAidlInterface {

    void CallServiceFunction( int a );
    //void receive( ITestAidlCallBack cb );
    void foo();
    boolean registerCallback(ITestAidlCallBack callback);
    boolean unregisterCallback(ITestAidlCallBack callback);
}
