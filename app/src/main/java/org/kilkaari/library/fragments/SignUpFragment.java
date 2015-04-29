package org.kilkaari.library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.LoginActivity;
import org.kilkaari.library.activities.MainActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.ValidationUtil;
import org.w3c.dom.Text;

/**
 * Created by vivek on 28/04/15.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    //> layout parameters
    private View rootView;
    private EditText edt_userName,edt_userEmail,edt_userPassword,edt_userConfirmPassword,edt_userPhone;
    private TextView txt_gender,txt_signUp;

    //> instance of activity called this fragment
    private LoginActivity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.layout_signup,container,false);

        edt_userName = (EditText)rootView.findViewById(R.id.edt_userName);
        edt_userEmail = (EditText)rootView.findViewById(R.id.edt_userEmail);
        edt_userPassword = (EditText)rootView.findViewById(R.id.edt_userPassword);
        edt_userPhone = (EditText)rootView.findViewById(R.id.edt_userPhone);
        edt_userConfirmPassword = (EditText)rootView.findViewById(R.id.edt_userConfirmPassword);
        txt_gender = (TextView)rootView.findViewById(R.id.txt_gender);
        txt_signUp = (TextView)rootView.findViewById(R.id.txt_signUp);
        txt_signUp.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (LoginActivity)getActivity();

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.txt_signUp)
        {
            ValidationUtil.emptyValidation(activity, edt_userName,
                    R.string.requiredErrorMessage);
            ValidationUtil.emptyValidation(activity, edt_userPassword,
                    R.string.requiredErrorMessage);
            ValidationUtil.compareValidation(activity, edt_userPassword, edt_userConfirmPassword,
                    R.string.passNoMatchErrorMessage);
            ValidationUtil.emailValidation(activity, edt_userEmail, R.string.emailErrorMessage);


            if (ValidationUtil.isErrorPresent(edt_userName)
                    || ValidationUtil.isErrorPresent(edt_userPassword)
                    || ValidationUtil.isErrorPresent(edt_userConfirmPassword)
                    || ValidationUtil.isErrorPresent(edt_userEmail)) {
                return;
            }
            else
            {
                signUpUser();
            }


        }
    }

    public void signUpUser()
    {
        activity.showProgressLayout();
        ParseUser user = new ParseUser();
        user.setUsername(edt_userEmail.getText().toString());
        user.setPassword(edt_userPassword.getText().toString());
        user.setEmail(edt_userEmail.getText().toString());

        //> ad custom data into table
        user.put(Constants.DataColumns.USER_NAME,edt_userName.getText().toString());

        if(!edt_userPhone.getText().toString().equals("")) {
            user.put(Constants.DataColumns.USER_PHONE, edt_userPhone.getText().toString());
        }

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                activity.hideProgressLayout();
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(activity,"Successfully Signed up for KILKAARI",Toast.LENGTH_SHORT).show();

                    //> logged in just singed up user
                    logInUser();

                } else {

                    e.printStackTrace();

                    Toast.makeText(activity, e.getMessage(),Toast.LENGTH_LONG).show();
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }

            }
        });
    }
    public void logInUser()
    {
        ParseUser.logInInBackground(edt_userEmail.getText().toString(),edt_userPassword.getText().toString(),new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(e== null)
                {
                    Toast.makeText(activity,"Successfully logged In",Toast.LENGTH_SHORT).show();

                    //> start Main activity after successful login
                    activity.startActivity(new Intent(activity, MainActivity.class));
                }
                else
                {
                    Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
