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
import org.kilkaari.library.activities.BooksCategoriesActivity;

import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class SavedPagesAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<String> listCategories;
    private BooksCategoriesActivity context;

    public SavedPagesAdapter(BaseActivity context, List<String> list){

        this.listCategories = list;
        this.context = (BooksCategoriesActivity)context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    // > selected children list is been fetched from application file to have central cntrol over it (both from grid and list views)

    @Override
    public int getCount() {
        return listCategories.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;
        final String model = listCategories.get(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_book_list_item,null);

            viewHolder = new ViewHolder();
            viewHolder.txt_title = (TextView)convertView.findViewById(R.id.txt_title);
            viewHolder.txt_dateTime  = (TextView)convertView.findViewById(R.id.txt_dateTime);
            viewHolder.txt_pageLink  = (TextView)convertView.findViewById(R.id.txt_pageLink);
            viewHolder.txt_viewInBrowser  = (TextView)convertView.findViewById(R.id.txt_viewInBrowser);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return listCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  0;
    }

    public static class ViewHolder{

        TextView txt_title;
        TextView txt_dateTime;
        TextView txt_pageLink;
        TextView txt_viewInBrowser;
    }
}

