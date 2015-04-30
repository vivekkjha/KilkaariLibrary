package org.kilkaari.library.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.kilkaari.library.R;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.LogUtil;

/**
 * Created by vivek on 20/03/15.
 */
public class BookDetailsActivity extends BaseActivity {

    //> layout parameters
    private TextView txt_categoryNameDetails,txt_donatedBy,txt_addedOn,txt_bookNameDetails,txt_authorNameDetails
            ,txt_languageDetails,txt_publisherDetails,txt_publishingYear,txt_pageCountDetails,txt_isbnDetails,txt_descriptionDetails;
    private RatingBar ratingBarDetails;
    private ImageView img_bookDetailsImage,img_toread,img_alreadyRead,img_requested;

    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        initializeLayout();

        int position = -1;
        position = getIntent().getExtras().getInt(Constants.EXTRAS.EXTRAS_SELECTED_BOOK_INDEX);

        if(position!=-1) {
            getBookDetails(position);
        }

    }

    //> initialize layout parameters
    private void initializeLayout()
    {
        //> TextViews
        txt_categoryNameDetails = (TextView)findViewById(R.id.txt_categoryNameDetails);
        txt_donatedBy = (TextView)findViewById(R.id.txt_donatedBy);
        txt_addedOn = (TextView)findViewById(R.id.txt_addedOn);
        txt_bookNameDetails = (TextView)findViewById(R.id.txt_bookNameDetails);
        txt_authorNameDetails = (TextView)findViewById(R.id.txt_authorNameDetails);
        txt_languageDetails = (TextView)findViewById(R.id.txt_languageDetails);
        txt_publisherDetails = (TextView)findViewById(R.id.txt_publisherDetails);
        txt_publishingYear = (TextView)findViewById(R.id.txt_publishingYear);
        txt_publisherDetails = (TextView)findViewById(R.id.txt_publisherDetails);
        txt_pageCountDetails = (TextView)findViewById(R.id.txt_pageCountDetails);
        txt_isbnDetails = (TextView)findViewById(R.id.txt_isbnDetails);
        txt_descriptionDetails = (TextView)findViewById(R.id.txt_descriptionDetails);

        //> rating bar
        ratingBarDetails = (RatingBar)findViewById(R.id.ratingBarDetails);

        //> ImageViews
        img_bookDetailsImage = (ImageView)findViewById(R.id.img_bookDetailsImage);
        img_toread = (ImageView)findViewById(R.id.img_toread);
        img_alreadyRead = (ImageView)findViewById(R.id.img_alreadyRead);
        img_requested = (ImageView)findViewById(R.id.img_requested);

       //> image loader
        options = getLibraryApplication().getImageLoaderOptions();
        loader = getLibraryApplication().getImageLoader();

    }
    public void getBookDetails(int pos)
    {
        BooksModel model = getList_books().get(pos);
        //> set different layouts elements with data in model
        LogUtil.w("BookDetailsActivity","Model object " + model);

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
