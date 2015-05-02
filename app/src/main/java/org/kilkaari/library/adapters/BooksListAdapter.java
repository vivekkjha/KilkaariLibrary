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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vivek on 17/03/15.
 */
public class BooksListAdapter extends BaseAdapter {

    private List<BooksModel> listBooks;
    private HashMap<String,Boolean> hash_booksAvailability;
    private boolean isEdit = false;
    private float px;

    private BaseActivity context;
    private LayoutInflater inflater;
    private SaveDataUtils saveDataUtils;

    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader loader;


    public BooksListAdapter(BaseActivity context,HashMap<String,Boolean> hash_booksAvailability, boolean isEdit){

        this.listBooks = context.getList_books();
        this.hash_booksAvailability = hash_booksAvailability;
        this.context = context;
        this.isEdit = isEdit;
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
            viewHolder.ratingBar  = (RatingBar)convertView.findViewById(R.id.ratingBar);
            viewHolder.img_toread  = (ImageView)convertView.findViewById(R.id.img_toread);
            viewHolder.img_alreadyRead  = (ImageView)convertView.findViewById(R.id.img_alreadyRead);
            viewHolder.img_requested  = (ImageView)convertView.findViewById(R.id.img_requested);
            viewHolder.img_moreOptions  = (ImageView)convertView.findViewById(R.id.img_moreOptions);
            viewHolder.img_availability  = (ImageView)convertView.findViewById(R.id.img_availability);
            viewHolder.lin_bookOptions = (LinearLayout)convertView.findViewById(R.id.lin_bookOptions);

            convertView.setTag(viewHolder);
    }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /*----- Add data into view controls ----------*/

        viewHolder.txt_bookName.setText(model.getName());
        viewHolder.txt_authorName.setText(model.getAuthor());

        //> set requested icon based on requests
        if(context.getList_requestBooksCurrentUser().size()!=0)
        {
            //> if request for this book has already been made
            if(context.getList_requestBooksCurrentUser().contains(model.getObjectId()))
            {
                //> then show different drawable
                viewHolder.img_requested.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_confirmation));
                viewHolder.img_requested.setTag("Requested");
            }
            else
            {
                //> then show different drawable
                viewHolder.img_requested.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_requested));
                viewHolder.img_requested.setTag(null);
            }
        }

        //> set Photo Url if available , else set Default icon
        if(model.getPhotoUrl()!=null) {
            loader.displayImage(model.getPhotoUrl(), viewHolder.img_bookIcon, options);
        }
        else {

            viewHolder.img_bookIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_book_default));

        }

        //> if in edit Mode , librarian don't need to see all the book options for user
        if(isEdit)
        {
            viewHolder.lin_bookOptions.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.lin_bookOptions.setVisibility(View.VISIBLE);
        }

        //> set availability tags according to the data received from server

        if(hash_booksAvailability.get(model.getObjectId())!=null)
        {
            if(hash_booksAvailability.get(model.getObjectId())) {

                //> if hash map has this value , it indicates that the book is available
                viewHolder.img_availability.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_available));
            }
            else {


                //> if hash map has doesn't have true for this books , it indicates that the book is not available
                viewHolder.img_availability.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_queue));
            }
        }
        else {

            //> if hash map has doesn't contain this value , it indicates that the book is not available and not in list
            viewHolder.img_availability.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_queue));

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
                    //> check if the book is not available then show Toast , informing user for same
                    if(hash_booksAvailability.get(model.getObjectId())!=null && hash_booksAvailability.get(model.getObjectId()))
                    {
                        //> save request data with isRequest == true
                        saveDataUtils.saveRequest(model.getObjectId(), true, "12/05/2015", "2 weeks");
                        viewHolder.img_requested.setTag("Requested");

                        //> change its drawable when request is been made
                        viewHolder.img_requested.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_confirmation));

                    }
                    else {

                        Toast.makeText(context,R.string.cannotBeRequested,Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    //> save request data with isRequest = false;
                    saveDataUtils.saveRequest(model.getObjectId(),false,"12/05/2015","2 weeks");
                    viewHolder.img_requested.setTag(null);

                    //> change its drawable when request has been changed
                    viewHolder.img_requested.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_requested));

                }

            }
        });

        //> click listener on icon for more options
        viewHolder.img_moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //> show popup window for more options resding in Base Actviity
                context.showBookPopupList(v,position,isEdit);

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
        ImageView img_availability;
        RatingBar ratingBar;
        ImageView img_toread;
        ImageView img_alreadyRead;
        ImageView img_requested;
        ImageView img_moreOptions;
        LinearLayout lin_bookOptions;


    }
}

