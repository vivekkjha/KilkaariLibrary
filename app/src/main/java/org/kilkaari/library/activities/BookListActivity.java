package org.kilkaari.library.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.BooksListAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vk on 20-03-2015.
 */
public class BookListActivity extends BaseActivity {

    public static List<BooksModel> list_books;

    private BooksListAdapter adapter;

    private ListView listView_listBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        listView_listBooks = (ListView)findViewById(R.id.listView_listBooks);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        list_books = new ArrayList<BooksModel>();

        String category = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS_SELECTED_CATEGORY);
        if(category!=null)
        {
            getBooksDetails(category);
        }

        adapter = new BooksListAdapter(this,list_books);
        listView_listBooks.setAdapter(adapter);

    }

    //> get details of all the Books in local list
    public void getBooksDetails(String category)
    {
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
                            list_books.add(model);
                        }
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
