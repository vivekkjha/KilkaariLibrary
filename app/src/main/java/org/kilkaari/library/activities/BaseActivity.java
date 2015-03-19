package org.kilkaari.library.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import org.kilkaari.library.R;

/**
 * Created by vk on 14-03-2015.
 */
public class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onBackClick(View v)
    {
        if(v.getId() == R.id.img_back)
        {
            super.onBackPressed();
        }
    }
    public void onBottomClick(View v)
    {


    }
}
