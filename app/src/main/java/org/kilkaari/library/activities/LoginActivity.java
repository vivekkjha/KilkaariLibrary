package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.fragments.BookCategoriesFragment;
import org.kilkaari.library.fragments.LoginFragment;
import org.kilkaari.library.fragments.SignUpFragment;
import org.kilkaari.library.utils.LogUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vivek on 31/03/15.
 */
public class LoginActivity extends BaseActivity {

    private LinearLayout lin_fragmentsLogin;
    //> fragments objects
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //> custom fragments to add
        lin_fragmentsLogin = (LinearLayout)findViewById(R.id.lin_fragmentsLogin);
        fragmentManager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        signUpFragment = new SignUpFragment();

        //> open Login Fragment at first
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lin_fragmentsLogin, loginFragment, "loginFragment");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();


    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.txt_signUp)
        {
            //> open Sign up Fragment
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lin_fragmentsLogin, signUpFragment,"signUpFragment");
            fragmentTransaction.addToBackStack("signUpFragment");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }




}
