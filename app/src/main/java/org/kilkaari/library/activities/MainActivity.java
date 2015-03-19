package org.kilkaari.library.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.kilkaari.library.R;
import org.kilkaari.library.fragments.NavigationDrawerFragment;


public class MainActivity extends BaseActivity  implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout drawer;


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

        }
        else if(position == 2){


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
            startActivity(new Intent(this,BooksCategoriesActivity.class));
        }
    }
}
