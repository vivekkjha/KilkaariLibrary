package org.kilkaari.library.utils;

import android.content.Context;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.models.RequestQueueModel;

import java.util.ArrayList;
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
    public  void saveRequest(String bookObjectId,boolean isRequest,String timestamp,String timePeriod )
    {
        //> check if current user is null
        if(ParseUser.getCurrentUser() != null) {

            //> make parse object for book with its object id
            ParseObject bookObject = ParseObject.createWithoutData(Constants.Table.TABLE_BOOKS,bookObjectId);

            if (isRequest)//> create new row in RequestQueue table
            {
                ParseObject newRequest = new ParseObject(Constants.Table.TABLE_REQUEST_QUEUE);
                newRequest.put(Constants.DataColumns.REQUEST_QUEUE_USER, ParseUser.getCurrentUser());
                newRequest.put(Constants.DataColumns.REQUEST_QUEUE_BOOK, bookObject);
                newRequest.put(Constants.DataColumns.REQUEST_QUEUE_TIMESTAMP, timestamp);
                newRequest.put(Constants.DataColumns.REQUEST_QUEUE_TIME_PERIOD, timePeriod);
                newRequest.saveInBackground();
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

        }
    }
    //> get Request queue
    public List<RequestQueueModel> getRequestQueue()
    {
        //> list to get all the rows in request queue
        final List<RequestQueueModel> requestQueueModelList = new ArrayList<RequestQueueModel>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_REQUEST_QUEUE);
        query.addDescendingOrder("createdAt"); //> show in descending order of createdAt
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> requestList, ParseException e) {

                if (e == null) {
                    Log.d("Request Queue List ", "Retrieved " + requestList.size() + " rows");
                    if(requestList.size()!=0)
                    {
                      for(int i=0;i<requestList.size();i++)
                      {
                          ParseObject object = requestList.get(i);
                          RequestQueueModel model =new RequestQueueModel();
                          model.setBookObject(object.getParseObject(Constants.DataColumns.REQUEST_QUEUE_BOOK));
                          model.setUserObject(object.getParseObject(Constants.DataColumns.REQUEST_QUEUE_USER));
                          model.setTimestamp(object.getString(Constants.DataColumns.REQUEST_QUEUE_TIMESTAMP));
                          model.setTimePeriod(object.getString(Constants.DataColumns.REQUEST_QUEUE_TIME_PERIOD));

                          //> set model into list
                          requestQueueModelList.add(model);
                      }

                    }
                    else {
                        LogUtil.e("Request Queue List","Database returned 0 list ");
                    }
                } else {
                    Log.d("Request Queue List", "Error: " + e.getMessage());
                }
            }
        });

        return requestQueueModelList;
    }

    //> delete from request queue
    public void deleteRequestQueueLibrarian(ParseObject bookObject,ParseObject userObject)
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_REQUEST_QUEUE);
        query.whereEqualTo(Constants.DataColumns.REQUEST_QUEUE_BOOK,bookObject);
        query.whereEqualTo(Constants.DataColumns.REQUEST_QUEUE_USER,userObject);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> requestList, ParseException e) {

                if (e == null) {
                    Log.d("Request Queue List ", "Retrieved " + requestList.size() + " row(s)");
                    if(requestList.size()!=0)
                    {
                        //> get first object as logiclly there should be only one with above request
                        //> delete that object in background

                            ParseObject object = requestList.get(0);
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if(e == null)
                                    {
                                        LogUtil.e("SaveDataUtils","object deleted successfully ");

                                    }

                                }
                            });


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

    //> method to update Availability table in Parse  with status value as "returned" or "issued"
    public void updateAvailability(String bookObjectId, final boolean isReturned)
    {
        //> make parse object for book with its object id
        ParseObject bookObject = ParseObject.createWithoutData(Constants.Table.TABLE_BOOKS,bookObjectId);

        //> check if the category already exist or not
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_AVAILABILITY);
        query.whereEqualTo(Constants.DataColumns.AVAILABLE_BOOK_POINT,bookObject);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Available", "Retrieved " + list.size() + " row(s)");
                    if(list.size()!=0)
                    {
                    // > update row
                        ParseObject parseObject = list.get(0);

                        //> get quantity of the book from server
                        int quantity = parseObject.getInt(Constants.DataColumns.AVAILABLE_QUANTITY);

                            if(isReturned)
                            {   quantity++;

                            }
                            else
                            {
                                quantity--;
                            }

                            parseObject.put(Constants.DataColumns.AVAILABLE_QUANTITY, quantity);
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    LogUtil.d("SaveDataUtils", "Availability Table updated");
                                }
                            });


                    }

                } else {

                    Log.d("Categories", "Error: " + e.getMessage());
                }
            }
        });

    }
}
