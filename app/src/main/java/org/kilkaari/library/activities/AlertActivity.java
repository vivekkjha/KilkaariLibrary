package org.kilkaari.library.activities;

import android.os.Bundle;
import android.view.View;

import org.kilkaari.library.R;

/**
 * Created by vivek on 17/03/15.
 */
public class AlertActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alert);
    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.txt_alertOk)
        {
            setResult(RESULT_OK);
        }
        else if(v.getId() == R.id.txt_alertCancel)
        {
            setResult(RESULT_CANCELED);
        }
        this.finish();
    }
}
