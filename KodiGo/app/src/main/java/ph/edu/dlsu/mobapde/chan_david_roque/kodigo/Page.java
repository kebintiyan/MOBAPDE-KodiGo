package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by USER on 2/25/2017.
 */

public class Page{

    public static final String TABLE_NAME           = "page";
    public static final String COLUMN_PAGE_ID       = "_id";
    public static final String COLUMN_NOTEBOOK_ID   = "notebookID";
    public static final String COLUMN_NAME          = "name";
    public static final String COLUMN_TEXT          = "text";
    public static final String COLUMN_PAGE_NUMBER   = "pageNumber";
    public static final String COLUMN_DATE_CREATED  = "dateCreated";
    public static final String TRIGGER_ON_CREATE    = "create_page";

    private long pageID;
    private long notebookID;
    private String name;
    private String text;
    private int pageNumber;
    private String dateCreated;

    private ArrayList<Image> images;
    private ArrayList<Comment> comments;

    private int layoutPosition; // to be set only in onBindViewHolder

    public Page(){
        name = "Untitled Page";
        text = "none";
        images = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public long getPageID() {
        return pageID;
    }

    public Page setPageID(long pageID) {
        this.pageID = pageID;
        return this;
    }

    public long getNotebookID() {
        return notebookID;
    }

    public Page setNotebookID(long notebookID) {
        this.notebookID = notebookID;
        return this;
    }

    public String getName() {
        return name;
    }

    public Page setName(String name) {
        this.name = name;
        return this;
    }

    public String getText() {
        return text;
    }

    public Page setText(String text) {
        this.text = text;
        return this;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public Page setImages(ArrayList<Image> images) {
        this.images = images;
        return this;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public Page setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public Page setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public Page setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
    public int getLayoutPosition() {
        return layoutPosition;
    }

    public void setLayoutPosition(int layoutPosition) {
        this.layoutPosition = layoutPosition;
    }
}
