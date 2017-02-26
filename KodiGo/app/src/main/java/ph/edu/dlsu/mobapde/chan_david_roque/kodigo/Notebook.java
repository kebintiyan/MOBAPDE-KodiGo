package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by USER on 2/25/2017.
 */

public class Notebook implements Parcelable {

    private String notebookID;
    private String title;
    private String titleColor;
    private String notebookColor;
    private String dateCreated;

    public Notebook(){

    }

    protected Notebook(Parcel in) {
        notebookID = in.readString();
        title = in.readString();
        titleColor = in.readString();
        notebookColor = in.readString();
        dateCreated = in.readString();
    }

    public Notebook(String notebookID, String title, String titleColor, String notebookColor, String dateCreated){
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

    public String getNotebookID() {
        return this.notebookID;
    }

    public void setNotebookID(String notebookID) {
        this.notebookID = notebookID;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleColor() {
        return this.titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getNotebookColor() {
        return this.notebookColor;
    }

    public void setNotebookColor(String notebookColor) {
        this.notebookColor = notebookColor;
    }

    public String getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.notebookID = notebookID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notebookID);
        dest.writeString(title);
        dest.writeString(titleColor);
        dest.writeString(notebookColor);
        dest.writeString(dateCreated);
    }
}
