package org.kilkaari.library.activities;

import android.os.Bundle;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;

/**
 * Created by vivek on 20/03/15.
 */
public class BookDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        int position = -1;
        position = getIntent().getExtras().getInt(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX);

        if(position!=-1) {
            getBookDetails(position);
        }

    }
    public void getBookDetails(int pos)
    {
        BooksModel model = BookListActivity.list_books.get(pos);
        //> set different layouts elements with data in model
        LogUtil.w("BookDetailsActivity","Model object " + model);

    }

}
