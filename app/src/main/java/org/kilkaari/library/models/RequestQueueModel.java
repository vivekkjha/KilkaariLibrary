package org.kilkaari.library.models;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by vk on 11-04-2015.
 */
public class RequestQueueModel {

    private ParseObject userObject;
    private ParseObject bookObject;
    private String timestamp;
    private String timePeriod;

    public ParseObject getUserObject() {
        return userObject;
    }

    public void setUserObject(ParseObject userObject) {
        this.userObject = userObject;
    }

    public ParseObject getBookObject() {
        return bookObject;
    }

    public void setBookObject(ParseObject bookObject) {
        this.bookObject = bookObject;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }


}
