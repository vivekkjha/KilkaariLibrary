package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.BooksListAdapter;
import org.kilkaari.library.adapters.IssueListAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.RequestQueueModel;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vivek on 30/03/15.
 */
public class IssueActivity extends BaseActivity {

    private ListView listView_issue;
    private PopupMenu popupDetails;

    //> list to get all the request from parse server
    private List<RequestQueueModel> requestQueueList;

    //> hash to store availability of books with Object id as key and boolean as value
    private HashMap<String,Boolean> hash_booksAvailability;

    private SaveDataUtils saveDataUtils;
    private IssueListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_issue);

        requestQueueList = new ArrayList<RequestQueueModel>();
        hash_booksAvailability = new HashMap<String,Boolean>();

        //listView_issue = (ListView)findViewById(R.id.listView_issue);
        saveDataUtils = new SaveDataUtils(this);

        //> get Request Queue Data
        showProgressLayout();
        getRequestQueue();

    }
    //> get Request queue
    public void getRequestQueue()
    {
        //> clear the availability hash
        hash_booksAvailability.clear();



        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_REQUEST_QUEUE);
        query.addDescendingOrder("createdAt"); //> show in descending order of createdAt
        query.include(Constants.DataColumns.REQUEST_QUEUE_BOOK);
        query.include(Constants.DataColumns.REQUEST_QUEUE_USER);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> requestList, ParseException e) {
                if (e == null) {
                    Log.d("Request Queue List ", "Retrieved " + requestList.size() + " rows");
                    if(requestList.size()!=0)
                    {
                        for(int i=0;i<requestList.size();i++)
                        {

                                ParseObject object = requestList.get(i);
                                RequestQueueModel model = new RequestQueueModel();
                                model.setBookObject(object.getParseObject(Constants.DataColumns.REQUEST_QUEUE_BOOK));
                                model.setUserObject(object.getParseObject(Constants.DataColumns.REQUEST_QUEUE_USER));
                                model.setTimestamp(object.getString(Constants.DataColumns.REQUEST_QUEUE_TIMESTAMP));
                                model.setTimePeriod(object.getString(Constants.DataColumns.REQUEST_QUEUE_TIME_PERIOD));

                                //> set model into list
                                requestQueueList.add(model);

                                //> get availability of books with book object as filter and a flag to indicate last element
                                getAvailability(model.getBookObject(),i==(requestQueueList.size()-1));

                        }
                    }
                    else {

                        LogUtil.e("Request Queue List","Database returned 0 list ");
                    }
                } else {
                    Log.d("Request Queue List", "Error: " + e.getMessage());
                }
                hideProgressLayout();

            }

        });


    }
    //> get availability of books
    public void getAvailability(ParseObject bookObject, final boolean isLast)
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_AVAILABILITY);
        query.whereEqualTo(Constants.DataColumns.AVAILABLE_BOOK_POINT,bookObject);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseList, ParseException e) {

                if (e == null) {
                    Log.d("Availability List ", "Retrieved " + parseList.size() + " rows");
                    if (parseList.size() != 0) {
                        ParseObject parseObject = parseList.get(0);
                        LogUtil.w("Availability IssueListActivity", "Object " + parseObject);

                        //> add data from sever into the hash map
                        boolean isAvailable = (parseObject.getInt(Constants.DataColumns.AVAILABLE_QUANTITY) != 0);

                        ParseObject po = parseObject.getParseObject(Constants.DataColumns.AVAILABLE_BOOK_POINT);

                        hash_booksAvailability.put(po.getObjectId(), isAvailable);

                        //> if the last element of list has been fetched , then show adapter

                    }
                    if (isLast) {
                        if (requestQueueList.size() != 0) {
                            adapter = new IssueListAdapter(IssueActivity.this, requestQueueList);
                            listView_issue.setAdapter(adapter);

                        }
                    }


                }
                    else {
                    LogUtil.e("IssueActivity", "Database returned 0 list ");
                }
            }
        });
    }

    public HashMap<String, Boolean> getHash_booksAvailability() {
        return hash_booksAvailability;
    }


    public void insertInIssueList(final RequestQueueModel model,String date) {
        //> show progress layout when uploading of data starts
        showProgressLayout();


        ParseObject newIssue = new ParseObject(Constants.Table.TABLE_ISSUE);
        newIssue.put(Constants.DataColumns.ISSUE_BOOK, model.getBookObject());
        newIssue.put(Constants.DataColumns.ISSUE_USER, model.getUserObject());
        newIssue.put(Constants.DataColumns.ISSUE_ISSUE_TIMESTAMP, date);
        newIssue.put(Constants.DataColumns.ISSUE_IS_RETURNED, false);
        newIssue.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                hideProgressLayout();
                if (e == null) {
                    //> update availability of book in background
                    saveDataUtils.updateAvailability(model.getBookObject().getObjectId(), false);

                    //> delete row from request queue
                    saveDataUtils.deleteRequestQueueLibrarian(model.getBookObject(), model.getUserObject());

                    //> refresh request queue
                    getRequestQueue();
                }
            }
        });

        LogUtil.i("Issue Row ", "Created");
    }

    //> Show popup on click of option icon , and click listener on its item
    public void showPopupWindow(View view, final int pos)
    {
        popupDetails = new PopupMenu(this,view);

        popupDetails.getMenuInflater().inflate(R.menu.menu_issue, popupDetails.getMenu());

        popupDetails.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().toString().equals(getString(R.string.userDetails)))
                {
                    Toast.makeText(IssueActivity.this,"Get user Details in popup",Toast.LENGTH_SHORT).show();
                }
                else if(item.getTitle().toString().equals(getString(R.string.bookDetails)))
                {
                    Toast.makeText(IssueActivity.this,"Get Book Details in activity",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popupDetails.show();

    }

}
