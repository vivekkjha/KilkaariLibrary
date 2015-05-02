package org.kilkaari.library.application;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;

import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vk on 14-03-2015.
 */
public class LibraryApplication extends Application {

    //> List to get book list for specific category
    private  List<BooksModel> list_books;


    //> List to store requested books for current user (Only book object id)
    private List<String> list_requestBooksCurrentUser;


    private Prefs prefs;
    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader loader;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new Prefs(this);
        list_books = new ArrayList<BooksModel>();
        list_requestBooksCurrentUser = new ArrayList<String>();

        //> create kilkaari folder
        createAppFolders();

        // > Initializing image loader at application level
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        loader.init(config);

        // Enable Local Datastore.

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "wF57BFoc8Sge0vz1tWb6vrHEOSr6BffUWULKXjpF", "KojP7xdhyKwULJNT0TglZbKyfwDoBNDouqkVze1v");




    }

    // >  Create new Folder for application
    private void createAppFolders()
    {

        String extr = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(extr +File.separator + Constants.Folders.FOLDER_KILKAARI );

        if (!mFolder.exists()) {
            boolean m = mFolder.mkdir();
            LogUtil.w("Kilkaari Folder ?", (m) ? "True" : "false");
        }
    }
    public Prefs getPref() {
        return prefs;
    }
    public DisplayImageOptions getImageLoaderOptions(){
        return options;
    }
    public com.nostra13.universalimageloader.core.ImageLoader getImageLoader(){
        return loader;
    }
    public List<BooksModel> getList_books()
    {
        return  list_books;
    }
    public List<String> getList_requestBooksCurrentUser() {
        return list_requestBooksCurrentUser;
    }


}
