



//#include "com_test_simpletest_MainActivity.h"
#include <jni.h>
#include "log.h"
#include <testjni.h>
#define LOG_TAG "easy"

extern jint add(    JNIEnv *env,    jobject thiz,    jint    a,    jint    b );

extern jint mul(    JNIEnv *env,    jobject thiz,    jint    a,    jint    b );

//JNIEXPORT jstring JNICALL Java_com_test_simpletest_MainActivity_getJNIString(JNIEnv *env, jobject obj) {
JNIEXPORT jstring JNICALL Java_com_test_simpletest_MainActivity_getJNIString(JNIEnv *env, jobject obj) {
    return env->NewStringUTF("Message from jnifunc");
}
JNIEXPORT jint JNICALL Java_com_test_simpletest_MainActivity_getJNIInt(JNIEnv *env, jobject obj) {
    return 9999;
}
JNIEXPORT jint JNICALL testFromNative(JNIEnv *env, jobject obj) {
    return test_int();
}
static JNINativeMethod methodTable[] = {
  {"java_mul", "(II)I", (void *) mul},
  {"java_add", "(II)I", (void *) add},
  {"getJNIString","()Ljava/lang/String;", (void *) Java_com_test_simpletest_MainActivity_getJNIString},
  {"getJNIInt", "()I", (void *) Java_com_test_simpletest_MainActivity_getJNIInt},
  {"getIntFromTestSo", "()I", (void *) testFromNative},

};

//------------------------------------------------------------------------
jint JNI_OnLoad(JavaVM* aVm, void* aReserved)
{
    // cache java VM

    JNIEnv* env;
    if (aVm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK)
    {
        LOGE("Failed to get the environment");
        return -1;
    }

    // 클래스를 찾고 해당 클래스에 네이티브 메서드로 등록한다.
    jclass activityClass = env->FindClass("com/test/simpletest/MainActivity");
    if (!activityClass)
    {
        LOGE("failed to get MainActivity class reference");
        return -1;
    }
    //gJavaActivityClass = env->NewGlobalRef(activityClass);

    // Register methods with env->RegisterNatives.
    env->RegisterNatives(activityClass, methodTable, sizeof(methodTable) / sizeof(methodTable[0]));

    return JNI_VERSION_1_6;
}

