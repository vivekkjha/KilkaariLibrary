package org.kilkaari.library.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.IssueListAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.RequestQueueModel;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek on 30/03/15.
 */
public class IssueActivity extends BaseActivity {

    private ListView listView_issue;

    //> list to get all the request from parse server
    private List<RequestQueueModel> requestQueueList;
    private SaveDataUtils saveDataUtils;
    private IssueListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_issue);

        requestQueueList = new ArrayList<RequestQueueModel>();

        listView_issue = (ListView)findViewById(R.id.listView_issue);
        saveDataUtils = new SaveDataUtils(this);

        //> get Request Queue Data
        showProgressLayout();
        getRequestQueue();

    }
    //> get Request queue
    public void getRequestQueue()
    {
        //> list to get all the rows in request queue
        final List<RequestQueueModel> requestQueueModelList = new ArrayList<RequestQueueModel>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_REQUEST_QUEUE);
        query.addDescendingOrder("createdAt"); //> show in descending order of createdAt
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> requestList, ParseException e) {

                hideProgressLayout();
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

                        if(requestQueueModelList.size()!=0)
                        {
                            adapter = new IssueListAdapter(IssueActivity.this,requestQueueModelList);
                            listView_issue.setAdapter(adapter);

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


    }


}
