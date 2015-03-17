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


    private static final String INSTALLATION_NURSERY_ID = "nurseryId";


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


    public String getInstallationNurseryId() {
        return get().getString(INSTALLATION_NURSERY_ID, null);
    }

    public void setInstallationNurseryId(String installationNurseryId) {
        get().edit().putString(INSTALLATION_NURSERY_ID, installationNurseryId).commit();
    }


    public boolean isLoggedIn() {
        return get().getBoolean(IS_LOGGED_IN,false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        get().edit().putBoolean(IS_LOGGED_IN, isLoggedIn).commit();
    }

}
