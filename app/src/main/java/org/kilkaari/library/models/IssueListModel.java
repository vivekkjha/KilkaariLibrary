package org.kilkaari.library.models;

import com.parse.ParseObject;

/**
 * Created by vivek on 23/04/15.
 */
public class IssueListModel {

    private ParseObject userObject;
    private ParseObject bookObject;
    private String returnTimestamp;
    private String issueTimestamp;
    private String timePeriod;
    private String fees;
    private boolean isReturned;

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

    public String getReturnTimestamp() {
        return returnTimestamp;
    }

    public void setReturnTimestamp(String returnTimestamp) {
        this.returnTimestamp = returnTimestamp;
    }

    public String getIssueTimestamp() {
        return issueTimestamp;
    }

    public void setIssueTimestamp(String issueTimestamp) {
        this.issueTimestamp = issueTimestamp;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }
}
