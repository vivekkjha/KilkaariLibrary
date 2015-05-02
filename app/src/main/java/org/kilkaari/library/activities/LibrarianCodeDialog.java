package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.codec.binary.Base64;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.LogUtil;

/**
 * Created by vivek on 28/04/15.
 */
public class LibrarianCodeDialog extends BaseActivity implements View.OnClickListener {

    TextWatcher watcher1 = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {

            if(edt_safeCode1.getText().toString().length()==1)
            {
                edt_safeCode2.requestFocus();
            }

        }
    };
    TextWatcher watcher2= new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {



        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(edt_safeCode2.getText().toString().length()==1)
            {
                edt_safeCode3.requestFocus();
            }
        }
    };
    TextWatcher watcher3 = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {



        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(edt_safeCode3.getText().toString().length()==1)
            {
                edt_safeCode4.requestFocus();
            }
        }
    };
    View.OnFocusChangeListener ofcListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if((v.getId()== R.id.edt_safeCode1 || v.getId() == R.id.edt_safeCode2 || v.getId()== R.id.edt_safeCode3 || v.getId() == R.id.edt_safeCode4) && hasFocus)
            {
                txt_enterCorrectCode.setVisibility(View.INVISIBLE);

            }

        }
    };


    private TextView txt_alertOk,txt_alertCancel,txt_enterCorrectCode;
    private EditText edt_safeCode1,edt_safeCode2,edt_safeCode3,edt_safeCode4;
    private LinearLayout lin_loading;
    private int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pos = getIntent().getIntExtra(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX,-1);

        setContentView(R.layout.layout_librarian_code);

        txt_alertOk = (TextView)findViewById(R.id.txt_alertOk);
        txt_alertOk.setOnClickListener(this);

        txt_alertCancel = (TextView)findViewById(R.id.txt_alertCancel);
        txt_alertCancel.setOnClickListener(this);

        txt_enterCorrectCode = (TextView)findViewById(R.id.txt_enterCorrectCode);
        lin_loading = (LinearLayout)findViewById(R.id.lin_loading);

        edt_safeCode1 = (EditText)findViewById(R.id.edt_safeCode1);
        edt_safeCode2 = (EditText)findViewById(R.id.edt_safeCode2);
        edt_safeCode3 = (EditText)findViewById(R.id.edt_safeCode3);
        edt_safeCode4 = (EditText)findViewById(R.id.edt_safeCode4);

        edt_safeCode1.requestFocus();

        edt_safeCode1.addTextChangedListener(watcher1);
        edt_safeCode2.addTextChangedListener(watcher2);
        edt_safeCode3.addTextChangedListener(watcher3);


        edt_safeCode1.setOnFocusChangeListener(ofcListener);
        edt_safeCode2.setOnFocusChangeListener(ofcListener);
        edt_safeCode3.setOnFocusChangeListener(ofcListener);
        edt_safeCode4.setOnFocusChangeListener(ofcListener);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.txt_alertOk)
        {
            //>show progress layout
            lin_loading.setVisibility(View.VISIBLE);
            checkAuthenticity(edt_safeCode1.getText().toString() + edt_safeCode2.getText().toString() + edt_safeCode3.getText().toString() + edt_safeCode4.getText().toString());

        }
        else if(v.getId() == R.id.txt_alertCancel)
        {
            setResult(RESULT_CANCELED);
            this.finish();

        }
    }

    private void checkAuthenticity(String code)
    {

        //> get encoded string
        final String encoded  = convertIntoBase64(code);


        if(ParseUser.getCurrentUser()!=null)
        {
            ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {

                    //>hide progress layout
                    lin_loading.setVisibility(View.GONE);

                    if(parseObject!=null)
                    {
                        //> compare code with encoded value from server
                        if(encoded.equals(parseObject.getString("code")))
                        {
                            //> prepare bundle to send data back to calling activity
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX,pos);
                            Intent myIntent = new Intent(LibrarianCodeDialog.this, BaseActivity.class);
                            myIntent.putExtras(bundle);
                            LibrarianCodeDialog.this.setResult(RESULT_OK,myIntent);
                            LibrarianCodeDialog.this.finish();
                        }
                        else
                        {
                            txt_enterCorrectCode.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        txt_enterCorrectCode.setVisibility(View.VISIBLE);
                        LibrarianCodeDialog.this.setResult(RESULT_CANCELED);
                        LibrarianCodeDialog.this.finish();
                    }
                }
            });
        }


    }

    //> get code into base64 format
    private String convertIntoBase64(String code)
    {
        byte[] encodedBytes = Base64.encodeBase64(code.getBytes());
        LogUtil.e("Librarian Code Activity","Base 64 Encoded : " + new String(encodedBytes));

        return new String(encodedBytes);
    }


}
