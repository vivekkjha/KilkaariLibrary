package org.kilkaari.library.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.LogUtil;

import java.io.ByteArrayOutputStream;

/**
 * Created by vivek on 20/03/15.
 */
public class AddBooksActivity extends BaseActivity {

    private EditText edt_donorName,edt_date,edt_bookName,edt_authorName,edt_language,edt_pageCount,edt_publisher,edt_publishingYear,edt_description;
    private AutoCompleteTextView autoTxt_category;
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


    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.lin_topDone)
        {
            saveQuestion();
        }
    }

    // > save asked question
    public void saveQuestion()
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

}
