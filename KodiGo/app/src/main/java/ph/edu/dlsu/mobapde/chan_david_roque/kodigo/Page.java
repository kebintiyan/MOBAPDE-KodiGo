package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by USER on 2/25/2017.
 */

public class Page implements Parcelable {

    private String pageID;
    private String notebookID;
    private String name;
    private ArrayList<String> text;
    private ArrayList<Image> images;
    private ArrayList<Comment> comments;
    private int pageNumber;
    private String dateCreated;

    public Page(){

    }

    protected Page(Parcel in) {
        setPageID(in.readString());
        notebookID = in.readString();
        name = in.readString();
        text = in.readArrayList(null);
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
        dest.writeString(getPageID());
        dest.writeString(getNotebookID());
        dest.writeString(getName());
        //dest.writeStringArray(getText());

        dest.writeString(getDateCreated());
    }

    public String getPageID() {
        return pageID;
    }

    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    public String getNotebookID() {
        return notebookID;
    }

    public void setNotebookID(String notebookID) {
        this.notebookID = notebookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
