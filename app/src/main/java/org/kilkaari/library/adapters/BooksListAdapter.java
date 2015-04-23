package org.kilkaari.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.activities.BookDetailsActivity;
import org.kilkaari.library.activities.BookListActivity;
import org.kilkaari.library.activities.BooksCategoriesActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.Availability;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.SaveDataUtils;

import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class BooksListAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<BooksModel> listBooks;
    private BookListActivity context;
    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader loader;
    private float px;
    private SaveDataUtils saveDataUtils;

    public BooksListAdapter(BaseActivity context, List<BooksModel> list){

        this.listBooks = list;
        this.context = (BookListActivity)context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        saveDataUtils = new SaveDataUtils(context);

        options = context.getLibraryApplication().getImageLoaderOptions();
        loader = context.getLibraryApplication().getImageLoader();

        // > convert 15 dp into pixels for rounded corners
        Resources r = context.getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());

    }

    @Override
    public int getCount() {
        return listBooks.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


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
            viewHolder.img_moreOptions  = (ImageView)convertView.findViewById(R.id.img_moreOptions);

            convertView.setTag(viewHolder);
    }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_bookName.setText(model.getName());
        viewHolder.txt_authorName.setText(model.getAuthor());

        if(model.getPhotoUrl()!=null) {
            loader.displayImage(model.getPhotoUrl(), viewHolder.img_bookIcon, options);
        }
        else {

            viewHolder.img_bookIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_book_default));

        }

        if(context.hash_booksAvailability.get(model.getObjectId())!=null)
        {
            if(context.hash_booksAvailability.get(model.getObjectId())) {
                //> programmatically set rounded corners and background color
                GradientDrawable shape =  new GradientDrawable();
                shape.setCornerRadius(px);
                shape.setColor(context.getResources().getColor(R.color.available));
                viewHolder.txt_availability.setTextColor(Color.WHITE);
                viewHolder.txt_availability.setText("A");
                viewHolder.txt_availability.setBackgroundDrawable(shape);
                viewHolder.txt_availability.setBackground(shape);
            }
            else {
                GradientDrawable shape = new GradientDrawable();
                shape.setCornerRadius(px);
                shape.setColor(context.getResources().getColor(R.color.inQueue));
                viewHolder.txt_availability.setTextColor(Color.WHITE);
                viewHolder.txt_availability.setText("Q");
                viewHolder.txt_availability.setBackgroundDrawable(shape);
                viewHolder.txt_availability.setBackground(shape);
            }
        }
        else {
            //> regret
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(px);
            shape.setColor(context.getResources().getColor(R.color.regret));
            viewHolder.txt_availability.setTextColor(Color.WHITE);
            viewHolder.txt_availability.setText("R");
            viewHolder.txt_availability.setBackgroundDrawable(shape);
            viewHolder.txt_availability.setBackground(shape);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*    Intent intent  = new Intent(context, BookDetailsActivity.class);
                intent.putExtra(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX,position);
                context.startActivity(intent);*/

            }
        });

        //> click listener on To_read icon
        viewHolder.img_toread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewHolder.img_toread.getTag()== null)
                {

                    viewHolder.img_toread.setTag("Clicked");
                    viewHolder.img_alreadyRead.setTag(null);
                    viewHolder.img_toread.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_requested_selected));
                    viewHolder.img_alreadyRead.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_already_read_inactive));
                }

            }
        });

        //> click listener on already_read icon
        viewHolder.img_alreadyRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewHolder.img_alreadyRead.getTag() ==  null)
                {

                    viewHolder.img_alreadyRead.setTag("Clicked");
                    viewHolder.img_toread.setTag(null);
                    viewHolder.img_alreadyRead.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_already_read));
                    viewHolder.img_toread.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_toread));
                }

            }
        });

        //> click listener on request icon
        viewHolder.img_requested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.img_requested.getTag() == null)
                {
                    //> save request data with isRequest == true
                    saveDataUtils.saveRequest(model.getObjectId(),true,"12/05/2015","2 weeks");


                    viewHolder.img_requested.setTag("Requested");
                }
                else
                {
                    //> save request data with isRequest = false;
                    saveDataUtils.saveRequest(model.getObjectId(),false,"12/05/2015","2 weeks");
                    viewHolder.img_requested.setTag(null);

                }

            }
        });

        //> click listener on icon for more options
        viewHolder.img_moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //> start Activity from BookListActivity
                context.showPopupWindow(v,position);

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
        ImageView img_bookIcon;
        TextView txt_bookName;
        TextView txt_authorName;
        TextView txt_availability;
        RatingBar ratingBar;
        ImageView img_toread;
        ImageView img_alreadyRead;
        ImageView img_requested;
        ImageView img_moreOptions;


    }
}

