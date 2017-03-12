package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

/**
 * Created by USER on 2/26/2017.
 */

public class Image {

    public static final String TABLE_NAME       = "image";
    public static final String COLUMN_IMAGE_ID  = "_id";
    public static final String COLUMN_PAGE_ID   = "pageID";
    public static final String COLUMN_URL       = "url";

    private int imageID;
    private int pageID;
    private String url;

    public Image() {

    }

    public int getImageID() {
        return imageID;
    }

    public Image setImageID(int imageID) {
        this.imageID = imageID;
        return this;
    }

    public int getPageID() {
        return pageID;
    }

    public Image setPageID(int pageID) {
        this.pageID = pageID;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Image setUrl(String url) {
        this.url = url;
        return this;
    }
}
