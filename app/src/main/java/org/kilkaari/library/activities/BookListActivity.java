package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.BooksListAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.Availability;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.models.RequestQueueModel;
import org.kilkaari.library.utils.LogUtil;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vk on 20-03-2015.
 */
public class BookListActivity extends BaseActivity {



    //> hash to store availability of books with Object id as key and boolean as value
    public HashMap<String,Boolean> hash_booksAvailability;
    public HashMap<String,Integer> hash_booksRating;

    private BooksListAdapter adapter;
    private SaveDataUtils saveDataUtils;
    private PopupMenu popupDetails;
    private ListView listView_listBooks;
    private AutoCompleteTextView autoTxt_searchBooks;

    private boolean isEdit = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        listView_listBooks = (ListView)findViewById(R.id.listView_listBooks);
        autoTxt_searchBooks = (AutoCompleteTextView)findViewById(R.id.autoTxt_searchBooks);

        hash_booksAvailability = new HashMap<String,Boolean>();
        hash_booksRating = new HashMap<String,Integer>();
        saveDataUtils  = new SaveDataUtils(this);

        isEdit = getIntent().getBooleanExtra(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT,false);
        String category = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS_SELECTED_CATEGORY);
        if(category!=null)
        {
            showProgressLayout();
            getBooksDetails(category);
        }
    }

    //> get details of all the Books in local list
    public void getBooksDetails(String category)
    {
        //> clear application list to store books details, when fetched for new category
        getList_books().clear();

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
                            getList_books().add(model);

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

                       /* adapter = new BooksListAdapter(BookListActivity.this,hash_booksAvailability,isEdit);
                        listView_listBooks.setAdapter(adapter);*/
                        hideProgressLayout();
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

    public void getRatings()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_RATING);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseList, ParseException e) {
                if (e == null) {
                    Log.d("Ratings List ", "Retrieved " + parseList.size() + " rows");
                    if(parseList.size()!=0)
                    {
                        for (int i=0;i<parseList.size();i++) {

                            ParseObject parseObject = parseList.get(i);
                            LogUtil.w("Ratings BookListActivity", "Object " + i );

                            //> add data from sever into the list
                            int rating  = Integer.parseInt(parseObject.getString(Constants.DataColumns.RATING_NET_RATING));

                            ParseObject po = parseObject.getParseObject(Constants.DataColumns.RATING_BOOK);

                            hash_booksRating.put(po.getObjectId(),rating);

                            //> notify adapter whenever new row gets added
                            if(adapter!=null)
                            {
                                adapter.notifyDataSetChanged();
                            }
                        }

                        //> hide progress layout when all the fetching operations gets completed

                       /* adapter = new BooksListAdapter(BookListActivity.this,hash_booksAvailability,isEdit);
                        listView_listBooks.setAdapter(adapter);*/
                        hideProgressLayout();
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


    public void onClick(View v)
    {

    }

    //> Show popup on click of option icon , and click listener on its item
    public void showPopupWindow(View view, final int pos)
    {
        popupDetails = new PopupMenu(this,view);

        popupDetails.getMenuInflater().inflate(R.menu.menu_book_details,  popupDetails.getMenu());

        popupDetails.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent  = new Intent(BookListActivity.this, BookDetailsActivity.class);
                intent.putExtra(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX,pos);
                startActivity(intent);

                return true;
            }
        });
        popupDetails.show();

    }

    //> Show popup on click of option icon , and click listener on its item
    public void showPopupWindowEdit(View view, final int pos)
    {
        popupDetails = new PopupMenu(this,view);

        popupDetails.getMenuInflater().inflate(R.menu.menu_book_edit,  popupDetails.getMenu());

        popupDetails.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().toString().equals(getString(R.string.bookDetails))) {
                    Intent intent = new Intent(BookListActivity.this, BookDetailsActivity.class);
                    intent.putExtra(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX, pos);
                    startActivity(intent);
                }
                else if(item.getTitle().toString().equals(getString(R.string.editDetails)))
                {

                }

                return true;
            }
        });
        popupDetails.show();

    }

}
