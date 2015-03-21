package org.kilkaari.library.constants;

/**
 * Created by vk on 14-03-2015.
 */
public interface Constants {

    public interface Table
    {
        public String TABLE_BOOKS = "Books";
        public String TABLE_AVAILABILITY = "Availability";
        public String TABLE_ISSUE = "Issue";
        public String TABLE_READ = "Read";
        public String TABLE_REQUEST_QUEUE = "RequestQueue";
        public String TABLE_SAVED_PAGES = "SavedPages";


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



    }
}
