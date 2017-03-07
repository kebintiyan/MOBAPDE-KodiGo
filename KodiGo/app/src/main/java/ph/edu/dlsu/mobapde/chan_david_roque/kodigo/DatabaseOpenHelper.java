package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kevin on 2/26/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    final static String SCHEMA = "kodigo";

    public DatabaseOpenHelper(Context context) {
        super(context, SCHEMA, null, 1);
        this.turnOnForeignKeySupport();
    }



    /*********** CREATE METHODS ***********/

    public void insertNotebook(Notebook n) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Notebook.COLUMN_TITLE, n.getTitle());
        cv.put(Notebook.COLUMN_TITLE_COLOR, n.getTitleColor());
        cv.put(Notebook.COLUMN_NOTEBOOK_COLOR, n.getNotebookColor());
        cv.put(Notebook.COLUMN_NOTEBOOK_NUMBER, getNextNotebookNumber());
        cv.put(Notebook.COLUMN_DATE_CREATED, getCurrentDate());

        db.insert(Notebook.TABLE_NAME, null, cv);
        db.close();
    }

    public long insertPage(Page p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Page.COLUMN_NOTEBOOK_ID, p.getNotebookID());
        cv.put(Page.COLUMN_NAME, p.getName());
        cv.put(Page.COLUMN_TEXT, p.getText());
        cv.put(Page.COLUMN_PAGE_NUMBER, getNextPageNumber(p.getNotebookID()));
        cv.put(Page.COLUMN_DATE_CREATED, getCurrentDate());

        for (Comment c : p.getComments()) {
            insertComment(c);
        }

        for (Image i : p.getImages()) {
            insertImage(i);
        }

        return db.insert(Page.TABLE_NAME, null, cv);
    }

    public long insertComment(Comment c) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Comment.COLUMN_PAGE_ID, c.getPageID());
        cv.put(Comment.COLUMN_COMMENT, c.getComment());

        return db.insert(Comment.TABLE_NAME, null, cv);
    }

    public long insertImage(Image i) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Image.COLUMN_PAGE_ID, i.getPageID());
        cv.put(Image.COLUMN_URL, i.getUrl());

        return db.insert(Comment.TABLE_NAME, null, cv);
    }

    /*********** READ METHODS ***********/

    public ArrayList<Notebook> queryAllNotebooks() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Select * From " +Notebook.TABLE_NAME + ";";
        Cursor c = db.rawQuery(sql,null);

        ArrayList<Notebook> notebooks = new ArrayList<>();

        if(c.getCount()>0) {
            try {
                while (c.moveToNext()) {
                    Notebook n = new Notebook()
                            .setNotebookID(c.getInt(c.getColumnIndex(Notebook.COLUMN_NOTEBOOK_ID)))
                            .setTitle(c.getString(c.getColumnIndex(Notebook.COLUMN_TITLE)))
                            .setTitleColor(c.getInt(c.getColumnIndex(Notebook.COLUMN_TITLE_COLOR)))
                            .setNotebookColor(c.getInt(c.getColumnIndex(Notebook.COLUMN_NOTEBOOK_COLOR)))
                            .setNotebookNumber(c.getInt(c.getColumnIndex(Notebook.COLUMN_NOTEBOOK_NUMBER)))
                            .setDateCreated(c.getString(c.getColumnIndex(Notebook.COLUMN_DATE_CREATED)));

                    notebooks.add(n);
                }
            } finally {

            }
        }
        c.close();
        db.close();
        return notebooks;
    }

    public Cursor queryAllNotebooksAsCursor() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(
                Notebook.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Notebook.COLUMN_NOTEBOOK_NUMBER);

        return c;
    }


    public Notebook queryNotebookByID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Notebook.TABLE_NAME,
                null,
                Notebook.COLUMN_NOTEBOOK_ID + " = ? ",
                selectionArgs,
                null,
                null,
                Notebook.COLUMN_NOTEBOOK_NUMBER);

        Notebook n = null;

        if (c.moveToFirst()) {
            n = new Notebook()
                    .setNotebookID(c.getInt(c.getColumnIndex(Notebook.COLUMN_NOTEBOOK_ID)))
                    .setTitle(c.getString(c.getColumnIndex(Notebook.COLUMN_TITLE)))
                    .setTitleColor(c.getInt(c.getColumnIndex(Notebook.COLUMN_TITLE_COLOR)))
                    .setNotebookColor(c.getInt(c.getColumnIndex(Notebook.COLUMN_NOTEBOOK_COLOR)))
                    .setNotebookNumber(c.getInt(c.getColumnIndex(Notebook.COLUMN_NOTEBOOK_NUMBER)))
                    .setDateCreated(c.getString(c.getColumnIndex(Notebook.COLUMN_DATE_CREATED)));
        }
        c.close();

        return n;
    }

    public ArrayList<Page> queryPagesByNotebookID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Page.TABLE_NAME,
                null,
                Page.COLUMN_NOTEBOOK_ID + " = ? ",
                selectionArgs,
                null,
                null,
                Page.COLUMN_PAGE_NUMBER);

        ArrayList<Page> pages = new ArrayList<>();

        try {
            while (c.moveToNext()) {
                Page p = new Page()
                        .setPageID(c.getInt(c.getColumnIndex(Page.COLUMN_PAGE_ID)))
                        .setNotebookID(c.getInt(c.getColumnIndex(Page.COLUMN_NOTEBOOK_ID)))
                        .setName(c.getString(c.getColumnIndex(Page.COLUMN_NAME)))
                        .setText(c.getString(c.getColumnIndex(Page.COLUMN_TEXT)))
                        .setImages(queryImagesByPageID(id))
                        .setComments(queryCommentsByPageID(id))
                        .setPageNumber(c.getInt(c.getColumnIndex(Page.COLUMN_PAGE_NUMBER)))
                        .setDateCreated(c.getString(c.getColumnIndex(Page.COLUMN_DATE_CREATED)));

                pages.add(p);
            }
        } finally {
            c.close();
        }

        return pages;
    }

    public Cursor queryPagesByNotebookIDAsCursor(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Page.TABLE_NAME,
                null,
                Page.COLUMN_NOTEBOOK_ID + " = ? ",
                selectionArgs,
                null,
                null,
                Page.COLUMN_PAGE_NUMBER);

        return c;
    }

    public Page queryPageByID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Page.TABLE_NAME,
                null,
                Page.COLUMN_PAGE_ID + " = ? ",
                selectionArgs,
                null,
                null,
                Page.COLUMN_PAGE_NUMBER);

        Page p = null;

        if (c.moveToFirst()) {
            p = new Page()
                    .setPageID(c.getInt(c.getColumnIndex(Page.COLUMN_PAGE_ID)))
                    .setNotebookID(c.getInt(c.getColumnIndex(Page.COLUMN_NOTEBOOK_ID)))
                    .setName(c.getString(c.getColumnIndex(Page.COLUMN_NAME)))
                    .setText(c.getString(c.getColumnIndex(Page.COLUMN_TEXT)))
                    .setImages(queryImagesByPageID(id))
                    .setComments(queryCommentsByPageID(id))
                    .setPageNumber(c.getInt(c.getColumnIndex(Page.COLUMN_PAGE_NUMBER)))
                    .setDateCreated(c.getString(c.getColumnIndex(Page.COLUMN_DATE_CREATED)));
        }
        c.close();

        return p;
    }

    public ArrayList<Comment> queryCommentsByPageID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Comment.TABLE_NAME,
                null,
                Comment.COLUMN_PAGE_ID + " = ? ",
                selectionArgs,
                null,
                null,
                null);

        ArrayList<Comment> comments = new ArrayList<>();

        try {
            while(c.moveToNext()) {
                Comment comment = new Comment()
                        .setCommentID(c.getInt(c.getColumnIndex(Comment.COLUMN_COMMENT_ID)))
                        .setPageID(c.getInt(c.getColumnIndex(Comment.COLUMN_PAGE_ID)))
                        .setComment(c.getString(c.getColumnIndex(Comment.COLUMN_COMMENT)));

                comments.add(comment);
            }
        } finally {
            c.close();
        }

        return comments;
    }

    public Comment queryCommentByID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Comment.TABLE_NAME,
                null,
                Comment.COLUMN_COMMENT_ID + " = ? ",
                selectionArgs,
                null,
                null,
                null);

        Comment comment = null;

        try {
            if(c.moveToFirst()) {
                comment = new Comment()
                        .setCommentID(c.getInt(c.getColumnIndex(Comment.COLUMN_COMMENT_ID)))
                        .setPageID(c.getInt(c.getColumnIndex(Comment.COLUMN_PAGE_ID)))
                        .setComment(c.getString(c.getColumnIndex(Comment.COLUMN_COMMENT)));
            }
        } finally {
            c.close();
        }

        return comment;
    }

    public ArrayList<Image> queryImagesByPageID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Image.TABLE_NAME,
                null,
                Image.COLUMN_PAGE_ID + " = ? ",
                selectionArgs,
                null,
                null,
                null);

        ArrayList<Image> images = new ArrayList<>();

        try {
            while(c.moveToNext()) {
                Image image = new Image()
                        .setImageID(c.getInt(c.getColumnIndex(Image.COLUMN_IMAGE_ID)))
                        .setPageID(c.getInt(c.getColumnIndex(Image.COLUMN_PAGE_ID)))
                        .setUrl(c.getString(c.getColumnIndex(Image.COLUMN_IMAGE_ID)));

                images.add(image);
            }
        } finally {
            c.close();
        }

        return images;
    }

    public Image queryImageByID(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] selectionArgs = {
                Long.toString(id)
        };

        Cursor c = db.query(
                Image.TABLE_NAME,
                null,
                Image.COLUMN_IMAGE_ID + " = ? ",
                selectionArgs,
                null,
                null,
                null);

        Image image = null;

        try {
            if(c.moveToFirst()) {
                image = new Image()
                        .setImageID(c.getInt(c.getColumnIndex(Image.COLUMN_IMAGE_ID)))
                        .setPageID(c.getInt(c.getColumnIndex(Image.COLUMN_PAGE_ID)))
                        .setUrl(c.getString(c.getColumnIndex(Image.COLUMN_IMAGE_ID)));

            }
        } finally {
            c.close();
        }

        return image;
    }

    /*********** UPDATE METHODS ***********/

    public long updateNotebook(Notebook n) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Notebook.COLUMN_TITLE, n.getTitle());
        cv.put(Notebook.COLUMN_TITLE_COLOR, n.getTitleColor());
        cv.put(Notebook.COLUMN_NOTEBOOK_COLOR, n.getNotebookColor());
        cv.put(Notebook.COLUMN_NOTEBOOK_NUMBER, n.getNotebookNumber());
        cv.put(Notebook.COLUMN_DATE_CREATED, getCurrentDate());

        return db.update(Notebook.TABLE_NAME, cv, Notebook.COLUMN_NOTEBOOK_ID + " = ? ",
                new String[]{Long.toString(n.getNotebookID())});
    }

    public long updatePage(Page p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Page.COLUMN_NOTEBOOK_ID, p.getNotebookID());
        cv.put(Page.COLUMN_NAME, p.getName());
        cv.put(Page.COLUMN_TEXT, p.getText());
        cv.put(Page.COLUMN_PAGE_NUMBER, getNextPageNumber(p.getNotebookID()));
        cv.put(Page.COLUMN_DATE_CREATED, getCurrentDate());

        /*for (Comment c : p.getComments()) {
            insertComment(c);
        }

        for (Image i : p.getImages()) {
            insertImage(i);
        }*/

        return db.update(Page.TABLE_NAME, cv, Page.COLUMN_PAGE_ID + " = ? ",
                new String[]{Long.toString(p.getPageID())});
    }

    public long updateComment(Comment c) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Comment.COLUMN_PAGE_ID, c.getPageID());
        cv.put(Comment.COLUMN_COMMENT, c.getComment());

        return db.update(Comment.TABLE_NAME, cv, Comment.COLUMN_COMMENT_ID + " = ? ",
                new String[]{Integer.toString(c.getCommentID())});
    }

    public long updateImage(Image i) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Image.COLUMN_PAGE_ID, i.getPageID());
        cv.put(Image.COLUMN_URL, i.getUrl());

        return db.update(Image.TABLE_NAME, cv, Image.COLUMN_IMAGE_ID + " = ? ",
                new String[]{Integer.toString(i.getImageID())});
    }

    /*********** DELETE METHODS ***********/

    public long deleteNotebook(long id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(Notebook.TABLE_NAME, Notebook.COLUMN_NOTEBOOK_ID + " = ? ",
                new String[]{Long.toString(id)});
    }

    public long deletePage(long id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(Page.TABLE_NAME, Page.COLUMN_PAGE_ID + " = ? ",
                new String[]{Long.toString(id)});
    }

    public long deleteComment(long id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(Comment.TABLE_NAME, Comment.COLUMN_COMMENT_ID+ " = ? ",
                new String[]{Long.toString(id)});
    }

    public long deleteImage(long id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(Comment.TABLE_NAME, Image.COLUMN_IMAGE_ID + " = ? ",
                new String[]{Long.toString(id)});
    }

    /*********** INITIALIZE DATABASE METHODS ***********/

    private void turnOnForeignKeySupport() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateNotebookTableSQL());
        db.execSQL(getCreatePageTableSQL());
        db.execSQL(getCreateCommentTableSQL());
        db.execSQL(getCreateImageTableSQL());

        Log.i("TEST", "DB Created");

    }

    private String getCreateNotebookTableSQL() {
        String sql = "CREATE TABLE " + Notebook.TABLE_NAME + "(" +
                Notebook.COLUMN_NOTEBOOK_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Notebook.COLUMN_TITLE           + " TEXT NOT NULL, " +
                Notebook.COLUMN_TITLE_COLOR     + " TEXT NOT NULL, " +
                Notebook.COLUMN_NOTEBOOK_COLOR  + " TEXT NOT NULL, " +
                Notebook.COLUMN_NOTEBOOK_NUMBER + " INTEGER NOT NULL, " +
                Notebook.COLUMN_DATE_CREATED    + " TEXT NOT NULL); ";
        return sql;
    }

    private String getCreatePageTableSQL() {
        String sql = "CREATE TABLE " + Page.TABLE_NAME + "(" +
                Page.COLUMN_PAGE_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Page.COLUMN_NOTEBOOK_ID     + " INTEGER NOT NULL, " +
                Page.COLUMN_NAME            + " TEXT NOT NULL, " +
                Page.COLUMN_TEXT            + " TEXT NOT NULL, " +
                Page.COLUMN_PAGE_NUMBER     + " INTEGER NOT NULL, " +
                Page.COLUMN_DATE_CREATED    + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + Page.COLUMN_NOTEBOOK_ID + ") REFERENCES " +
                    Notebook.TABLE_NAME + "(" + Notebook.COLUMN_NOTEBOOK_ID + ") ON DELETE CASCADE);";
        return sql;
    }

    private String getCreateCommentTableSQL() {
        String sql = "CREATE TABLE " + Comment.TABLE_NAME + "(" +
                Comment.COLUMN_COMMENT_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Comment.COLUMN_PAGE_ID      + " INTEGER NOT NULL, " +
                Comment.COLUMN_COMMENT      + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + Comment.COLUMN_PAGE_ID + ") REFERENCES " +
                    Page.TABLE_NAME + "(" + Page.COLUMN_PAGE_ID + ") ON DELETE CASCADE);";
        return sql;
    }

    private String getCreateImageTableSQL() {
        String sql = "CREATE TABLE " + Image.TABLE_NAME + "(" +
                Image.COLUMN_IMAGE_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Image.COLUMN_PAGE_ID    + " INTEGER NOT NULL, " +
                Image.COLUMN_URL        + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + Image.COLUMN_PAGE_ID + ") REFERENCES " +
                Page.TABLE_NAME + "(" + Page.COLUMN_PAGE_ID + ") ON DELETE CASCADE);";
        return sql;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*********** HELPER METHODS ***********/

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");

        return df.format(c.getTime());
    }

    private int getNextNotebookNumber() {

        return 0;
    }

    private int getNextPageNumber(long notebookID) {

        return 0;
    }

}
