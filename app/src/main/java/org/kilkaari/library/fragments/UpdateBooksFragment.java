package org.kilkaari.library.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.kilkaari.library.R;
import org.kilkaari.library.activities.BaseActivity;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;

/**
 * Created by vivek on 29/04/15.
 */
public class UpdateBooksFragment extends Fragment {

    private View rootView;
    private BaseActivity activity;

    private EditText edt_donorName,edt_date,edt_bookName,edt_authorName,edt_language,edt_pageCount,edt_publisher,edt_publishingYear,edt_description;
    private AutoCompleteTextView autoTxt_category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_update_books,container,false);

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



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity)getActivity();

        //> actions on top title bar and done layout from baseActivity
        activity.setHeading("Update Books");
        activity.showHideDone(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        int pos = this.getArguments().getInt(Constants.EXTRAS.EXTRAS_BOOK_IS_EDIT);

        if(activity.getList_books().size()!=0) {

            //> assign book details into model
            BooksModel model = activity.getList_books().get(pos);

            //> setEditTexts for edit
            edt_donorName.setText(model.getDonatedBy());
            edt_date.setText(model.getAddedOn());
            edt_bookName.setText(model.getName());
            edt_authorName.setText(model.getAuthor());
            edt_language.setText(model.getLanguage());
            edt_pageCount.setText(model.getPageCount() + "");
            edt_publisher.setText(model.getPublisher());
            edt_publishingYear.setText(model.getPublicationYear() + "");
            edt_description.setText(model.getDescription());
            autoTxt_category.setText(model.getCategory());
        }

    }
}
