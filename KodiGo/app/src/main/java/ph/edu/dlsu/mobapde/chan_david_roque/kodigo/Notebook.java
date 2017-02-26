package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by USER on 2/25/2017.
 */

public class Notebook implements Parcelable {

    public static final String TABLE_NAME               = " notebook ";
    public static final String COLUMN_NOTEBOOK_ID       = " notebookID ";
    public static final String COLUMN_TITLE             = " title ";
    public static final String COLUMN_TITLE_COLOR       = " titleColor ";
    public static final String COLUMN_NOTEBOOK_COLOR    = " notebookColor ";
    public static final String COLUMN_NOTEBOOK_NUMBER   = " notebookNumber ";
    public static final String COLUMN_DATE_CREATED      = " dateCreated ";

    private int notebookID;
    private String title;
    private String titleColor;
    private String notebookColor;
    private int notebookNumber;
    private String dateCreated;

    private ArrayList<Page> pages;

    public Notebook(){

    }

    protected Notebook(Parcel in) {
        notebookID = in.readInt();
        title = in.readString();
        titleColor = in.readString();
        notebookColor = in.readString();
        dateCreated = in.readString();
    }

    public Notebook(int notebookID, String title, String titleColor, String notebookColor, String dateCreated){
        this.notebookID = notebookID;
        this.title = title;
        this.titleColor = titleColor;
        this.notebookColor = notebookColor;
        this.dateCreated = dateCreated;
    }

    public static final Creator<Notebook> CREATOR = new Creator<Notebook>() {
        @Override
        public Notebook createFromParcel(Parcel in) {
            return new Notebook(in);
        }

        @Override
        public Notebook[] newArray(int size) {
            return new Notebook[size];
        }
    };

    public int getNotebookID() {
        return this.notebookID;
    }

    public Notebook setNotebookID(int notebookID) {
        this.notebookID = notebookID;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Notebook setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitleColor() {
        return this.titleColor;
    }

    public Notebook setTitleColor(String titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public String getNotebookColor() {
        return this.notebookColor;
    }

    public Notebook setNotebookColor(String notebookColor) {
        this.notebookColor = notebookColor;
        return this;
    }

    public ArrayList<Page> getPages() {
        return this.pages;
    }

    public Notebook setPages(ArrayList<Page> pages) {
        this.pages = pages;
        return this;
    }

    public int getNotebookNumber() {
        return this.notebookNumber;
    }

    public Notebook setNotebookNumber(int notebookNumber) {
        this.notebookNumber = notebookNumber;
        return this;
    }

    public String getDateCreated() {
        return this.dateCreated;
    }

    public Notebook setDateCreated(String dateCreated) {
        this.notebookID = notebookID;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(notebookID);
        dest.writeString(title);
        dest.writeString(titleColor);
        dest.writeString(notebookColor);
        dest.writeString(dateCreated);
    }
}
