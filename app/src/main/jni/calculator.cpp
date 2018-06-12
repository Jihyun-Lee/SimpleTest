#include <jni.h>

jint add
(
    JNIEnv *env,
    jobject thiz,
    jint    a,
    jint    b
)
{
    return a + b;
}

jint mul
(
    JNIEnv *env,
    jobject thiz,
    jint    a,
    jint    b
)
{
    return a * b;
}