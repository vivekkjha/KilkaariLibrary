package org.kilkaari.library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.kilkaari.library.R;
import org.kilkaari.library.activities.LoginActivity;
import org.kilkaari.library.activities.MainActivity;
import org.kilkaari.library.application.LibraryApplication;
import org.kilkaari.library.application.Prefs;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.ValidationUtil;

import java.util.Arrays;

/**
 * Created by vivek on 28/04/15.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    //> layout related objects
    private View rootView;
    private LinearLayout lin_loginFacebook;
    private EditText edt_userEmail,edt_userPassword;
    private TextView txt_login;

    //> program related objects
    private JSONObject facebookResponse;
    private ParseUser pUser;
    private LoginActivity activity;
    private Prefs prefs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_login,container,false);

        //> initialize layout objects
        lin_loginFacebook = (LinearLayout)rootView.findViewById(R.id.lin_loginFacebook);
        edt_userEmail = (EditText)rootView.findViewById(R.id.edt_userEmail);
        edt_userPassword = (EditText)rootView.findViewById(R.id.edt_userPassword);
        txt_login = (TextView)rootView.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);

        lin_loginFacebook.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (LoginActivity)getActivity();
        prefs =((LibraryApplication)getActivity().getApplication()).getPref();
    }

    @Override
    public void onStart() {
        super.onStart();

        //> check if the user is already logged in
        if(ParseUser.getCurrentUser()!=null) {
            if (ParseUser.getCurrentUser().isAuthenticated()) {
                startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lin_loginFacebook)
        {
            //> login with facebook
            loginWithFacebook();
        }
        else if(v.getId() == R.id.txt_login)
        {
            //> apply validation on two
            ValidationUtil.emptyValidation(activity, edt_userEmail,
                    R.string.requiredErrorMessage);
            ValidationUtil.emptyValidation(activity, edt_userPassword,
                    R.string.requiredErrorMessage);
            ValidationUtil.emailValidation(activity, edt_userEmail, R.string.emailErrorMessage);


            if (ValidationUtil.isErrorPresent(edt_userEmail)
                    || ValidationUtil.isErrorPresent(edt_userPassword)) {
                return;
            }
            else
            {
                activity.showProgressLayout();
                logInUser();
            }
        }


    }

    //> method to login with facebook
    public void loginWithFacebook()
    {

        ParseFacebookUtils.logIn(Arrays.asList("public_profile"), activity, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    LogUtil.e("Login Activity", "Uh oh. The user cancelled the Facebook login.");
                    Toast.makeText(activity, R.string.couldNotLoginFB, Toast.LENGTH_SHORT).show();

                } else if (user.isNew()) {
                    LogUtil.w("Login Activity", "User signed up and logged in through Facebook!");
                    pUser = user;
                    getFBData();
                    getLibrarianAuthentication();

                } else {
                    LogUtil.w("Login Activity", "User logged in through Facebook!");
                    pUser = user;
                    getLibrarianAuthentication();

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
                            String name = null;
                            if (facebookResponse.has("email")) {
                                email = facebookResponse.getString("email");
                            }
                            if (facebookResponse.has("name")) {
                                name = facebookResponse.getString("name");
                            }

                            try {
                                // > store data in User table of parse
                                pUser.put(Constants.DataColumns.USER_FACEBOOK_DATA, facebookResponse.toString());
                                if (email != null) {
                                    pUser.put(Constants.DataColumns.USER_EMAIL, email);

                                }
                                if (name != null) {
                                    pUser.put(Constants.DataColumns.USER_NAME, name);

                                }
                                pUser.put(Constants.DataColumns.USER_IS_LIBRARIAN, false);
                                pUser.save();


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

    private void getLibrarianAuthentication()
    {
        if(pUser!=null)
        {
            activity.showProgressLayout();
            pUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if(parseObject != null)
                    {
                        if(parseObject.get(Constants.DataColumns.USER_IS_LIBRARIAN)!=null) {

                            prefs.setIsLibrarian(parseObject.getBoolean(Constants.DataColumns.USER_IS_LIBRARIAN));
                        }
                        if(parseObject.get(Constants.DataColumns.USER_NAME)!=null)
                        {
                            prefs.setUserName(parseObject.getString(Constants.DataColumns.USER_NAME));
                        }
                    }
                    //> Call MainActivity only after setting preference value / getting callback
                    startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                    activity.hideProgressLayout();
                }
            });


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);

    }

    //> login user
    public void logInUser()
    {
        ParseUser.logInInBackground(edt_userEmail.getText().toString(),edt_userPassword.getText().toString(),new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(e== null)
                {
                    Toast.makeText(activity,"Successfully logged In",Toast.LENGTH_SHORT).show();

                    //> get librarian authentication before starting main activity
                    pUser = parseUser;
                    getLibrarianAuthentication();
                }
                else
                {
                    Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                activity.hideProgressLayout();
            }
        });
    }

}
