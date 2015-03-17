package org.kilkaari.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;

import org.kilkaari.library.R;
import org.kilkaari.library.adapters.BooksCategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class BooksCategoriesActivity extends BaseActivity {


    private GridView gridViewCategories;
    private BooksCategoriesAdapter adapter;
    private List<String> listCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_books_categories);

        //> layout initialization
        gridViewCategories = (GridView)findViewById(R.id.gridViewCategories);

        //> list initialization
        listCategories = new ArrayList<String>();

        listCategories.add("Fiction");
        listCategories.add("Biography");
        listCategories.add("History");
        listCategories.add("Travel");
        listCategories.add("Fiction");
        listCategories.add("Biography");
        listCategories.add("History");
        listCategories.add("Travel");
        listCategories.add("Fiction");
        listCategories.add("Biography");
        listCategories.add("History");
        listCategories.add("Travel");

        if(listCategories.size()!=0) {

            adapter = new BooksCategoriesAdapter(this,listCategories);
            gridViewCategories.setAdapter(adapter);
        }

    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.lin_topDone)
        {
            startActivity(new Intent(this,AlertActivity.class));

        }
    }
}
