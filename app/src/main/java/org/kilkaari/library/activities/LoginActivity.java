package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
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
import org.kilkaari.library.utils.LogUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vivek on 31/03/15.
 */
public class LoginActivity extends BaseActivity {

    //> layout related objects
    private LinearLayout lin_loginFacebook,lin_loginTwitter;

    //> program related objects
    private JSONObject facebookResponse;
    private ParseUser pUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //> initialize layout objects
        lin_loginFacebook = (LinearLayout)findViewById(R.id.lin_loginFacebook);
        lin_loginTwitter = (LinearLayout)findViewById(R.id.lin_loginTwitter);

        //> check if the user is already logged in
        if(ParseUser.getCurrentUser()!=null) {
            if (ParseUser.getCurrentUser().isAuthenticated()) {
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            }
        }

    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.lin_loginFacebook)
        {
            //> login with facebook
            loginWithFacebook();
        }
    }

    //> method to login with facebook
    public void loginWithFacebook()
    {

        ParseFacebookUtils.logIn(Arrays.asList("public_profile"), this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    LogUtil.e("Login Activity", "Uh oh. The user cancelled the Facebook login.");
                    Toast.makeText(LoginActivity.this, R.string.couldNotLoginFB, Toast.LENGTH_SHORT).show();

                } else if (user.isNew()) {
                    LogUtil.w("Login Activity", "User signed up and logged in through Facebook!");
                    pUser = user;
                    getFBData();

                } else {
                    LogUtil.w("Login Activity", "User logged in through Facebook!");
                    pUser = user;
                    getFBData();
                }
            }
        });

    }

    // > get public profile data from  facebook and store it on Parse server
    public void getFBData()
    {
        if(ParseFacebookUtils.getSession()!=null)
        {
            Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // Display the parsed user info
                        LogUtil.e("Facebook Data ", "Data :-" + user);

                        try {
                            facebookResponse = new JSONObject(response.getRawResponse());

                            String email = null;
                            if (facebookResponse.has("email")) {
                                email = facebookResponse.getString("email");
                            }

                            try {
                                // > store data in User table of parse
                                pUser.put(Constants.DataColumns.USER_FACEBOOK_DATA, facebookResponse.toString());
                                if (email != null) {
                                    pUser.put(Constants.DataColumns.USER_EMAIL, email);
                                }
                                pUser.save();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }


}
