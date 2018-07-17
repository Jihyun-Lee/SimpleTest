package com.test.simpletest.designe_pattern.factory_pattern;

import android.util.Log;

public class Rectangle implements Shape {
    private final String TAG = Rectangle.class.getSimpleName();
    @Override
    public void draw() {
        Log.d(TAG, "Rectangle draw");
    }
}
