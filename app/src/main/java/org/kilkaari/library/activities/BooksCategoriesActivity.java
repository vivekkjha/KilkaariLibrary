package org.kilkaari.library.activities;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.BooksCategoriesAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BookCategoriesModel;
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
    private LinearLayout lin_topDone;

    //> program objects
    private BooksCategoriesAdapter adapter;

    //> lists to get entries from Books table
    public static List<BookCategoriesModel> list_CategoriesBooks;

    //> list to get all the categories
    private List<String> list_booksCategories;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_books_categories);

        //> layout initialization
        gridViewCategories = (GridView)findViewById(R.id.gridViewCategories);
        lin_topDone = (LinearLayout)findViewById(R.id.lin_topDone);
        lin_topDone.setVisibility(View.GONE);

        //> list initialization
        list_CategoriesBooks = new ArrayList<BookCategoriesModel>();
        list_booksCategories =new ArrayList<String>();

        //> getCategories from server
        showProgressLayout();
        getCategories();




    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.lin_topDone)
        {
            startActivity(new Intent(this,BookDetailsActivity.class));

        }
    }
    public void setAdapter()
    {
        if(list_CategoriesBooks.size()!=0) {

            //> set gridView adapter
            adapter = new BooksCategoriesAdapter(this,list_CategoriesBooks);
            gridViewCategories.setAdapter(adapter);
        }
    }



    public void getCategories()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Categories", "Retrieved " + categoryList.size() + " rows");

                    if(categoryList.size()!=0)
                    {
                        list_booksCategories.clear();
                        for (int i=0;i<categoryList.size();i++)
                        {
                            list_booksCategories.add(categoryList.get(i).getString("category"));
                            LogUtil.w("Books Categories","Category : "+ categoryList.get(i).getString("category"));

                            //> if loop count is on last element , start fetching count of that category
                            if(i== (categoryList.size()-1))
                            {
                                for(int j=0;j<list_booksCategories.size();j++)
                                {
                                    //> get data for all respective categories
                                    getCategoriesCountFromParse(list_booksCategories.get(j));
                                }
                            }
                        }
                    }
                } else {
                    Log.d("Categories", "Error: " + e.getMessage());
                }
            }
        });
    }
    public void getCategoriesCountFromParse(final String category)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_BOOKS);
        query.whereEqualTo(Constants.DataColumns.BOOKS_CATEGORY, category);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null) {

                    BookCategoriesModel model = new BookCategoriesModel();
                    model.setCategory(category);
                    model.setCount(i);
                    list_CategoriesBooks.add(model);

                    //> setAdapter
                    setAdapter();

                    //> if the category is last element of of list_booksCategories List
                    if(category.equals(list_booksCategories.get(list_booksCategories.size()-1)))
                    {

                        hideProgressLayout();
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }

        });



    }
}
