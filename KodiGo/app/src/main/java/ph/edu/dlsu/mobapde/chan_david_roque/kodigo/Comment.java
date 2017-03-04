package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

/**
 * Created by USER on 2/26/2017.
 */

public class Comment {

    public static final String TABLE_NAME           = " comment ";
    public static final String COLUMN_COMMENT_ID    = " commentID ";
    public static final String COLUMN_PAGE_ID       = " pageID ";
    public static final String COLUMN_COMMENT       = " comment ";

    private int commentID;
    private int pageID;
    private String comment;

    public Comment() {

    }
    public int getCommentID() {
        return commentID;
    }

    public Comment setCommentID(int commentID) {
        this.commentID = commentID;
        return this;
    }

    public int getPageID() {
        return pageID;
    }

    public Comment setPageID(int pageID) {
        this.pageID = pageID;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Comment setComment(String comment) {
        this.comment = comment;
        return this;
    }
}
