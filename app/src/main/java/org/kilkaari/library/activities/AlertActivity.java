package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;

/**
 * Created by vivek on 17/03/15.
 */
public class AlertActivity extends BaseActivity {

    private String title,okText, cancelText,mssg ;
    private int iconResource;

    private TextView txt_alertTitle,txt_alertText,txt_alertOk,txt_alertCancel;
    private ImageView img_alertIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alert);

        txt_alertTitle = (TextView)findViewById(R.id.txt_alertTitle);
        txt_alertText = (TextView)findViewById(R.id.txt_alertText);
        txt_alertOk = (TextView)findViewById(R.id.txt_alertOk);
        txt_alertCancel = (TextView)findViewById(R.id.txt_alertCancel);
        img_alertIcon = (ImageView)findViewById(R.id.img_alertIcon);

       
        title = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS_ALERT_TITLE);
        okText = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS_ALERT_OK_TEXT);
        cancelText = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS_ALERT_CANCEL_TEXT);
        mssg = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS_ALERT_MSSG);
        iconResource = getIntent().getIntExtra(Constants.EXTRAS.EXTRAS_ALERT_ICON_RESOURCE,-1);

        txt_alertTitle.setText(title);
        txt_alertText.setText(mssg);
        txt_alertOk.setText(okText);
        txt_alertCancel.setText(cancelText);

        img_alertIcon.setImageDrawable(getResources().getDrawable(iconResource));

    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.txt_alertOk)
        {
            setResult(Constants.RESULT_YES);
        }
        else if(v.getId() == R.id.txt_alertCancel)
        {
            setResult(RESULT_CANCELED);
        }
        this.finish();
    }
}
