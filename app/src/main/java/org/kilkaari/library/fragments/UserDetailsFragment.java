package org.kilkaari.library.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import org.kilkaari.library.R;
import org.kilkaari.library.application.LibraryApplication;
import org.kilkaari.library.application.Prefs;

/**
 * Created by vivek on 29/04/15.
 */
public class UserDetailsFragment extends android.support.v4.app.DialogFragment {


    private View rootView;
    private ImageView img_userImage;
    private TextView txt_userName,txt_userEmail,txt_phone,txt_gender,txt_address;

    private Prefs prefs;

    public static UserDetailsFragment newInstance(String name, String email, String phone,String address,String gender) {
        UserDetailsFragment f = new UserDetailsFragment();


        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("name",name);
        args.putString("email",email);
        args.putString("phone",phone);
        args.putString("address",address);
        args.putString("gender",gender);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = android.support.v4.app.DialogFragment.STYLE_NO_FRAME;
        int theme = android.R.style.Theme_Holo_Light_Dialog_MinWidth;
        setStyle(style, theme);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //> get preference in fragment
        prefs =((LibraryApplication)getActivity().getApplication()).getPref();

        rootView = inflater.inflate(R.layout.layout_user_details,container,false);
        img_userImage = (ImageView)rootView.findViewById(R.id.img_userImage);
        txt_userName = (TextView)rootView.findViewById(R.id.txt_userName);
        txt_userEmail = (TextView)rootView.findViewById(R.id.txt_userEmail);
        txt_phone = (TextView)rootView.findViewById(R.id.txt_phone);
        txt_gender = (TextView)rootView.findViewById(R.id.txt_gender);
        txt_address = (TextView)rootView.findViewById(R.id.txt_address);


        txt_userName.setText(this.getArguments().getString("name"));
        txt_userEmail.setText(this.getArguments().getString("email"));
        txt_phone.setText(this.getArguments().getString("phone"));
        txt_gender.setText(this.getArguments().getString("gender"));
        txt_address.setText(this.getArguments().getString("address"));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
