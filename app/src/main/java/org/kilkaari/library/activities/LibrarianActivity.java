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

    private LinearLayout lin_fragments;

    //> fragments objects
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentAddBooks fragmentAddBooks;
    private FragmentIssueBooks fragmentIssueBooks;
    private FragmentReturnBooks fragmentReturnBooks;



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
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lin_fragments, fragmentAddBooks);
            fragmentTransaction.addToBackStack("AddBooks");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();

        }
        else if(v.getId() == R.id.lin_updateBooks)
        {
            startActivity(new Intent(this,AlertActivity.class));
        }
        else if(v.getId() == R.id.lin_issueBooks)
        {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lin_fragments,fragmentIssueBooks);
            fragmentTransaction.addToBackStack("IssueBooks");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
        else if(v.getId() == R.id.lin_returnBooks)
        {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lin_fragments,fragmentReturnBooks);
            fragmentTransaction.addToBackStack("ReturnBooks");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }


}
