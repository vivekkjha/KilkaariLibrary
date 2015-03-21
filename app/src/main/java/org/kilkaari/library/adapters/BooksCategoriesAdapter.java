package org.kilkaari.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.activities.BookListActivity;
import org.kilkaari.library.activities.BooksCategoriesActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BookCategoriesModel;
import org.kilkaari.library.models.BooksModel;

import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class BooksCategoriesAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<BookCategoriesModel> listBooks;
    private BooksCategoriesActivity context;

    public BooksCategoriesAdapter(BaseActivity context, List<BookCategoriesModel> list){

        this.listBooks = list;
        this.context = (BooksCategoriesActivity)context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public int getCount() {
        return listBooks.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;
        final BookCategoriesModel model = listBooks.get(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_books_categories,null);

            viewHolder = new ViewHolder();
            viewHolder.img_categoryImage = (ImageView)convertView.findViewById(R.id.img_categoryImage);
            viewHolder.txt_categoryName = (TextView)convertView.findViewById(R.id.txt_categoryName);
            viewHolder.txt_categoryCount  = (TextView)convertView.findViewById(R.id.txt_categoryCount);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_categoryName.setText(model.getCategory());
        viewHolder.img_categoryImage.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_book_default));
        viewHolder.txt_categoryCount.setText(Integer.toString(model.getCount()));


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, BookListActivity.class);
                intent.putExtra(Constants.EXTRAS.EXTRAS_SELECTED_CATEGORY,model.getCategory());
                context.startActivity(intent);

            }
        });
        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return listBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  0;
    }

    public static class ViewHolder{
        ImageView img_categoryImage;
        TextView txt_categoryName;
        TextView txt_categoryCount;


    }
}

