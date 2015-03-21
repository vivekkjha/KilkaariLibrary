package org.kilkaari.library.application;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vaibhav on 30/05/14.
 */
public class Prefs {
    /**
     * The Constant sharedPrefsName.
     */
    private static final String sharedPrefsName = "AppPrefs";


    private static final String BOOKS_CATEGORIES = "booksCategories";



    /*--- Application Preferences --*/

    private static final String IS_LOGGED_IN = "isLogggedIn";

    /**
     * The m_context.
     */
    private Context context;

    /**
     * Instantiates a new prefs.
     *
     * @param context the context
     */
   public Prefs(Context context) {
        this.context = context;
    }

    /**
     * Gets the.
     *
     * @return the shared preferences
     */
    private SharedPreferences get() {
        return context.getSharedPreferences(sharedPrefsName,
                Context.MODE_PRIVATE);
    }


    public String getBooksCategories() {
        return get().getString(BOOKS_CATEGORIES, null);
    }

    public void setBooksCategories(String booksCategories) {
        get().edit().putString(BOOKS_CATEGORIES, booksCategories).commit();
    }


    public boolean isLoggedIn() {
        return get().getBoolean(IS_LOGGED_IN,false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        get().edit().putBoolean(IS_LOGGED_IN, isLoggedIn).commit();
    }

}
