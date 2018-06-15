LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE := jnitest
LOCAL_SRC_FILES := jnimain.cpp
LOCAL_SRC_FILES += calculator.cpp


LOCAL_SHARED_LIBRARIES := test_jni
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := test_jni
LOCAL_SRC_FILES := $(LOCAL_PATH)/libtest/libtest_jni.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/libtest/include
include $(PREBUILT_SHARED_LIBRARY)



#include $(CLEAR_VARS)

#LOCAL_MODULE := test_jni
#LOCAL_SRC_FILES := test_jni.c
#LOCAL_LDLIBS := -llog
#include $(BUILD_SHARED_LIBRARY)


