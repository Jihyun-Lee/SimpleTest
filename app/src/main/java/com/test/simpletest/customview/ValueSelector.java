package com.test.simpletest.customview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.simpletest.R;



public class ValueSelector extends RelativeLayout{

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    private int REPEAT_INTERNAL_MS = 100;
    Handler mHandler=new Handler();
    public int getMinValue(){
        return minValue;

    }
    public void setMinValue(int minValue){
        this.minValue=minValue;
    }
    public int getMaxValue(){
        return maxValue;
    }
    public void setMaxValue(int maxValue){
        this.maxValue=maxValue;
    }



    public int getValue(){
        return Integer.valueOf(valueTextView.getText().toString());
    }
    public void setValue(int newValue){
        int value = newValue;
        if( value < minValue)
            value=minValue;
        else if (value > maxValue)
            value=maxValue;
        valueTextView.setText(String.valueOf(value));
    }

    View rootView;
    TextView valueTextView;
    View minusButton;
    View plusButton;

    public ValueSelector (Context context){
        super(context);
        init(context);

    }

    public ValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * init method will inflate the layout, get references to all of the child views, and setup the buttons
     * @param context
     */
    private void init (Context context){
        rootView = inflate(context, R.layout.value_selector, this);
        valueTextView = (TextView) rootView.findViewById(R.id.valueTextView);

        minusButton = rootView.findViewById(R.id.minusButton);
        plusButton = rootView.findViewById(R.id.plusButton);


        minusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                decrementValue();
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                incrementValue();
            }
        });
        plusButton.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                mHandler.post(new AutoIncrementer());
                return false;
            }
        });
        minusButton.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                mHandler.post(new AutoDecrementer());
                return false;
            }
        });
    }

    private void decrementValue() {
        int currentVal= Integer.valueOf(valueTextView.getText().toString());
        if (currentVal > minValue )
            valueTextView.setText(String.valueOf(currentVal-1));
    }

    private void incrementValue() {

        int currentVal= Integer.valueOf(valueTextView.getText().toString());
        if( currentVal < maxValue ){
            valueTextView.setText(String.valueOf(currentVal+1));
        }
    }
    private class AutoIncrementer implements Runnable{
        @Override
        public void run() {
            if ( plusButton.isPressed() ){
                incrementValue();
                getHandler().postDelayed( new AutoIncrementer(), REPEAT_INTERNAL_MS);
            }
        }
    }
    private class AutoDecrementer implements Runnable{
        @Override
        public void run() {
            if( minusButton.isPressed() ){
                decrementValue();
                getHandler().postDelayed(new AutoDecrementer(),REPEAT_INTERNAL_MS);
            }
        }
    }
}


