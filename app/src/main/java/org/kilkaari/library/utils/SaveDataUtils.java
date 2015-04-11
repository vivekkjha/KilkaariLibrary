package org.kilkaari.library.utils;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;

import java.util.List;

/**
 * Created by vk on 11-04-2015.
 */
public class SaveDataUtils {

    Context context;
    public SaveDataUtils(Context context)
    {
        this.context = context;
    }

    //> save request when prompted by user click on request button / update Table in parse
    public void saveRequest(String bookObjectId,boolean isRequest,String timestamp,String timePeriod )
    {
        //> check if current user is null
         if(ParseUser.getCurrentUser() != null) {

             try {
                 //> make parse object for book with its object id
                 ParseObject bookObject = new ParseObject(Constants.Table.TABLE_BOOKS);
                 bookObject.setObjectId(bookObjectId);

                 if (isRequest)//> create new row in RequestQueue table
                 {
                     ParseObject newRequest = new ParseObject(Constants.Table.TABLE_REQUEST_QUEUE);
                     newRequest.put(Constants.DataColumns.REQUEST_QUEUE_USER, ParseUser.getCurrentUser());
                     newRequest.put(Constants.DataColumns.REQUEST_QUEUE_USER, ParseUser.getCurrentUser());
                     newRequest.put(Constants.DataColumns.REQUEST_QUEUE_TIMESTAMP, timestamp);
                     newRequest.put(Constants.DataColumns.REQUEST_QUEUE_TIME_PERIOD, timePeriod);
                     newRequest.save();
                     LogUtil.d("Request Queue Row ", "Created");
                 }
                 //>  check if the request existed previously , delete request from table
                 else {

                     ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_REQUEST_QUEUE);
                     query.whereEqualTo(Constants.DataColumns.REQUEST_QUEUE_USER,ParseUser.getCurrentUser());
                     query.whereEqualTo(Constants.DataColumns.REQUEST_QUEUE_BOOK,bookObject);
                     query.findInBackground(new FindCallback<ParseObject>() {
                         public void done(List<ParseObject> requestList, ParseException e) {

                             if (e == null) {
                                 Log.d("Request Queue List ", "Retrieved " + requestList.size() + " rows");
                                 if(requestList.size()!=0)
                                 {
                                     //> get first object as logiclly there should be only one with above request
                                     //> delete that object in background
                                     ParseObject object = requestList.get(0);
                                     object.deleteInBackground();
                                 }
                                 else {
                                     LogUtil.e("Request Queue List","Database returned 0 list ");
                                 }
                             } else {
                                 Log.d("Request Queue List", "Error: " + e.getMessage());
                             }
                         }
                     });
                 }


             } catch (ParseException ex) {
                 ex.printStackTrace();

             }
         }
    }
}
