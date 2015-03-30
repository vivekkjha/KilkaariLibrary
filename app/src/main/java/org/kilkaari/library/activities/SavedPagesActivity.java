package org.kilkaari.library.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.SavedPagesAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.models.SavedPagesModel;
import org.kilkaari.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vk on 20-03-2015.
 */
public class SavedPagesActivity extends BaseActivity {

    private List<SavedPagesModel> list_savedPages;
    private SavedPagesAdapter adapter;
    private ListView listView_savedPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_pages);

        listView_savedPages = (ListView)findViewById(R.id.listView_savedPages);
        list_savedPages = new ArrayList<SavedPagesModel>();

        //> get Saved pages data from server
        //showProgressLayout();
        getSavedPages();

    }

    //> get details of all the SavedPages in the local list
    public void getSavedPages()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_SAVED_PAGES);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> savedPagesList, ParseException e) {
               // hideProgressLayout();
                if (e == null) {
                    Log.d("Saved pages List ", "Retrieved " + savedPagesList.size() + " rows");

                    if(savedPagesList.size()!=0)
                    {
                        for(int i=0;i<savedPagesList.size();i++)
                        {
                            SavedPagesModel model = new SavedPagesModel();
                            model.setTitle(savedPagesList.get(i).getString(Constants.DataColumns.SAVED_PAGES_TITLE));
                            model.setLink(savedPagesList.get(i).getString(Constants.DataColumns.SAVED_PAGES_LINK));
                            model.setTimestamp(savedPagesList.get(i).getString(Constants.DataColumns.SAVED_PAGES_TIMESTAMP));

                            list_savedPages.add(model);
                        }
                        adapter = new SavedPagesAdapter(SavedPagesActivity.this,list_savedPages);
                        listView_savedPages.setAdapter(adapter);

                    }
                    else {
                        LogUtil.e("Saved pages List ","Database returned 0 list ");
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
