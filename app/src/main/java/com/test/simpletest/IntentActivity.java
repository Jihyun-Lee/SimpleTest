package com.test.simpletest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class IntentActivity extends Activity {
    static final String CUSTOM_INTENT="com.test.simpletest.SEARCH";
    private final String TAG  = IntentActivity.class.getSimpleName();
    TextView tView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_activity);


        tView= findViewById(R.id.txt);
        tView.setTextSize(30);


        if(getIntent().getAction().toString().equals(CUSTOM_INTENT)){
            Log.d(TAG,"Intent = "+CUSTOM_INTENT);
            activateVoiceInput();
        }



    }

    private void activateVoiceInput(){


        /* Call Activity for Voice Input */
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Oops! Your device doesn't support Speech to Text",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    String yourResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                    tView.setText(yourResult);
                }
                break;
            }
        }
    }
}
