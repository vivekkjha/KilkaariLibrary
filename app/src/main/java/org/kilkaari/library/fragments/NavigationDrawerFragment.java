package org.kilkaari.library.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.MainActivity;
import org.kilkaari.library.adapters.NavigationalDrawerAdapter;
import org.kilkaari.library.application.LibraryApplication;
import org.kilkaari.library.application.Prefs;
import org.kilkaari.library.models.SlideItem;


/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */

    private DrawerLayout mDrawerLayout;

    private View mainView;
    private ListView mDrawerListView;
    private LinearLayout lin_userDetails;
    private View mFragmentContainerView;
    private ImageView img_userImage;
    private TextView txt_userName, txt_userEmail;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private Prefs prefs;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //> get preference in fragment
        prefs =((LibraryApplication)getActivity().getApplication()).getPref();

        mainView = (View) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        img_userImage = (ImageView)mainView.findViewById(R.id.img_userImage);
        txt_userName = (TextView)mainView.findViewById(R.id.txt_userName);
        txt_userEmail = (TextView)mainView.findViewById(R.id.txt_userEmail);
        lin_userDetails = (LinearLayout)mainView.findViewById(R.id.lin_userDetails);

        //> set user name from preferences
        if(prefs.getUserName()!= null)
        {
            txt_userName.setText(prefs.getUserName());
        }

        //> set User email from parse user
        if(ParseUser.getCurrentUser()!= null) {
            txt_userEmail.setText(ParseUser.getCurrentUser().getEmail());
        }


        mDrawerListView = (ListView) mainView.findViewById(R.id.menuList);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        SlideItem[] slideItems;



        if(prefs.isLibrarian()) {
            //> count , item name , icon
            slideItems = new SlideItem[]{
                    new SlideItem(4, getResources().getString(R.string.library), R.drawable.icon_library),
                    new SlideItem(-1, getResources().getString(R.string.savedLinks), R.drawable.icon_saved_pages),
                    new SlideItem(-1, getResources().getString(R.string.requestedBooks), R.drawable.icon_requested),
                    new SlideItem(3, getResources().getString(R.string.toRead), R.drawable.icon_toread),
                    new SlideItem(3, getResources().getString(R.string.read), R.drawable.icon_already_read),
                    new SlideItem(-1, getResources().getString(R.string.librarian), R.drawable.icon_admin),
                    new SlideItem(-1, getResources().getString(R.string.logOut), R.drawable.icon_library)
            };
        }
        else {
            slideItems = new SlideItem[]{
                    new SlideItem(4, getResources().getString(R.string.library), R.drawable.icon_library),
                    new SlideItem(-1, getResources().getString(R.string.savedLinks), R.drawable.icon_saved_pages),
                    new SlideItem(-1, getResources().getString(R.string.requestedBooks), R.drawable.icon_requested),
                    new SlideItem(3, getResources().getString(R.string.toRead), R.drawable.icon_toread),
                    new SlideItem(3, getResources().getString(R.string.read), R.drawable.icon_already_read),
                    new SlideItem(-1, getResources().getString(R.string.logOut), R.drawable.icon_library)

            };

        }


        NavigationalDrawerAdapter navigationBarAdapter = new NavigationalDrawerAdapter(this.getActivity(), slideItems);
        mDrawerListView.setAdapter(navigationBarAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        lin_userDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //> close drawers
                if(isDrawerOpen())
                {
                    mDrawerLayout.closeDrawers();
                }

                //> call show user details dialog for details dialog of current user details
                ((MainActivity)getActivity()).showUserDetailsDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

    }
    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.

    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
