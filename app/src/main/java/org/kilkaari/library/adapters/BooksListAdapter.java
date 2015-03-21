package org.kilkaari.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.activities.BookListActivity;
import org.kilkaari.library.activities.BooksCategoriesActivity;
import org.kilkaari.library.models.BooksModel;

import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class BooksListAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<BooksModel> listBooks;
    private BookListActivity context;

    public BooksListAdapter(BaseActivity context, List<BooksModel> list){

        this.listBooks = list;
        this.context = (BookListActivity)context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    // > selected children list is been fetched from application file to have central cntrol over it (both from grid and list views)

    @Override
    public int getCount() {
        return listBooks.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;
        final BooksModel model = listBooks.get(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_book_list_item,null);

            viewHolder = new ViewHolder();
            viewHolder.img_bookIcon = (ImageView)convertView.findViewById(R.id.img_bookIcon);
            viewHolder.txt_bookName = (TextView)convertView.findViewById(R.id.txt_bookName);
            viewHolder.txt_authorName  = (TextView)convertView.findViewById(R.id.txt_authorName);
            viewHolder.txt_availability  = (TextView)convertView.findViewById(R.id.txt_availability);
            viewHolder.ratingBar  = (RatingBar)convertView.findViewById(R.id.ratingBar);
            viewHolder.img_toread  = (ImageView)convertView.findViewById(R.id.img_toread);
            viewHolder.img_alreadyRead  = (ImageView)convertView.findViewById(R.id.img_alreadyRead);
            viewHolder.img_requested  = (ImageView)convertView.findViewById(R.id.img_requested);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_bookName.setText(model.getName());
        viewHolder.txt_authorName.setText(model.getAuthor());

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
        ImageView img_bookIcon;
        TextView txt_bookName;
        TextView txt_authorName;
        TextView txt_availability;
        RatingBar ratingBar;
        ImageView img_toread;
        ImageView img_alreadyRead;
        ImageView img_requested;


    }
}

