package org.kilkaari.library.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.kilkaari.library.R;
import org.kilkaari.library.models.SlideItem;


/**
 * Created by vivek on 19/02/15.
 */
public class NavigationalDrawerAdapter extends ArrayAdapter<SlideItem> {


    private Context context;
    private LayoutInflater inflater = null;
    private SlideItem[] list;

    public NavigationalDrawerAdapter(Context context, SlideItem[] list) {
        super(context, R.layout.layout_slide_item, list);
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            //inflate your layout
            SlideItem item = this.getItem(position);
            convertView = inflater.inflate(R.layout.layout_slide_item, null);
            viewHolder = new ViewHolder();
            viewHolder.count = item.getId();
            viewHolder.title = item.getTitle();
            viewHolder.image = this.getContext().getResources().getDrawable(item.getImageId());

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ((ImageView) convertView.findViewById(R.id.img_icon)).setImageDrawable(viewHolder.image);
        ((TextView) convertView.findViewById(R.id.txt_itemText)).setText(viewHolder.title);
        if(viewHolder.count!=-1) {
            ((TextView) convertView.findViewById(R.id.txt_count)).setText(viewHolder.count +"");
        }
        else
        {
            ((TextView) convertView.findViewById(R.id.txt_count)).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public static class ViewHolder {
        int count;
        String title;
        Drawable image;
    }
}
