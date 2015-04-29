package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.kilkaari.library.R;
import org.kilkaari.library.application.LibraryApplication;
import org.kilkaari.library.application.Prefs;
import org.w3c.dom.Text;

/**
 * Created by vk on 14-03-2015.
 */
public class BaseActivity extends FragmentActivity {

    public Prefs prefs;
    private LinearLayout progressLayout;
    private TextView txt_plsWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getPref();

    }

    // > show progress layout
    public void showProgressLayout(){
       // public void showProgressLayout(String text){
        progressLayout = (LinearLayout)findViewById(R.id.progress_layout);
        txt_plsWait = (TextView)findViewById(R.id.txt_plsWait);
        progressLayout.setVisibility(View.VISIBLE);
        txt_plsWait.setVisibility(View.VISIBLE);


    }

    // > hide progress layout
    public void hideProgressLayout(){

        if(progressLayout != null) {
            progressLayout.setVisibility(View.GONE);
        }
    }

    public void onBackClick(View v)
    {
        if(v.getId() == R.id.img_back)
        {
            super.onBackPressed();
        }
    }

    // > get application in any child activity of base activity
    public LibraryApplication getLibraryApplication() {
        return ((LibraryApplication) getApplication());
    }

    // > get preferences in any child activity of base activity
    public Prefs getPref() {
        return getLibraryApplication().getPref();
    }
}
