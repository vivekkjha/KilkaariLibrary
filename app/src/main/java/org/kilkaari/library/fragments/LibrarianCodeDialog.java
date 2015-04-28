package org.kilkaari.library.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.codec.binary.Base64;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.MainActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.RequestQueueModel;
import org.kilkaari.library.utils.LogUtil;

import java.util.List;

/**
 * Created by vivek on 28/04/15.
 */
public class LibrarianCodeDialog extends DialogFragment implements View.OnClickListener {

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
    private MainActivity activity;
    private View rootView;
    private TextView txt_alertOk,txt_alertCancel,txt_enterCorrectCode;
    private EditText edt_safeCode1,edt_safeCode2,edt_safeCode3,edt_safeCode4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.layout_librarian_code,container,false);

        txt_alertOk = (TextView)rootView.findViewById(R.id.txt_alertOk);
        txt_alertOk.setOnClickListener(this);

        txt_alertCancel = (TextView)rootView.findViewById(R.id.txt_alertCancel);
        txt_alertCancel.setOnClickListener(this);

        txt_enterCorrectCode = (TextView)rootView.findViewById(R.id.txt_enterCorrectCode);
        edt_safeCode1 = (EditText)rootView.findViewById(R.id.edt_safeCode1);
        edt_safeCode2 = (EditText)rootView.findViewById(R.id.edt_safeCode2);
        edt_safeCode3 = (EditText)rootView.findViewById(R.id.edt_safeCode3);
        edt_safeCode4 = (EditText)rootView.findViewById(R.id.edt_safeCode4);

        edt_safeCode1.addTextChangedListener(watcher1);
        edt_safeCode2.addTextChangedListener(watcher2);
        edt_safeCode3.addTextChangedListener(watcher3);


        edt_safeCode1.setOnFocusChangeListener(ofcListener);
        edt_safeCode2.setOnFocusChangeListener(ofcListener);
        edt_safeCode3.setOnFocusChangeListener(ofcListener);
        edt_safeCode4.setOnFocusChangeListener(ofcListener);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (MainActivity)getActivity();

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.txt_alertOk)
        {
            checkAuthenticity(edt_safeCode1.getText().toString() + edt_safeCode2.getText().toString()+ edt_safeCode3.getText().toString()+ edt_safeCode4.getText().toString());
        }
        else if(v.getId() == R.id.txt_alertCancel)
        {
            dismiss();
        }
    }

    private void checkAuthenticity(String code)
    {
        final String encoded  = convertIntoBase64(code);


        if(ParseUser.getCurrentUser()!=null)
        {
            ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {

                    if(parseObject!=null)
                    {
                        if(encoded.equals(parseObject.getString("code")))
                        {

                        }
                    }
                }
            });
        }


    }

    private String convertIntoBase64(String code)
    {
        byte[] encodedBytes = Base64.encodeBase64(code.getBytes());
        LogUtil.e("Librarian Code Activity","Base 64 Encoded : " + new String(encodedBytes));

        return new String(encodedBytes);
    }


}
