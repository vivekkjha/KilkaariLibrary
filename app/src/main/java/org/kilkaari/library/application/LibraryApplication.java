package org.kilkaari.library.application;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by vk on 14-03-2015.
 */
public class LibraryApplication extends Application {

    private Prefs prefs;
    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new Prefs(this);

        // Enable Local Datastore.

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "wF57BFoc8Sge0vz1tWb6vrHEOSr6BffUWULKXjpF", "KojP7xdhyKwULJNT0TglZbKyfwDoBNDouqkVze1v");


    }
    public Prefs getPref() {
        return prefs;
    }
}
