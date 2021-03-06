package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.kilkaari.library.R;
import org.kilkaari.library.application.LibraryApplication;
import org.kilkaari.library.application.Prefs;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.fragments.AlertFragment;
import org.kilkaari.library.fragments.BookDetailsFragment;
import org.kilkaari.library.fragments.UpdateBooksFragment;
import org.kilkaari.library.fragments.UserDetailsFragment;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.SaveDataUtils;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by vk on 14-03-2015.
 */
public class BaseActivity extends FragmentActivity {

    public Prefs prefs;
    public TextView txt_title;
    public LinearLayout lin_topDone;
    private LinearLayout progressLayout;
    private TextView txt_plsWait;
    private PopupMenu popupDetails;
    //> container to add fragment in main Activity and Librarian activity
    private LinearLayout fragmentContainer;
    private FragmentManager fragmentManager;

    private SaveDataUtils saveDataUtils;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getPref();

        saveDataUtils = new SaveDataUtils(this);

    }

    // > show progress layout
    public void showProgressLayout(){
       // public void showProgressLayout(String text){
        progressLayout = (LinearLayout)findViewById(R.id.progress_layout);
        txt_plsWait = (TextView)findViewById(R.id.txt_plsWait);
        progressLayout.setVisibility(View.VISIBLE);
        txt_plsWait.setVisibility(View.VISIBLE);


    }

    // > hide progress layout
    public void hideProgressLayout(){

        if(progressLayout != null) {
            progressLayout.setVisibility(View.GONE);
        }
    }

    public void onBackClick(View v)
    {
        if(v.getId() == R.id.img_back)
        {
            super.onBackPressed();
        }
    }

    // > get application in any child activity of base activity
    public LibraryApplication getLibraryApplication() {
        return ((LibraryApplication) getApplication());
    }

    // > get preferences in any child activity of base activity
    public Prefs getPref() {
        return getLibraryApplication().getPref();
    }

    // > get Book list for any specific category
    public List<BooksModel> getList_books() {
        return getLibraryApplication().getList_books();
    }


    //> get list of requested books filtered by current user
    public List<String> getList_requestBooksCurrentUser() {
        return getLibraryApplication().getList_requestBooksCurrentUser();
    }

    //> Show popup on click of option icon in book list , and click listener on its item
    public void showBookPopupList(View view, final int pos, final boolean isEdit)
    {
        popupDetails = new PopupMenu(this,view);

        if(isEdit)
        {
            popupDetails.getMenuInflater().inflate(R.menu.menu_book_edit,  popupDetails.getMenu());
        }
        else {
            popupDetails.getMenuInflater().inflate(R.menu.menu_book_details, popupDetails.getMenu());
        }

        popupDetails.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().toString().equals(getString(R.string.bookDetails))) {

                    showBookDetails(pos, isEdit);
                   /* Intent intent = new Intent(BaseActivity.this, BookDetailsActivity.class);
                    intent.putExtra(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX, pos);
                    startActivity(intent);*/
                }
                else if(item.getTitle().toString().equals(getString(R.string.editDetails)))
                {
                    //> create bundle to transfer data to fragment
                    Bundle editBundle = new Bundle();
                    editBundle.putInt(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX, pos);
                    editBundle.putBoolean(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT, isEdit);


                    //> open Book Details for editing
                    fragmentManager = getFragmentManagerActivity();
                    UpdateBooksFragment updateBooksFragment = new UpdateBooksFragment();
                    updateBooksFragment.setArguments(editBundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(getFragmentContainer().getId(), updateBooksFragment,"updateBooksFragment");
                    fragmentTransaction.addToBackStack("updateBooksFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.commit();

                }
                else if(item.getTitle().toString().equals(getString(R.string.deleteBook)))
                {
                    //> delete books from the Parse database

                    Intent intent = new Intent(BaseActivity.this, LibrarianCodeDialog.class);
                    intent.putExtra(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX, pos);
                    startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_LIBRARY_CODE);
                }

                return true;
            }
        });
        popupDetails.show();

    }

    //> get Container Fragment
    public LinearLayout getFragmentContainer() {
        return fragmentContainer;
    }

    //> set Container Fragment
    public void setFragmentContainer(LinearLayout fragmentContainer) {
        this.fragmentContainer = fragmentContainer;
    }

    //> get Fragment manager from child Activity
    public FragmentManager getFragmentManagerActivity() {
        return fragmentManager;
    }

    //> set Fragment manager from child Activity
    public void setFragmentManagerActivity(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showBookDetails(int pos, boolean isEdit)
    {
        //> create bundle to transfer data to fragment
        Bundle editBundle = new Bundle();
        editBundle.putInt(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX, pos);
        editBundle.putBoolean(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT, isEdit);

        //> open Book Details Fragment
        fragmentManager = getFragmentManagerActivity();
        BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        bookDetailsFragment.setArguments(editBundle);

        fragmentTransaction.replace(getFragmentContainer().getId(), bookDetailsFragment,"bookDetailsFragment");
        fragmentTransaction.addToBackStack("bookDetailsFragment");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

    public void showAlertDialog(Fragment fragment,String title, int iconResource,String mssg, String okText, String cancelText) {


        fragmentManager = getFragmentManagerActivity();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("alertDialog");
        if (prev != null) {
            fragmentTransaction.remove(prev);
            fragmentManager.popBackStack();
        }
        fragmentTransaction.addToBackStack("alertDialog");

        //> open fragment to get result
        fragment.setTargetFragment(fragment,Constants.REQUEST_CODE.REQUEST_OPEN_ALERT);

        // Create and show the dialog.
        DialogFragment dFragment = AlertFragment.newInstance(title,iconResource,mssg,okText,cancelText);

        // Show DialogFragment
        dFragment.show(fragmentManager, "alertDialog");
    }

    //> method to set heading
    public void setHeading(String title)
    {
       if(txt_title!=null) {
           txt_title.setText(title);
       }
    }

    public void showHideDone(boolean show)
    {
        if(lin_topDone !=null) {
            if (show) {
                lin_topDone.setVisibility(View.VISIBLE);
            } else {
                lin_topDone.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE.REQUEST_LIBRARY_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                //> get bundle and position to delete
                Bundle bundle = data.getExtras();
                int pos = bundle != null ? bundle.getInt(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX): -1;

                //> if bundle has some data
                if(pos!=-1)
                {
                    //> id size of the application list is not 0
                    if(getList_books().size()!=0)
                    {
                        //> get objectId of that book and delete book
                        String objectId = getList_books().get(pos).getObjectId();
                        saveDataUtils.deleteBookBackground(objectId);
                    }

                }

                LogUtil.i("BaseActivity","Deleting ");
            }
        }
    }




}
