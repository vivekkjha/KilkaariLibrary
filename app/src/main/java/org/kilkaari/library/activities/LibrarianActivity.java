package org.kilkaari.library.activities;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.LogUtil;

import java.util.List;

/**
 * Created by vivek on 19/03/15.
 */
public class LibrarianActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);


    }


}
