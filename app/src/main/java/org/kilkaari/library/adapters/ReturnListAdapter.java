package org.kilkaari.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.activities.IssueActivity;
import org.kilkaari.library.activities.ReturnActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.IssueListModel;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class ReturnListAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<IssueListModel> issueList;
    private ReturnActivity context;
    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader loader;
    private SaveDataUtils saveDataUtils;

    public ReturnListAdapter(BaseActivity context, List<IssueListModel> list){

        this.issueList = list;
        this.context = (ReturnActivity)context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        saveDataUtils = new SaveDataUtils(context);

        options = context.getLibraryApplication().getImageLoaderOptions();
        loader = context.getLibraryApplication().getImageLoader();



    }

    @Override
    public int getCount() {
        return issueList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;
        final IssueListModel model = issueList.get(position);


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
            viewHolder.txt_issue.setText("Return");


            convertView.setTag(viewHolder);
    }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //> get Data for  books
        ParseObject bookObject = model.getBookObject();
        viewHolder.txt_bookName.setText(bookObject.getString(Constants.DataColumns.BOOKS_NAME));
        viewHolder.txt_authorName.setText(bookObject.getString(Constants.DataColumns.BOOKS_AUTHOR));

        ParseFile bookImage = bookObject.getParseFile(Constants.DataColumns.BOOKS_PHOTO);
        if(bookImage!=null) {

            loader.displayImage(bookImage.getUrl(), viewHolder.img_bookIcon, options);
        }
        else
        {
            viewHolder.img_bookIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_book_default));
        }

        //> get Data for  books
        ParseObject userObject = model.getBookObject();
        viewHolder.txt_userName.setText(userObject.getString(Constants.DataColumns.USER_NAME));
        viewHolder.txt_userEmail.setText(userObject.getString(Constants.DataColumns.USER_EMAIL));



        viewHolder.txt_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!model.isReturned())
               {
                   context.updateIssueList(model,"24/04/2015","5.0");
               }
            }
        });

        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return issueList.get(position);
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

