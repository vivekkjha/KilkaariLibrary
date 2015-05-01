package org.kilkaari.library.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.LibrarianActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vivek on 27/04/15.
 */
public class FragmentAddBooks extends android.support.v4.app.Fragment implements View.OnClickListener{

    public static byte[] imageBytes =  null;
    //> layout related objects
    private View rootView;
    private EditText edt_donorName,edt_date,edt_bookName,edt_authorName,edt_language,edt_pageCount,edt_publisher,edt_publishingYear,edt_description;
    private AutoCompleteTextView autoTxt_category;
    private ImageView img_bookImage;
    private LinearLayout lin_topDone;

    //> program objects
    private LibrarianActivity activity;
    private String camera = "Camera";
    private String gallery = "Gallery";
    private String removeImage = "Remove";
    private int CAMERA_RESULT_CODE = 111;
    private int GALLERY_RESULT_CODE = 222;
    private boolean isFromGallery = false;
    private Uri capturedFileUri = null;
    private Bitmap bookBitmapImage = null;
    private boolean isCategoryImageSaved = false;


    //> get categories from parse
    private List<String> list_booksCategories;

    // private String categories = "";

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image from Camera*/

    private static File getOutputMediaFile(){

        String extr = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(extr +File.separator + Constants.Folders.FOLDER_KILKAARI );

        if (!mFolder.exists()) {
            mFolder.mkdir();
        }

        String strF = mFolder.getAbsolutePath();
        File mSubFolder = new File(strF);

        if (! mSubFolder.exists()){
            if (! mSubFolder.mkdirs()){
                LogUtil.d("MKdirs", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mSubFolder.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        list_booksCategories = new ArrayList<String>();

        //> get books categories from preferences separated bt $%^

      /*  if(prefs.getBooksCategories()!=null) {
            String[] categoryArray = prefs.getBooksCategories().split(Constants.CATEGORIES_DELIMITER);
            for (int i = 0; i < categoryArray.length; i++) {
                list_booksCategories.add(categoryArray[i]);
            }
        }*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.layout_add_books,container,false);

        edt_donorName = (EditText)rootView.findViewById(R.id.edt_donorName);
        edt_date = (EditText)rootView.findViewById(R.id.edt_date);
        edt_bookName = (EditText)rootView.findViewById(R.id.edt_bookName);
        edt_authorName = (EditText)rootView.findViewById(R.id.edt_authorName);
        edt_language = (EditText)rootView.findViewById(R.id.edt_language);
        edt_pageCount = (EditText)rootView.findViewById(R.id.edt_pageCount);
        edt_publisher = (EditText)rootView.findViewById(R.id.edt_publisher);
        edt_publishingYear = (EditText)rootView.findViewById(R.id.edt_publishingYear);
        edt_description = (EditText)rootView.findViewById(R.id.edt_description);
        autoTxt_category = (AutoCompleteTextView)rootView.findViewById(R.id.autoTxt_category);
        img_bookImage = (ImageView)rootView.findViewById(R.id.img_bookImage);
        img_bookImage.setOnClickListener(this);

        lin_topDone = (LinearLayout)getActivity().findViewById(R.id.lin_topDone);
        lin_topDone.setOnClickListener(this);


        registerForContextMenu(img_bookImage);
        this.getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        activity = (LibrarianActivity)getActivity();
        //> get Categories list from parse server
        activity.showProgressLayout();
        getCategories();

        //> add adapter for auto-complete

        ArrayAdapter adapter = new ArrayAdapter
                (activity, android.R.layout.simple_list_item_1, list_booksCategories);
        autoTxt_category.setAdapter(adapter);
        autoTxt_category.setThreshold(1);
        autoTxt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoTxt_category.setText(parent.getItemAtPosition(position).toString());
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
      public void onCreateContextMenu(ContextMenu menu, View v,
                                      ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(camera);
        menu.add(gallery);
        if(imageBytes !=null) {
            menu.add(removeImage);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //> Open camera to take pics on selection of camera
        if(item.getTitle().equals(camera))
        {
            //> open camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            capturedFileUri = getOutputMediaFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedFileUri);
            startActivityForResult(intent, CAMERA_RESULT_CODE);

        }
        //> open gallery to take photos from gallery
        else if(item.getTitle().equals(gallery))
        {
            //> open gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Get photos from…"),GALLERY_RESULT_CODE);
        }
        //> remove image from ImageView
        else if(item.getTitle().equals(removeImage))
        {
            img_bookImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_book_default));
            imageBytes =null;
        }
        else
        {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.img_bookImage)
        {
            //> open Context menu for image
            activity.openContextMenu(img_bookImage);

        }
        else if(v.getId() == R.id.lin_topDone)
        {

            //> save entered category ind atabase
            saveCategories();

            //> save details of book entered
            saveBookDetails();
        }
    }

    // > save asked question
    public void saveBookDetails()
    {
        //show progress layout when uploading of data starts
        activity.showProgressLayout();
        try {
            //> Delete whitespaces from image name and set it to string variable
            String imageName = edt_bookName.getText().toString().replaceAll("\\s+","");

            //> file to store photo
            ParseFile photo =null;
            if(imageBytes!=null)
            {
                photo = new ParseFile(imageName+".jpg",imageBytes);
            }


            ParseObject newBook = new ParseObject(Constants.Table.TABLE_BOOKS);
            newBook.put(Constants.DataColumns.BOOKS_DONATED_BY, edt_donorName.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_ADDED_ON, edt_date.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_PAGE_COUNT, Integer.parseInt(edt_pageCount.getText().toString()));
            newBook.put(Constants.DataColumns.BOOKS_DESCRIPTION, edt_description.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_AUTHOR, edt_authorName.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_CATEGORY, autoTxt_category.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_LANGUAGE, edt_language.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_PUBLISHER, edt_publisher.getText().toString());
            newBook.put(Constants.DataColumns.BOOKS_PUBLICATION_YEAR, Integer.parseInt(edt_publishingYear.getText().toString()));
            newBook.put(Constants.DataColumns.BOOKS_NAME, edt_bookName.getText().toString());

            //> check if the photo data is null or not
            if(photo!=null) {
                newBook.put(Constants.DataColumns.BOOKS_PHOTO, photo);

            }

            newBook.put(Constants.DataColumns.BOOKS_QUANTITY, 1);

            ParseObject bookAvail = new ParseObject(Constants.Table.TABLE_AVAILABILITY);
            bookAvail.put(Constants.DataColumns.AVAILABLE_BOOK_POINT,newBook);
            bookAvail.put(Constants.DataColumns.AVAILABLE_AVAILABLE,true);
            bookAvail.put(Constants.DataColumns.AVAILABLE_QUANTITY,1);
            bookAvail.save();
            LogUtil.d("Books Row ", "Created");

            if(imageBytes!=null &&  isCategoryImageSaved)
            {
                //> after uploading set imageBytes to null , so that same image cannot be selected for different books
                imageBytes = null;
                isCategoryImageSaved = false;
            }



        }
        catch (ParseException ex)
        {
            ex.printStackTrace();

        }
        //> hide progress layout on complete
        activity.hideProgressLayout();
    }

    /*--- get uri from file saved----*/

    //> get categories from parse server
    public void getCategories()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, com.parse.ParseException e) {
                //> hide loading layout
                activity.hideProgressLayout();
                if (e == null) {
                    Log.d("Categories", "Retrieved " + categoryList.size() + " rows");

                    if(categoryList.size()!=0)
                    {
                        list_booksCategories.clear();
                        for (int i=0;i<categoryList.size();i++)
                        {
                            list_booksCategories.add(categoryList.get(i).getString("category"));
                            LogUtil.w("Books Categories","Category : "+ categoryList.get(i).getString("category"));
                        }

                    }
                } else {
                    Log.d("Categories", "Error: " + e.getMessage());
                }
            }
        });
    }

    //> save categories on server
    public void saveCategories()
    {
        //> check if the category already exist or not
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Table.TABLE_CATEGORIES);
        query.whereEqualTo(Constants.DataColumns.CATEGORIES_CATEGORY,autoTxt_category.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Categories", "Retrieved " + categoryList.size() + " rows");

                    //> Delete whitespaces from image name and set it to string variable
                    String imageName = autoTxt_category.getText().toString().replaceAll("\\s+","");
                    if(categoryList.size()==0)
                    {
                        //show progress layout when uploading of data starts
                        activity.showProgressLayout();

                        //> file to store photo
                        ParseFile photo =null;
                        if(imageBytes!=null)
                        {
                            photo = new ParseFile(imageName+".jpg",imageBytes);
                        }

                        try {
                            //> if category does not exist. insert in table
                            ParseObject newCategory = new ParseObject(Constants.Table.TABLE_CATEGORIES);
                            newCategory.put(Constants.DataColumns.CATEGORIES_CATEGORY, autoTxt_category.getText().toString());

                            //> check if the photo data is null or not
                            if(photo!=null) {
                                newCategory.put(Constants.DataColumns.CATEGORIES_PHOTO, photo);
                            }
                            newCategory.save();

                            //> hide progress layout on complete
                            activity.hideProgressLayout();

                            //> set CategoryImage flag to true;
                            isCategoryImageSaved = true;
                        }
                        catch (ParseException pe)
                        {pe.printStackTrace();}


                    }
                } else {
                    //> if list size is not 0, then no need to store image for category
                    isCategoryImageSaved = true;
                    Log.d("Categories", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_RESULT_CODE)
        {
            if(resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    isFromGallery = true;
                    Uri galleryUri = data.getData();

                    try {
                        bookBitmapImage = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(galleryUri));
                        img_bookImage.setImageBitmap(bookBitmapImage);
                    } catch (FileNotFoundException fe) {
                        fe.printStackTrace();
                    }

                    // Convert it to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Compress image to lower quality scale 1 - 100
                    bookBitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    imageBytes = stream.toByteArray();
                }
            }
        }
        else if(requestCode == CAMERA_RESULT_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {

                isFromGallery = false;
                Uri galleryUri = capturedFileUri;

                try {
                    bookBitmapImage = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(galleryUri));
                    img_bookImage.setImageBitmap(bookBitmapImage);
                } catch (FileNotFoundException fe) {
                    fe.printStackTrace();
                }

                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bookBitmapImage.compress(Bitmap.CompressFormat.JPEG, 10, stream);

                imageBytes = stream.toByteArray();

            }
        }
    }


}
