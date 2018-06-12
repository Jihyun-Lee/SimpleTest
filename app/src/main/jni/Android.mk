LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE := jnitest
LOCAL_SRC_FILES := jnimain.cpp
LOCAL_SRC_FILES += calculator.cpp

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
