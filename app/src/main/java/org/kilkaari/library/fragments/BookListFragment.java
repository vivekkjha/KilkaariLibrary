package org.kilkaari.library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.adapters.BooksListAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vivek on 29/04/15.
 */
public class BookListFragment extends Fragment {

    //> hash to store availability of books with Object id as key and boolean as value
    private HashMap<String,Boolean> hash_booksAvailability;

    private List<String> requestedBooksList;

    private BooksListAdapter adapter;
    private SaveDataUtils saveDataUtils;
    private PopupMenu popupDetails;


    private ListView listView_listBooks;
    private AutoCompleteTextView autoTxt_searchBooks;
    private View rootView;
    private BaseActivity activity;

    private boolean isEdit = false;
    private String category;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //> get bundle from calling activity
        isEdit = getArguments().getBoolean(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT);
        category = getArguments().getString(Constants.EXTRAS.EXTRAS_SELECTED_CATEGORY);

        hash_booksAvailability = new HashMap<String,Boolean>();
        requestedBooksList = new ArrayList<String>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(org.kilkaari.library.R.layout.activity_book_list,container,false);

        listView_listBooks = (ListView)rootView.findViewById(org.kilkaari.library.R.id.listView_listBooks);
        autoTxt_searchBooks = (AutoCompleteTextView)rootView.findViewById(org.kilkaari.library.R.id.autoTxt_searchBooks);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (BaseActivity)getActivity();
        saveDataUtils  = new SaveDataUtils(activity);

        //> actions on top title bar and done layout from baseActivity
        activity.setHeading(category);
        activity.showHideDone(false);


        //> get requested book list for current user in SaveDataUtils
        saveDataUtils.getRequestedBooksCurrentUser();

    }

    @Override
    public void onStart() {
        super.onStart();

        //> get Book's Details if category is not null
        if(category!=null)
        {
            activity.showProgressLayout();
            getBooksDetails(category);
        }
    }

    //> get details of all the Books in local list
    public void getBooksDetails(String category)
    {
        //> clear application list to store books details, when fetched for new category
        activity.getList_books().clear();

        //> parse query to get list of books for this category
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_BOOKS);
        query.whereEqualTo(Constants.DataColumns.BOOKS_CATEGORY,category);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, ParseException e) {

                if (e == null) {
                    Log.d("Book List ", "Retrieved " + userList.size() + " rows");
                    if(userList.size()!=0)
                    {
                        for (int i=0;i<userList.size();i++) {
                            ParseObject parseObject = userList.get(i);
                            LogUtil.w("Books Category Activity", "Object " + i + "Name: " + parseObject.get("name"));

                            //> add data from sever into the list
                            BooksModel model = new BooksModel();
                            model.setObjectId(parseObject.getObjectId());
                            model.setAddedOn(parseObject.getString(Constants.DataColumns.BOOKS_ADDED_ON));
                            model.setAuthor(parseObject.getString(Constants.DataColumns.BOOKS_AUTHOR));
                            model.setCategory(parseObject.getString(Constants.DataColumns.BOOKS_CATEGORY));
                            model.setDescription(parseObject.getString(Constants.DataColumns.BOOKS_CATEGORY));
                            model.setDonatedBy(parseObject.getString(Constants.DataColumns.BOOKS_DONATED_BY));
                            model.setLanguage(parseObject.getString(Constants.DataColumns.BOOKS_LANGUAGE));
                            model.setName(parseObject.getString(Constants.DataColumns.BOOKS_NAME));
                            model.setPageCount(parseObject.getInt(Constants.DataColumns.BOOKS_PAGE_COUNT));
                            model.setPublisher(parseObject.getString(Constants.DataColumns.BOOKS_PUBLISHER));
                            model.setPublicationYear(parseObject.getInt(Constants.DataColumns.BOOKS_PUBLICATION_YEAR));
                            model.setQuantity(parseObject.getInt(Constants.DataColumns.BOOKS_QUANTITY));

                            //> add category name as key, and photo url as value
                            if(parseObject.getParseFile(Constants.DataColumns.BOOKS_PHOTO)!=null) {

                                model.setPhotoUrl( parseObject.getParseFile(Constants.DataColumns.BOOKS_PHOTO).getUrl());
                                LogUtil.w("Books Categories","Category URl : "+ parseObject.getParseFile(Constants.DataColumns.BOOKS_PHOTO).getUrl());
                            }
                            activity.getList_books().add(model);

                            //> notify adapter whenever new row gets added
                            if(adapter!=null)
                            {
                                adapter.notifyDataSetChanged();
                            }
                        }
                        //> get availability once all books list has been fetched
                        getAvailability();
                    }
                    else {
                        LogUtil.e("BooksCategories","Database returned 0 list ");
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void getAvailability()
     {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_AVAILABILITY);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseList, ParseException e) {
                if (e == null) {
                    Log.d("Availability List ", "Retrieved " + parseList.size() + " rows");
                    if(parseList.size()!=0)
                    {
                        for (int i=0;i<parseList.size();i++) {
                            ParseObject parseObject = parseList.get(i);
                            LogUtil.w("Availability BookListActivity", "Object " + i );

                            //> add data from sever into the list
                            boolean isAvailable  = (parseObject.getInt(Constants.DataColumns.AVAILABLE_QUANTITY)!=0);

                            ParseObject po = parseObject.getParseObject(Constants.DataColumns.AVAILABLE_BOOK_POINT);

                            hash_booksAvailability.put(po.getObjectId(),isAvailable);

                            //> notify adapter whenever new row gets added
                            if(adapter!=null)
                            {
                                adapter.notifyDataSetChanged();
                            }
                        }

                        //> hide progress layout when all the fetching operations gets completed

                        adapter = new BooksListAdapter(activity,hash_booksAvailability,isEdit);
                        listView_listBooks.setAdapter(adapter);
                        activity.hideProgressLayout();
                    }
                    else {
                        LogUtil.e("BooksCategories","Database returned 0 list ");
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }





}
