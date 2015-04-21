package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;

import org.kilkaari.library.R;
import org.kilkaari.library.application.LibraryApplication;
import org.kilkaari.library.application.Prefs;

/**
 * Created by vk on 14-03-2015.
 */
public class BaseActivity extends FragmentActivity {

    public Prefs prefs;
    private LinearLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getPref();

    }

    // > show progress layout
    public void showProgressLayout(){
        progressLayout = (LinearLayout)findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);

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
    public void onBottomClick(View v)
    {
        if(v.getId() == R.id.lin_addBooks)
        {
            startActivity(new Intent(this,AddBooksActivity.class));
        }
        else if(v.getId() == R.id.lin_updateBooks)
        {
            startActivity(new Intent(this,AlertActivity.class));
        }
        else if(v.getId() == R.id.lin_issueBooks)
        {
            startActivity(new Intent(this,IssueActivity.class));
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
