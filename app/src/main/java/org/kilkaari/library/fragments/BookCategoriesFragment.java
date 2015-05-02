package org.kilkaari.library.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.activities.MainActivity;
import org.kilkaari.library.adapters.BooksCategoriesAdapter;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BookCategoriesModel;
import org.kilkaari.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vivek on 28/04/15.
 */
public class BookCategoriesFragment extends Fragment {

    //> lists to get entries from Books table
    public List<BookCategoriesModel> list_CategoriesBooks;

    //> layout related objects
    private View rootView;
    private GridView gridViewCategories;



    //> program objects
    private BooksCategoriesAdapter adapter;
    private BaseActivity activity;

    //> list to get all the categories
    private List<String> list_booksCategories;

    //> hash=map to get links of all the categories
    private HashMap<String,String> hash_categoryPhoto;

    private boolean isEdit = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //> list initialization
        list_CategoriesBooks = new ArrayList<BookCategoriesModel>();
        list_booksCategories =new ArrayList<String>();

        hash_categoryPhoto =new HashMap<String,String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //> get bundle from calling activity
        isEdit = getArguments().getBoolean(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT);
        rootView = inflater.inflate(R.layout.activtity_books_categories,container,false);

        //> layout initialization
        gridViewCategories = (GridView)rootView.findViewById(R.id.gridViewCategories);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (BaseActivity)getActivity();

        //> actions on top title bar and done layout from baseActivity
        activity.setHeading("Book Shelf");
        activity.showHideDone(false);

        //> getCategories from server
        activity.showProgressLayout();
        getCategories();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void setAdapter()
    {
        if(list_CategoriesBooks.size()!=0) {

            //> set gridView adapter
            adapter = new BooksCategoriesAdapter(activity,list_CategoriesBooks,isEdit);
            gridViewCategories.setAdapter(adapter);
        }
    }


    //> get categories of books from parse server
    public void getCategories()
    {
        //> clear categories list before loading data into it
        list_CategoriesBooks.clear();

        //> parse query to get categories from parse server
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_CATEGORIES);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Categories", "Retrieved " + categoryList.size() + " rows");

                    if(categoryList.size()!=0)
                    {
                        list_booksCategories.clear();
                        hash_categoryPhoto.clear();

                        for (int i=0;i<categoryList.size();i++)
                        {
                            //> add category name into the list
                            list_booksCategories.add(categoryList.get(i).getString(Constants.DataColumns.CATEGORIES_CATEGORY));
                            LogUtil.w("Books Categories", "Category : " + categoryList.get(i).getString(Constants.DataColumns.CATEGORIES_CATEGORY));

                            //> add category name as key, and photo url as value
                            if(categoryList.get(i).getParseFile(Constants.DataColumns.CATEGORIES_PHOTO)!=null) {
                                hash_categoryPhoto.put(categoryList.get(i).getString(Constants.DataColumns.CATEGORIES_CATEGORY),
                                        categoryList.get(i).getParseFile(Constants.DataColumns.CATEGORIES_PHOTO).getUrl());
                                LogUtil.w("Books Categories","Category URl : "+ categoryList.get(i).getParseFile(Constants.DataColumns.CATEGORIES_PHOTO).getUrl());
                            }

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

    //> get Categories count from parse server
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

                    if(hash_categoryPhoto.get(category)!=null) {
                        model.setPhotoUrl(hash_categoryPhoto.get(category));
                    }
                    model.setCount(i);
                    list_CategoriesBooks.add(model);

                    //> setAdapter
                    setAdapter();

                    //> if the category is last element of list_booksCategories List
                    if(category.equals(list_booksCategories.get(list_booksCategories.size()-1)))
                    {

                        activity.hideProgressLayout();
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }

        });



    }
}
