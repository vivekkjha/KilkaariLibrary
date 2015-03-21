package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.BooksCategoriesAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class BooksCategoriesActivity extends BaseActivity {


    //> layout related objects
    private GridView gridViewCategories;

    //> program objects
    private BooksCategoriesAdapter adapter;

    //> lists to get entries from Books table
    private List<BooksModel> list_books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_books_categories);

        //> layout initialization
        gridViewCategories = (GridView)findViewById(R.id.gridViewCategories);

        //> list initialization
        list_books = new ArrayList<BooksModel>();

        //> get all books details
        getBooksDetails();

        if(list_books.size()!=0) {

            //> set gridview adapter
            adapter = new BooksCategoriesAdapter(this,list_books);
            gridViewCategories.setAdapter(adapter);
        }

    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.lin_topDone)
        {
            startActivity(new Intent(this,BookDetailsActivity.class));

        }
    }

    //> get details of all the Books in local list
    public void getBooksDetails()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_BOOKS);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    Log.d("Book List ", "Retrieved " + userList.size() + " rows");
                    if(userList.size()!=0)
                    {
                        for (int i=0;i<userList.size();i++) {
                            ParseObject parseObject = userList.get(i);
                            LogUtil.w("Librarian Activity", "Object " + i + "Name: " + parseObject.get("name"));

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
                        LogUtil.e("LibrarianActivity","Database returned 0 list ");
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
