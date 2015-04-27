package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.fragments.FragmentAddBooks;
import org.kilkaari.library.fragments.FragmentIssueBooks;
import org.kilkaari.library.fragments.FragmentReturnBooks;
import org.kilkaari.library.utils.LogUtil;

import java.util.List;

/**
 * Created by vivek on 19/03/15.
 */
public class LibrarianActivity extends BaseActivity {

    //> linear layout to add fragments in activity
    private LinearLayout lin_fragments;

    //> fragments objects
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentAddBooks fragmentAddBooks;
    private FragmentIssueBooks fragmentIssueBooks;
    private FragmentReturnBooks fragmentReturnBooks;

    //> flags to prevent same fragment to load again and again
    private boolean isAddBooks  = true,isUpdate = true, isIssue = true, isReturn = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);

        lin_fragments = (LinearLayout)findViewById(R.id.lin_fragments);

        fragmentAddBooks = new FragmentAddBooks();
        fragmentIssueBooks = new FragmentIssueBooks();
        fragmentReturnBooks = new FragmentReturnBooks();

        fragmentManager = getSupportFragmentManager();



    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.lin_topDone)
        {

            //> save entered category ind atabase
            fragmentAddBooks.saveCategories();

            //> save details of book entered
            fragmentAddBooks.saveBookDetails();
        }
    }

    public void onBottomClick(View v)
    {
        if(v.getId() == R.id.lin_addBooks)
        {
            if(isAddBooks) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lin_fragments, fragmentAddBooks);
                fragmentTransaction.addToBackStack("AddBooks");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();

                isAddBooks = false;
                isIssue = true;
                isReturn =  true;
            }

        }
        else if(v.getId() == R.id.lin_updateBooks)
        {
            startActivity(new Intent(this,AlertActivity.class));
        }
        else if(v.getId() == R.id.lin_issueBooks)
        {
            if(isIssue) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lin_fragments, fragmentIssueBooks);
                fragmentTransaction.addToBackStack("IssueBooks");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();

                isAddBooks = true;
                isIssue = false;
                isReturn =  true;
            }
        }
        else if(v.getId() == R.id.lin_returnBooks)
        {
            if(isReturn) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lin_fragments, fragmentReturnBooks);
                fragmentTransaction.addToBackStack("ReturnBooks");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();

                isAddBooks = true;
                isIssue = true;
                isReturn =  false;

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isAddBooks = true;
        isIssue = true;
        isReturn =  true;

    }
}
