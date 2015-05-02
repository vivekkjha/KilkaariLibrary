package org.kilkaari.library.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;

/**
 * Created by vivek on 30/04/15.
 */
public class BookDetailsFragment extends Fragment{

    //> layout parameters
    private TextView txt_categoryNameDetails,txt_donatedBy,txt_addedOn,txt_bookNameDetails,txt_authorNameDetails
            ,txt_languageDetails,txt_publisherDetails,txt_publishingYear,txt_pageCountDetails,txt_isbnDetails,txt_descriptionDetails;
    private RatingBar ratingBarDetails;
    private ImageView img_bookDetailsImage,img_toread,img_alreadyRead,img_requested;
    private LinearLayout lin_bookOptions;
    private View rootView;

    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader loader;
    private BaseActivity activity;
    private int pos = -1;
    private boolean isEdit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

          //> get position for selected Book to show it's details
        pos = getArguments().getInt(Constants.EXTRAS.EXTRAS_SELECTED_CATEGORY);
        isEdit = getArguments().getBoolean(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT);


        rootView = inflater.inflate(R.layout.activity_book_details,container,false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (BaseActivity)getActivity();
        initializeLayout();

        //> actions on top title bar and done layout from baseActivity
        activity.setHeading("Book Details");
        activity.showHideDone(false);



    }

    @Override
    public void onStart() {
        super.onStart();

        if(pos!=-1) {
            getBookDetails(pos);
        }


    }
    //> initialize layout parameters
    private void initializeLayout()
    {
        //> TextViews
        txt_categoryNameDetails = (TextView)rootView.findViewById(R.id.txt_categoryNameDetails);
        txt_donatedBy = (TextView)rootView.findViewById(R.id.txt_donatedBy);
        txt_addedOn = (TextView)rootView.findViewById(R.id.txt_addedOn);
        txt_bookNameDetails = (TextView)rootView.findViewById(R.id.txt_bookNameDetails);
        txt_authorNameDetails = (TextView)rootView.findViewById(R.id.txt_authorNameDetails);
        txt_languageDetails = (TextView)rootView.findViewById(R.id.txt_languageDetails);
        txt_publisherDetails = (TextView)rootView.findViewById(R.id.txt_publisherDetails);
        txt_publishingYear = (TextView)rootView.findViewById(R.id.txt_publishingYear);
        txt_publisherDetails = (TextView)rootView.findViewById(R.id.txt_publisherDetails);
        txt_pageCountDetails = (TextView)rootView.findViewById(R.id.txt_pageCountDetails);
        txt_isbnDetails = (TextView)rootView.findViewById(R.id.txt_isbnDetails);
        txt_descriptionDetails = (TextView)rootView.findViewById(R.id.txt_descriptionDetails);
        lin_bookOptions = (LinearLayout)rootView.findViewById(R.id.lin_bookOptions);

        //> hide book options when opened in edit mode
        if(isEdit)
        {
            lin_bookOptions.setVisibility(View.INVISIBLE);
        }

        //> rating bar
        ratingBarDetails = (RatingBar)rootView.findViewById(R.id.ratingBarDetails);

        //> ImageViews
        img_bookDetailsImage = (ImageView)rootView.findViewById(R.id.img_bookDetailsImage);
        img_toread = (ImageView)rootView.findViewById(R.id.img_toread);
        img_alreadyRead = (ImageView)rootView.findViewById(R.id.img_alreadyRead);
        img_requested = (ImageView)rootView.findViewById(R.id.img_requested);

        //> image loader
        options = activity.getLibraryApplication().getImageLoaderOptions();
        loader = activity.getLibraryApplication().getImageLoader();

    }

    public void getBookDetails(int pos)
    {
        BooksModel model = activity.getList_books().get(pos);
        //> set different layouts elements with data in model
        LogUtil.w("BookDetailsActivity", "Model object " + model);

        txt_categoryNameDetails.setText(model.getCategory());
        txt_donatedBy.setText(model.getDonatedBy());
        txt_addedOn.setText(model.getAddedOn());
        txt_bookNameDetails.setText(model.getName());
        txt_authorNameDetails.setText("by "+model.getAuthor());
        txt_languageDetails.setText("in "+model.getLanguage());
        txt_publisherDetails.setText(model.getPublisher());
        txt_publishingYear.setText(model.getPublicationYear()+"");
        txt_pageCountDetails.setText(model.getPageCount()+"");
        txt_isbnDetails.setText("1234567890123");

        if(model.getPhotoUrl()!=null && !model.getPhotoUrl().equals("")) {
            loader.displayImage(model.getPhotoUrl(), img_bookDetailsImage, options);
        }
        else {
            img_bookDetailsImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_book_default));
        }
    }
}
