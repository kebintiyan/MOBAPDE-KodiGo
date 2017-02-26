package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by USER on 2/25/2017.
 */

public class Page implements Parcelable {

    public static final String TABLE_NAME           = " page ";
    public static final String COLUMN_PAGE_ID       = " pageID ";
    public static final String COLUMN_NOTEBOOK_ID   = " notebookID ";
    public static final String COLUMN_NAME          = " name ";
    public static final String COLUMN_TEXT          = " text ";
    public static final String COLUMN_PAGE_NUMBER   = " pageNumber ";
    public static final String COLUMN_DATE_CREATED  = " dateCreated ";

    private int pageID;
    private int notebookID;
    private String name;
    private String text;
    private int pageNumber;
    private String dateCreated;

    private ArrayList<Image> images;
    private ArrayList<Comment> comments;

    public Page(){

    }

    protected Page(Parcel in) {
        setPageID(in.readInt());
        notebookID = in.readInt();
        name = in.readString();
        text = in.readString();
        images = in.readArrayList(null);
        comments = in.readArrayList(null);
        pageNumber = in.readInt();
        setDateCreated(in.readString());
    }

    public static final Creator<Page> CREATOR = new Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel in) {
            return new Page(in);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getPageID());
        dest.writeInt(getNotebookID());
        dest.writeString(getName());
        //dest.writeStringArray(getText());

        dest.writeString(getDateCreated());
    }

    public int getPageID() {
        return pageID;
    }

    public Page setPageID(int pageID) {
        this.pageID = pageID;
        return this;
    }

    public int getNotebookID() {
        return notebookID;
    }

    public Page setNotebookID(int notebookID) {
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
}
