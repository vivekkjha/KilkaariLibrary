package org.kilkaari.library.models;

import android.graphics.Bitmap;

/**
 * Created by vk on 21-03-2015.
 */
public class BookCategoriesModel {

    private String category;
    private int count;

    private String photoUrl;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


}
