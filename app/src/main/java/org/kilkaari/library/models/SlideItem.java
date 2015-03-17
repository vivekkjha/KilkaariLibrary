package org.kilkaari.library.models;


public class SlideItem {
    private int count;
    private String title;
    private int imageId;

    public SlideItem(int count, String title, int imageId) {
        this.count = count;
        this.title = title;
        this.imageId = imageId;
    }

    public int getId() {
        return count;
    }

    public void setId(int count) {
        this.count = count;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
