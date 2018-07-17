package com.test.simpletest.customview;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.test.simpletest.R;

import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;

public class ValueBar extends View {


    private int barHeight;
    private int circleRadius;
    private int spaceAfterBar;
    private int circleTextSize;
    private int maxValueTextSize;
    private int labelTextSize;
    private int labelTextColor;
    private int currentValueTextColor;
    private int circleTextColor;
    private int baseColor;
    private int fillColor;
    private String labelText;



    private int maxValue=100;//default
    private int currentValue=50;
    private Paint labelPaint;
    private Paint maxValuePaint;
    private Paint barBasePaint;
    private Paint circlePaint;
    private Paint barFillPaint;
    private Paint currentValuePaint;
    private float valueToDraw;

    public void setMaxValue(int maxValue){
        this.maxValue = maxValue;
        invalidate();
        requestLayout();
    }
    public void setValue( int newValue ){
        int previousValue=currentValue;
        if (newValue < 0){
            currentValue=0;
        } else if ( newValue > maxValue ){
            currentValue=maxValue;
        } else {
            currentValue= newValue;
        }

      /*  if(animation != null)
            animation.cancel();*/

        if(animated){

            animation = ValueAnimator.ofFloat(previousValue, currentValue);
            int changeInValue = Math.abs(previousValue-currentValue);
            long durationToUse = (long)(animationDuration * ((float)changeInValue / (float)maxValue));
            Log.d("hello", "durationToUse ="+durationToUse );
            animation.setDuration(3000l);
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        valueToDraw = (float) valueAnimator.getAnimatedValue();
                        ValueBar.this.invalidate();
                        Log.d("hello","invalidate");
                }
            });
            animation.start();
        } else {
            valueToDraw = currentValue;
        }

        invalidate();
    }

    public ValueBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    private boolean animated;
    private long animationDuration = 400l;
    ValueAnimator animation =null;

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ValueBar,0 ,0);
        barHeight = ta.getDimensionPixelSize(R.styleable.ValueBar_barHeight,0);
        circleRadius = ta.getDimensionPixelSize(R.styleable.ValueBar_circleRadius,0);
        spaceAfterBar = ta.getDimensionPixelSize(R.styleable.ValueBar_spaceAfterBar,0);
        circleTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_circleTextSize, 0);
        maxValueTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_maxValueTextSize, 0);
        labelTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_labelTextSize, 0);
        labelTextColor = ta.getColor(R.styleable.ValueBar_labelTextColor, Color.BLACK);
        currentValueTextColor = ta.getColor(R.styleable.ValueBar_maxValueTextColor, Color.BLACK);
        circleTextColor = ta.getColor(R.styleable.ValueBar_circleTextColor, Color.BLACK);
        baseColor = ta.getColor(R.styleable.ValueBar_baseColor, Color.BLACK);
        fillColor = ta.getColor(R.styleable.ValueBar_fillColor, Color.BLACK);
        labelText = ta.getString(R.styleable.ValueBar_labelText);
        ta.recycle();

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setColor(labelTextColor);
        labelPaint.setTextAlign(Paint.Align.LEFT);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        maxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maxValuePaint.setTextSize(maxValueTextSize);
        maxValuePaint.setColor(currentValueTextColor);
        maxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        maxValuePaint.setTextAlign(Paint.Align.RIGHT);

        barBasePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
        barBasePaint.setColor(baseColor);

        barFillPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
        barFillPaint.setColor(fillColor);

        circlePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(fillColor);


        currentValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentValuePaint.setTextSize(circleTextSize);
        currentValuePaint.setColor(circleTextColor);
        currentValuePaint.setTextAlign(Paint.Align.CENTER);

        setSaveEnabled(true);
        setAnimated(true);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KEYCODE_DPAD_LEFT){
            setValue(currentValue-10);
            return true;
        } else if ( keyCode == KEYCODE_DPAD_RIGHT){
            setValue(currentValue+10);
            return true;
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLabel(canvas);
        drawBar(canvas);
        drawMaxValue(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));

    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        Log.d("hello", "gainFocus = "+gainFocus);
        if ( gainFocus )
            circlePaint.setColor(Color.BLUE);
        else
            circlePaint.setColor(fillColor);


    }

    private int measureHeight(int measureSpec){
        int size = getPaddingTop() + getPaddingBottom();
        size += labelPaint.getFontSpacing();
        float maxValueTextSpacing = maxValuePaint.getFontSpacing();
        size += Math.max(maxValueTextSpacing, Math.max(barHeight, circleRadius * 2));
        return resolveSizeAndState(size, measureSpec,0);
    }
    private int measureWidth(int measureSpec){
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        size += bounds.width();
        return resolveSizeAndState(size, measureSpec,0);
    }


    private void drawLabel(Canvas canvas){
        float x = getPaddingLeft();
        Rect bounds= new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();
        canvas.drawText(labelText, x, y, labelPaint);
    }

    private void drawBar(Canvas canvas){
        String maxValueString = String.valueOf(maxValue);
        Rect maxValueRect =new Rect();
        maxValuePaint.getTextBounds(maxValueString, 0 , maxValueString.length(), maxValueRect);
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - circleRadius - maxValueRect.width() - spaceAfterBar;
        float barCenter = getBarCenter();
        float halfBarheight = barHeight/2;
        float top = barCenter - halfBarheight;
        float bottom =barCenter + halfBarheight;
        float left = getPaddingLeft();
        float right = getPaddingLeft() + barLength;
        RectF rect = new RectF(left,top,right,bottom);
        canvas.drawRoundRect(rect, halfBarheight, halfBarheight, barBasePaint);

        float percentFilled = (float) currentValue / (float) maxValue;
        float fillLength = barLength * percentFilled;
        float fillPosition = left + fillLength;
        RectF fillRect = new RectF(left,top, fillPosition, bottom);
        canvas.drawRoundRect(fillRect, halfBarheight,halfBarheight,barFillPaint);

        canvas.drawCircle(fillPosition, barCenter, circleRadius, circlePaint);


        Rect bounds = new Rect();
        String valueString = String.valueOf(Math.round(currentValue));
        currentValuePaint.getTextBounds(valueString, 0, valueString.length(), bounds);
        float y = barCenter + (bounds.height() / 2);
        canvas.drawText(valueString, fillPosition, y, currentValuePaint);

    }
    private float getBarCenter(){
        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom())/ 2;
        barCenter += getPaddingTop() + .1f * getHeight();//move it down a bit
        return barCenter;
    }
    private void drawMaxValue(Canvas canvas){
        String maxValue = String.valueOf(this.maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValue,0 ,maxValue.length(), maxValueRect);

        float xPos = getWidth() - getPaddingRight();
        float yPos = getBarCenter() + maxValueRect.height()/2;
        canvas.drawText(maxValue, xPos, yPos, maxValuePaint);

    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = currentValue;
        valueToDraw=currentValue;
        Log.d("hello","onSaveInstanceState -> currentValue ="+currentValue);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        currentValue=ss.value;
        Log.d("hello","onRestoreInstanceState -> currentValue ="+currentValue);
    }

}
