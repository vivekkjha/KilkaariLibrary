package org.kilkaari.library.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.kilkaari.library.R;
import org.kilkaari.library.application.LibraryApplication;
import org.kilkaari.library.application.Prefs;
import org.kilkaari.library.constants.Constants;

/**
 * Created by vivek on 29/04/15.
 */
public class AlertFragment extends android.support.v4.app.DialogFragment {


    private View rootView;
    private ImageView img_alertIcon;
    private TextView txt_alertTitle,txt_alertText,txt_alertOk,txt_alertCancel;

    private Prefs prefs;

    public static AlertFragment newInstance(String titleText, int iconResource, String message,String okText,String cancelText) {
        AlertFragment f = new AlertFragment();


        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("titleText",titleText);
        args.putInt("iconResource",iconResource);
        args.putString("message",message);
        args.putString("okText",okText);
        args.putString("cancelText",cancelText);
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

        int iconResource = this.getArguments().getInt("iconResource");
        String titleText = this.getArguments().getString("titleText");
        String message = this.getArguments().getString("message");
        String okText = this.getArguments().getString("okText");
        String cancelText = this.getArguments().getString("cancelText");

        rootView = inflater.inflate(R.layout.layout_alert,container,false);

        img_alertIcon = (ImageView)rootView.findViewById(R.id.img_alertIcon);
        txt_alertTitle = (TextView)rootView.findViewById(R.id.txt_alertTitle);
        txt_alertText = (TextView)rootView.findViewById(R.id.txt_alertText);
        txt_alertOk = (TextView)rootView.findViewById(R.id.txt_alertOk);
        txt_alertCancel = (TextView)rootView.findViewById(R.id.txt_alertCancel);

        //> if there is no text for cancel , then don't show this button
        if(cancelText== null)
        {
            txt_alertCancel.setVisibility(View.GONE);
        }

        img_alertIcon.setImageDrawable(getActivity().getResources().getDrawable(iconResource));
        txt_alertTitle.setText(titleText);
        txt_alertText.setText(message);
        txt_alertOk.setText(okText);
        txt_alertCancel.setText(cancelText);

        txt_alertOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(Constants.REQUEST_CODE.REQUEST_OPEN_ALERT,1,null);
            }
        });

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
