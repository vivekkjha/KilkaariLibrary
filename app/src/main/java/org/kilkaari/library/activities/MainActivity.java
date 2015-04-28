package org.kilkaari.library.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.kilkaari.library.R;
import org.kilkaari.library.fragments.BookCategoriesFragment;
import org.kilkaari.library.fragments.NavigationDrawerFragment;


public class MainActivity extends BaseActivity  implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    public static String TAG_BOOK_CATEGORIES = "BookCategories";
    public static String TAG_BOOK_DETAILS = "BookDetails";
    public static String TAG_READ_BOOKS = "BookRead";
    public static String TAG_SAVED_LINKS = "SavedLinks";
    public static String TAG_REQUESTED = "Requested";
    public static String TAG_TO_READ_BOOKS = "ToReadBooks";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout drawer;
    private LinearLayout lin_fragments;
    //> fragments objects
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BookCategoriesFragment bookCategoriesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setup slide drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, drawer);

        //> custom fragments to add
        lin_fragments = (LinearLayout)findViewById(R.id.lin_fragments);
        fragmentManager = getSupportFragmentManager();
        bookCategoriesFragment = new BookCategoriesFragment();

        //> open Book Shelf means categories list
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lin_fragments, bookCategoriesFragment,"BookCategories");
        fragmentTransaction.addToBackStack("BookCategories");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(drawer != null && mNavigationDrawerFragment != null && mNavigationDrawerFragment.isDrawerOpen()){
            drawer.closeDrawers();
            return;
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == 0) {


        }
        else if(position == 1){

            startActivity(new Intent(this,SavedPagesActivity.class));

        }
        else if(position == 2){

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lin_fragments, bookCategoriesFragment,"BookCategories");
            fragmentTransaction.addToBackStack("BookCategories");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();

        }
        else if(position == 5)
        {
            startActivity(new Intent(this,LibrarianActivity.class));
        }

        //Toast.makeText(this, "Selected Index : " + position, Toast.LENGTH_SHORT).show();

    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.img_menu)
        {
            drawer.openDrawer(Gravity.START);
        }
        if(v.getId() == R.id.lin_topDone)
        {
           // startActivity(new Intent(this,BooksCategoriesActivity.class));


        }
    }
    public void startBookListFragment()
    {

    }
}
