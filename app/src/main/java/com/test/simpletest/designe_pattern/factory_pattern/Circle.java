package com.test.simpletest.designe_pattern.factory_pattern;

import android.util.Log;

public class Circle implements Shape {

    private final String TAG = Circle.class.getSimpleName();
    @Override
    public void draw() {
        Log.d(TAG, "Circle draw");
    }
}
