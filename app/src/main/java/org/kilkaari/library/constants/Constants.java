package org.kilkaari.library.constants;

/**
 * Created by vk on 14-03-2015.
 */
public interface Constants {

    public String CATEGORIES_DELIMITER ="$%^";

    public interface Folders
    {
        public String FOLDER_KILKAARI = "Kilkaari";

    }

    public interface Table
    {
        public String TABLE_USER = "_User";
        public String TABLE_BOOKS = "Books";
        public String TABLE_AVAILABILITY = "Availability";
        public String TABLE_ISSUE = "Issue";
        public String TABLE_READ = "Read";
        public String TABLE_REQUEST_QUEUE = "RequestQueue";
        public String TABLE_SAVED_PAGES = "SavedPages";
        public String TABLE_CATEGORIES = "Categories";


    }

    public interface DataColumns
    {
        public String BOOKS_DONATED_BY = "donatedBy";
        public String BOOKS_ADDED_ON = "addedOn";
        public String BOOKS_PAGE_COUNT = "pageCount";
        public String BOOKS_DESCRIPTION = "description";
        public String BOOKS_CATEGORY = "category";
        public String BOOKS_LANGUAGE = "language";
        public String BOOKS_PUBLISHER = "publisher";
        public String BOOKS_PUBLICATION_YEAR = "publicationYear";
        public String BOOKS_QUANTITY = "quantity";
        public String BOOKS_NAME = "name";
        public String BOOKS_AUTHOR = "author";
        public String BOOKS_PHOTO = "photo";

        public String CATEGORIES_CATEGORY = "category";
        public String CATEGORIES_PHOTO = "photo";

        public String AVAILABLE_AVAILABLE = "available";
        public String AVAILABLE_BOOK_ID = "bookId";
        public String AVAILABLE_BOOK = "book";
        public String AVAILABLE_BOOK_POINT = "bookPoint";
        public String AVAILABLE_QUANTITY = "quantity";

        public String SAVED_PAGES_TITLE = "title";
        public String SAVED_PAGES_LINK = "link";
        public String SAVED_PAGES_TIMESTAMP = "timestamp";

        public String USER_EMAIL = "email";
        public String USER_FACEBOOK_DATA = "facebookData";

        public String REQUEST_QUEUE_TIMESTAMP = "timestamp";
        public String REQUEST_QUEUE_BOOK = "book";
        public String REQUEST_QUEUE_USER = "user";
        public String REQUEST_QUEUE_TIME_PERIOD = "timePeriod";



    }
    public interface EXTRAS
    {
        public String EXTRAS_SELECTED_CATEGORY = "selectedCategory";
        public String EXTRAS_OBJECT_ID = "objectId";
        public String EXTRAS_SELECTED_BOOK_INDEX = "selectedBookIndex";
    }
}
