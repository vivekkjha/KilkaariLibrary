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
import org.kilkaari.library.activities.SavedPagesActivity;
import org.kilkaari.library.models.SavedPagesModel;

import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class SavedPagesAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<SavedPagesModel> listSavedPagesModel;
    private SavedPagesActivity context;

    public SavedPagesAdapter(BaseActivity context, List<SavedPagesModel> list){

        this.listSavedPagesModel = list;
        this.context = (SavedPagesActivity)context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return listSavedPagesModel.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;
        final SavedPagesModel model = listSavedPagesModel.get(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_saved_pages_item,null);

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

        viewHolder.txt_title.setText(model.getTitle());
        viewHolder.txt_pageLink.setText(model.getLink());
        viewHolder.txt_dateTime.setText(model.getTimestamp());

        viewHolder.txt_viewInBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listSavedPagesModel.get(position);
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

