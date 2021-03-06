package org.kilkaari.library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.AlertActivity;
import org.kilkaari.library.activities.LibrarianActivity;
import org.kilkaari.library.adapters.ReturnListAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.IssueListModel;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek on 27/04/15.
 */
public class FragmentReturnBooks extends ListFragment{

    //> layout parameters
    private View rootView;
    private LinearLayout lin_add;
    private LibrarianActivity activity;
    private TextView txt_empty;

    //> list to get all the request from parse server
    private List<IssueListModel> issueList;

    private SaveDataUtils saveDataUtils;
    private ReturnListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        issueList = new ArrayList<IssueListModel>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.layout_issue,container,false);

        txt_empty = (TextView)rootView.findViewById(R.id.txt_empty);
        lin_add = (LinearLayout)rootView.findViewById(R.id.lin_add);
        lin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  //> get activity result in fragment instead of activity associated
                FragmentReturnBooks.this.startActivityForResult(new Intent(activity, AlertActivity.class),Constants.REQUEST_CODE.REQUEST_OPEN_ALERT);*/
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (LibrarianActivity)getActivity();



        //> actions on top title bar and done layout from baseActivity
        activity.setHeading("Issued Books");
        activity.showHideDone(false);

    }

    @Override
    public void onStart() {
        super.onStart();

        issueList = new ArrayList<IssueListModel>();

        // listView_issue = (ListView)findViewById(R.id.listView_issue);
        saveDataUtils = new SaveDataUtils(activity);

        //> get Request Queue Data
        activity.showProgressLayout();
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
                            adapter = new ReturnListAdapter(activity,IssueModelList);
                            setListAdapter(adapter);


                        }
                    }
                    else {

                        LogUtil.e("Issue List", "Database returned 0 list ");
                        txt_empty.setText("Sorry ! No Issued Books");



                    }
                } else {
                    Log.d("Issue List", "Error: " + e.getMessage());
                }
                activity.hideProgressLayout();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE.REQUEST_OPEN_ALERT)
        {
            LogUtil.e("Return Books Fragment","Control returned");
        }
    }
}
