package org.kilkaari.library.models;

/**
 * Created by vk on 21-03-2015.
 */
public class BooksModel {



    private String objectId;
    private String name;
    private String author;
    private String category;
    private String donatedBy;
    private String addedOn;
    private String description;
    private String language;
    private String publisher;
    private int publicationYear;
    private int pageCount;
    private int quantity;
    private String photoUrl;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDonatedBy() {
        return donatedBy;
    }

    public void setDonatedBy(String donatedBy) {
        this.donatedBy = donatedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public int hashCode() {

        return objectId.hashCode();
    }

    // > to compare two objects we need to override equals and hashCode default implementation
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BooksModel other = (BooksModel) obj;

        // > comparing objectId of every object to detect if objects are equal
        if (!(objectId.equals(other.objectId)))
            return false;

        return true;
    }



}
