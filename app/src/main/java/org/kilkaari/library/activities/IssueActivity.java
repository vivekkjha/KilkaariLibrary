package org.kilkaari.library.activities;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.LogUtil;

import java.util.List;

/**
 * Created by vivek on 30/03/15.
 */
public class IssueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateAvailability(String objectId)
    {
        ParseObject object = new ParseObject(Constants.Table.TABLE_BOOKS);
        object.setObjectId(objectId);

        //> check if the category already exist or not
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_AVAILABILITY);
        query.whereEqualTo(Constants.DataColumns.AVAILABLE_BOOK_POINT,object);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Available", "Retrieved " + list.size() + " rows");
                    if(list.size()!=0)
                    {
                        LogUtil.e("Availability", "isAvailable ? " + list.get(0).get(Constants.DataColumns.AVAILABLE_AVAILABLE));
                    }

                } else {

                    Log.d("Categories", "Error: " + e.getMessage());
                }
            }
        });

    }
}
