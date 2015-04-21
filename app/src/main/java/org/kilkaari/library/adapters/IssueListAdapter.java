package org.kilkaari.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.activities.BookDetailsActivity;
import org.kilkaari.library.activities.BookListActivity;
import org.kilkaari.library.activities.IssueActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.models.RequestQueueModel;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class IssueListAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<RequestQueueModel> listRequest;
    private IssueActivity context;
    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader loader;
    private SaveDataUtils saveDataUtils;

    public IssueListAdapter(BaseActivity context, List<RequestQueueModel> list){

        this.listRequest = list;
        this.context = (IssueActivity)context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        saveDataUtils = new SaveDataUtils(context);

        options = context.getLibraryApplication().getImageLoaderOptions();
        loader = context.getLibraryApplication().getImageLoader();



    }

    @Override
    public int getCount() {
        return listRequest.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;
        final RequestQueueModel model = listRequest.get(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_issue_list,null);

            viewHolder = new ViewHolder();
            viewHolder.img_bookIcon = (ImageView)convertView.findViewById(R.id.img_bookIcon);
            viewHolder.txt_bookName = (TextView)convertView.findViewById(R.id.txt_bookName);
            viewHolder.txt_authorName  = (TextView)convertView.findViewById(R.id.txt_authorName);
            viewHolder.img_availability  = (ImageView)convertView.findViewById(R.id.img_availability);
            viewHolder.img_moreOptions  = (ImageView)convertView.findViewById(R.id.img_moreOptions);
            viewHolder.img_userImage  = (ImageView)convertView.findViewById(R.id.img_userImage);
            viewHolder.txt_userName  = (TextView)convertView.findViewById(R.id.txt_userName);
            viewHolder.txt_userEmail  = (TextView)convertView.findViewById(R.id.txt_userEmail);
            viewHolder.txt_issue  = (TextView)convertView.findViewById(R.id.txt_issue);
            viewHolder.txt_issue  = (TextView)convertView.findViewById(R.id.txt_issue);


            convertView.setTag(viewHolder);
    }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }



        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return listRequest.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  0;
    }

    public static class ViewHolder{
        ImageView img_bookIcon;
        TextView txt_bookName;
        TextView txt_authorName;
        ImageView img_moreOptions;
        ImageView img_availability;
        TextView txt_userName;
        TextView txt_userEmail;
        TextView txt_issue;
        ImageView img_userImage;



    }
}

