package com.test.simpletest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.test.simpletest.fragments.LM_Fragment;
import com.test.simpletest.fragments.PM_Fragment;

public class FragmentActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = getResources().getConfiguration();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Fragment lm=  new LM_Fragment();
            fragmentTransaction.replace(android.R.id.content, lm);
        } else {
            Fragment pm=  new PM_Fragment();
            fragmentTransaction.replace(android.R.id.content, pm);

        }
        fragmentTransaction.commit();
    }

}
