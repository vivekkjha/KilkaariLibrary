package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import org.kilkaari.library.fragments.BookCategoriesFragment;
import org.kilkaari.library.fragments.FragmentAddBooks;
import org.kilkaari.library.fragments.FragmentIssueBooks;
import org.kilkaari.library.fragments.FragmentReturnBooks;
import org.kilkaari.library.fragments.UserDetailsFragment;
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
    private BookCategoriesFragment bookCategoriesFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);

        lin_fragments = (LinearLayout)findViewById(R.id.lin_fragments);
        fragmentManager = getSupportFragmentManager();

        //> set Container Fragment and Fragment Transaction in BaseActivity
        setFragmentContainer(lin_fragments);
        setFragmentManagerActivity(fragmentManager);

        fragmentAddBooks = new FragmentAddBooks();
        fragmentIssueBooks = new FragmentIssueBooks();
        fragmentReturnBooks = new FragmentReturnBooks();
        bookCategoriesFragment = new BookCategoriesFragment();

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

                Fragment prev = fragmentManager.findFragmentByTag("AddBooks");
                if (prev != null) {
                    fragmentManager.popBackStack();
                }
                fragmentTransaction.addToBackStack("AddBooks");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();



        }
        else if(v.getId() == R.id.lin_updateBooks)
        {
            //startActivity(new Intent(this,AlertActivity.class));

            //> create bundle to transfer data to fragment
            Bundle editBundle = new Bundle();
            editBundle.putBoolean(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT, true);

            //> open Book Shelf means categories list
            fragmentTransaction = fragmentManager.beginTransaction();
            bookCategoriesFragment.setArguments(editBundle);
            fragmentTransaction.replace(R.id.lin_fragments, bookCategoriesFragment,"BookCategories");
            Fragment prev = fragmentManager.findFragmentByTag("BookCategories");
            if (prev != null) {
                fragmentManager.popBackStack();
            }
            fragmentTransaction.addToBackStack("BookCategories");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
        else if(v.getId() == R.id.lin_issueBooks)
        {

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lin_fragments, fragmentIssueBooks);
                fragmentTransaction.addToBackStack("IssueBooks");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();


        }
        else if(v.getId() == R.id.lin_returnBooks)
        {

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.lin_fragments, fragmentReturnBooks);
                fragmentTransaction.addToBackStack("ReturnBooks");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();


        }
    }



    public FragmentAddBooks getFragmentAddBooks() {
        return fragmentAddBooks;
    }

    public FragmentIssueBooks getFragmentIssueBooks() {
        return fragmentIssueBooks;
    }

    public FragmentReturnBooks getFragmentReturnBooks() {
        return fragmentReturnBooks;
    }

    public void showUserDetailsDialog(ParseObject parseObject) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.


        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("UserDetailsDialog");
        if (prev != null) {
            fragmentTransaction.remove(prev);
            fragmentManager.popBackStack();
        }
        fragmentTransaction.addToBackStack("UserDetailsDialog");
/*
        fragmentTransaction.replace(R.id.lin_fragment, dFragment,"DialogFragment");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

        // Create and show the dialog.
        String name  = parseObject.getString(Constants.DataColumns.USER_NAME);
        String email  = parseObject.getString(Constants.DataColumns.USER_EMAIL);
        String phone = parseObject.getString(Constants.DataColumns.USER_PHONE);
        String address = parseObject.getString(Constants.DataColumns.USER_ADDRESS);
        String gender = parseObject.getString(Constants.DataColumns.USER_GENDER);

        DialogFragment dFragment =  UserDetailsFragment.newInstance((name!=null)?name:"",(email!= null)?email:"",
                (phone!=null)?phone:"",(address!=null)?address:"",(gender!=null)?gender:"");

        // Show DialogFragment
        dFragment.show(fragmentManager, "UserDetailsDialog");
    }


}
