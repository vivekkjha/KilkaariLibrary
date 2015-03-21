package org.kilkaari.library.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek on 20/03/15.
 */
public class AddBooksActivity extends BaseActivity {

    private EditText edt_donorName,edt_date,edt_bookName,edt_authorName,edt_language,edt_pageCount,edt_publisher,edt_publishingYear,edt_description;
    private AutoCompleteTextView autoTxt_category;

    //> get categories from parse
    private List<String> list_booksCategories;

   // private String categories = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_books);

        edt_donorName = (EditText)findViewById(R.id.edt_donorName);
        edt_date = (EditText)findViewById(R.id.edt_date);
        edt_bookName = (EditText)findViewById(R.id.edt_bookName);
        edt_authorName = (EditText)findViewById(R.id.edt_authorName);
        edt_language = (EditText)findViewById(R.id.edt_language);
        edt_pageCount = (EditText)findViewById(R.id.edt_pageCount);
        edt_publisher = (EditText)findViewById(R.id.edt_publisher);
        edt_publishingYear = (EditText)findViewById(R.id.edt_publishingYear);
        edt_description = (EditText)findViewById(R.id.edt_description);
        autoTxt_category = (AutoCompleteTextView)findViewById(R.id.autoTxt_category);

        list_booksCategories = new ArrayList<String>();

        //> get books categories from preferences separated bt $%^

      /*  if(prefs.getBooksCategories()!=null) {
            String[] categoryArray = prefs.getBooksCategories().split(Constants.CATEGORIES_DELIMITER);
            for (int i = 0; i < categoryArray.length; i++) {
                list_booksCategories.add(categoryArray[i]);
            }
        }*/

        //> get Categories list from parse server
        getCategories();

        //> add adapter for auto-complete

        ArrayAdapter adapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, list_booksCategories);
        autoTxt_category.setAdapter(adapter);
        autoTxt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoTxt_category.setText(list_booksCategories.get(position));
            }
        });
    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.lin_topDone)
        {
           /* //> if textview has some new element
            if(!list_booksCategories.contains(autoTxt_category.getText().toString()))
            {
                //>  add new category in the list
                list_booksCategories.add(autoTxt_category.getText().toString());
                //> parse through the list and attach delimiter
                for(int i=0;i<list_booksCategories.size();i++)
                {
                    if(i!=list_booksCategories.size()) {
                        categories += list_booksCategories.get(i) + Constants.CATEGORIES_DELIMITER;
                    }
                    else {
                        categories += list_booksCategories.get(i);
                    }
                }
                //> set cocatenated string in preferences
                prefs.setBooksCategories(categories);

            }*/
            //> save entered category ind atabase
            saveCategories();

            //> save details of book entered
            saveBookDetails();
        }
    }

    // > save asked question
    public void saveBookDetails()
    {
        try {

            ParseObject newBook = new ParseObject(Constants.Table.TABLE_BOOKS);
            newBook.put(Constants.DataColumns.BOOKS_DONATED_BY, edt_donorName.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_ADDED_ON, edt_date.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_PAGE_COUNT, Integer.parseInt(edt_pageCount.getText().toString()));
            newBook.put(Constants.DataColumns.BOOKS_DESCRIPTION, edt_description.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_AUTHOR, edt_authorName.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_CATEGORY, autoTxt_category.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_LANGUAGE, edt_language.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_PUBLISHER, edt_publisher.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_PUBLICATION_YEAR, Integer.parseInt(edt_publishingYear.getText().toString()));
            newBook.put(Constants.DataColumns.BOOKS_NAME, edt_bookName.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_QUANTITY, 1);
            newBook.save();
            LogUtil.d("Books Row ", "Created");

        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
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
                        }
                    }
                } else {
                    Log.d("Categories", "Error: " + e.getMessage());
                }
            }
        });
    }
    public void saveCategories()
    {
        //> check if the category already exist or not
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_CATEGORIES);
        query.whereEqualTo(Constants.DataColumns.CATEGORIES_CATEGORY,autoTxt_category.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Categories", "Retrieved " + categoryList.size() + " rows");

                    if(categoryList.size()==0)
                    {
                        try {
                            //> if category does not exist. insert in table
                            ParseObject newCategory = new ParseObject(Constants.Table.TABLE_CATEGORIES);
                            newCategory.put(Constants.DataColumns.CATEGORIES_CATEGORY, autoTxt_category.getText().toString());
                            newCategory.save();
                        }
                        catch (ParseException pe)
                        {pe.printStackTrace();}


                    }
                } else {
                    Log.d("Categories", "Error: " + e.getMessage());
                }
            }
        });
    }

}
