package org.kilkaari.library.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.IssueListAdapter;
import org.kilkaari.library.adapters.ReturnListAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.IssueListModel;
import org.kilkaari.library.models.RequestQueueModel;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vivek on 30/03/15.
 */
public class ReturnActivity extends BaseActivity {

    private ListView listView_issue;

    //> list to get all the request from parse server
    private List<IssueListModel> issueList;

    private SaveDataUtils saveDataUtils;
    private ReturnListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_issue);

        issueList = new ArrayList<IssueListModel>();

       // listView_issue = (ListView)findViewById(R.id.listView_issue);
        saveDataUtils = new SaveDataUtils(this);

        //> get Request Queue Data
        showProgressLayout();
        getIssueList();

    }
    //> get Request queue
    public void getIssueList()    {


        //> list to get all the rows in request queue
        final List<IssueListModel> IssueModelList = new ArrayList<IssueListModel>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_ISSUE);

        //> check if the books has already been returned or not
        query.whereEqualTo(Constants.DataColumns.ISSUE_IS_RETURNED,false);
        query.addDescendingOrder("createdAt"); //> show in descending order of createdAt
        query.include(Constants.DataColumns.REQUEST_QUEUE_BOOK);
        query.include(Constants.DataColumns.REQUEST_QUEUE_USER);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> issueList, ParseException e) {


                if (e == null) {
                    Log.d("Issue List ", "Retrieved " + issueList.size() + " rows");
                    if(issueList.size()!=0)
                    {
                        for(int i=0;i<issueList.size();i++)
                        {
                            try {
                                ParseObject object = issueList.get(i);


                                IssueListModel model = new IssueListModel();
                                model.setBookObject(object.fetch().getParseObject(Constants.DataColumns.ISSUE_BOOK));
                                model.setUserObject(object.fetch().getParseObject(Constants.DataColumns.ISSUE_USER));
                                model.setIssueTimestamp(object.getString(Constants.DataColumns.ISSUE_ISSUE_TIMESTAMP));
                                model.setTimePeriod(object.getString(Constants.DataColumns.ISSUE_TIME_PERIOD));
                                model.setReturnTimestamp(object.getString(Constants.DataColumns.ISSUE_RETURN_TIMESTAMP));
                                model.setReturned(object.getBoolean(Constants.DataColumns.ISSUE_IS_RETURNED));

                                //> set model into list
                                IssueModelList.add(model);


                            }
                            catch (ParseException pe)
                            {
                                pe.printStackTrace();
                            }
                        }

                        if(IssueModelList.size()!=0)
                        {
                            adapter = new ReturnListAdapter(ReturnActivity.this,IssueModelList);
                            listView_issue.setAdapter(adapter);

                        }
                    }
                    else {

                        LogUtil.e("Issue List","Database returned 0 list ");
                    }
                } else {
                    Log.d("Issue List", "Error: " + e.getMessage());
                }
                hideProgressLayout();

            }
        });


    }

    //> update issue list from adapter
    public void updateIssueList(final IssueListModel model, final String returnDate,final String fees)   {


        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_ISSUE);
        query.whereEqualTo(Constants.DataColumns.ISSUE_USER,model.getUserObject());
        query.whereEqualTo(Constants.DataColumns.ISSUE_BOOK,model.getBookObject());

        query.addDescendingOrder("createdAt"); //> show in descending order of createdAt
        query.include(Constants.DataColumns.REQUEST_QUEUE_BOOK);
        query.include(Constants.DataColumns.REQUEST_QUEUE_USER);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if(e == null)
                {
                    Log.d("Return Queue List ", "Retrieved " + parseObjects.size() + " rows");
                    if(parseObjects.size()!=0)
                    {
                        ParseObject issueListModel = parseObjects.get(0);
                        issueListModel.put(Constants.DataColumns.ISSUE_RETURN_TIMESTAMP,returnDate);
                        issueListModel.put(Constants.DataColumns.ISSUE_FEES,fees);
                        issueListModel.put(Constants.DataColumns.ISSUE_IS_RETURNED,true);
                        issueListModel.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) {
                                    LogUtil.w("ReturnListActivity", "Issue List updated with return values");

                                    //> update availability of book in background when returned
                                    saveDataUtils.updateAvailability(model.getBookObject().getObjectId(),true);
                                }
                            }
                        });
                    }
                }
                else
                {
                    LogUtil.e("ReturnActivity", "Exception " + e);
                }



            }
        });
    }





}
