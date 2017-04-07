package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.R.id.notebookColor;

/**
 * Created by kevin on 2/26/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    final static String SCHEMA = "kodigo";

    public DatabaseHelper(Context context) {
        super(context, SCHEMA, null, 1);
        this.turnOnForeignKeySupport();
    }



    /*********** CREATE METHODS ***********/

    public long insertNotebook(Notebook n) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Notebook.COLUMN_TITLE, n.getTitle());
        cv.put(Notebook.COLUMN_TITLE_COLOR, n.getTitleColor());
        cv.put(Notebook.COLUMN_NOTEBOOK_COLOR, n.getNotebookColor());
        //cv.put(Notebook.COLUMN_NOTEBOOK_NUMBER, 0);
        cv.put(Notebook.COLUMN_DATE_CREATED, getCurrentDate());

        long result = db.insert(Notebook.TABLE_NAME, null, cv);
        db.close();

        return result;
    }

    public long insertPage(Page p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Page.COLUMN_NOTEBOOK_ID, p.getNotebookID());
        cv.put(Page.COLUMN_NAME, p.getName());
        cv.put(Page.COLUMN_TEXT, p.getText());
        //cv.put(Page.COLUMN_PAGE_NUMBER, 0);
        cv.put(Page.COLUMN_DATE_CREATED, getCurrentDate());

        for (Comment c : p.getComments()) {
            insertComment(c);
        }

        for (Image i : p.getImages()) {
            insertImage(i);
        }

        long result = db.insert(Page.TABLE_NAME, null, cv);
        db.close();

        return result;
    }

    public long insertComment(Comment c) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Comment.COLUMN_PAGE_ID, c.getPageID());
        cv.put(Comment.COLUMN_COMMENT, c.getComment());

        long result = db.insert(Comment.TABLE_NAME, null, cv);
        db.close();

        return result;
    }

    public long insertImage(Image i) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Image.COLUMN_PAGE_ID, i.getPageID());
        cv.put(Image.COLUMN_URL, i.getUrl());

        long result = db.insert(Image.TABLE_NAME, null, cv);
        db.close();

        return result;
    }

    /*********** READ METHODS ***********/

    public ArrayList<Notebook> queryAllNotebooks() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Select * From " +Notebook.TABLE_NAME + ";";
        Cursor c = db.rawQuery(sql,null);

        ArrayList<Notebook> notebooks = new ArrayList<>();

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
        db.close();

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

        db.close();
        c.close();

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
        db.close();
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

        while(c.moveToNext()) {
            Comment comment = new Comment()
                    .setCommentID(c.getInt(c.getColumnIndex(Comment.COLUMN_COMMENT_ID)))
                    .setPageID(c.getInt(c.getColumnIndex(Comment.COLUMN_PAGE_ID)))
                    .setComment(c.getString(c.getColumnIndex(Comment.COLUMN_COMMENT)));

            comments.add(comment);
        }

        db.close();
        c.close();

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

        if(c.moveToFirst()) {
            comment = new Comment()
                    .setCommentID(c.getInt(c.getColumnIndex(Comment.COLUMN_COMMENT_ID)))
                    .setPageID(c.getInt(c.getColumnIndex(Comment.COLUMN_PAGE_ID)))
                    .setComment(c.getString(c.getColumnIndex(Comment.COLUMN_COMMENT)));
        }

        db.close();
        c.close();

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

        while(c.moveToNext()) {
            Image image = new Image()
                    .setImageID(c.getInt(c.getColumnIndex(Image.COLUMN_IMAGE_ID)))
                    .setPageID(c.getInt(c.getColumnIndex(Image.COLUMN_PAGE_ID)))
                    .setUrl(c.getString(c.getColumnIndex(Image.COLUMN_IMAGE_ID)));

            images.add(image);
        }

        db.close();
        c.close();

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

        if(c.moveToFirst()) {
            image = new Image()
                    .setImageID(c.getInt(c.getColumnIndex(Image.COLUMN_IMAGE_ID)))
                    .setPageID(c.getInt(c.getColumnIndex(Image.COLUMN_PAGE_ID)))
                    .setUrl(c.getString(c.getColumnIndex(Image.COLUMN_URL)));

        }

        db.close();
        c.close();

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

        long result = db.update(Notebook.TABLE_NAME, cv, Notebook.COLUMN_NOTEBOOK_ID + " = ? ",
                new String[]{Long.toString(n.getNotebookID())});

        db.close();

        return result;
    }

    public long updatePage(Page p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Page.COLUMN_NOTEBOOK_ID, p.getNotebookID());
        cv.put(Page.COLUMN_NAME, p.getName());
        cv.put(Page.COLUMN_TEXT, p.getText());
        cv.put(Page.COLUMN_PAGE_NUMBER, p.getPageNumber());
        cv.put(Page.COLUMN_DATE_CREATED, getCurrentDate());

        long result =  db.update(Page.TABLE_NAME, cv, Page.COLUMN_PAGE_ID + " = ? ",
                new String[]{Long.toString(p.getPageID())});
        db.close();

        return result;
    }

    public long updateComment(Comment c) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Comment.COLUMN_PAGE_ID, c.getPageID());
        cv.put(Comment.COLUMN_COMMENT, c.getComment());

        long result =  db.update(Comment.TABLE_NAME, cv, Comment.COLUMN_COMMENT_ID + " = ? ",
                new String[]{Integer.toString(c.getCommentID())});

        db.close();

        return result;
    }

    public long updateImage(Image i) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Image.COLUMN_PAGE_ID, i.getPageID());
        cv.put(Image.COLUMN_URL, i.getUrl());

        long result =  db.update(Image.TABLE_NAME, cv, Image.COLUMN_IMAGE_ID + " = ? ",
                new String[]{Integer.toString(i.getImageID())});

        db.close();

        return result;
    }

    /*********** DELETE METHODS ***********/

    public long deleteNotebook(long id) {
        SQLiteDatabase db = getWritableDatabase();

        long result =  db.delete(Notebook.TABLE_NAME, Notebook.COLUMN_NOTEBOOK_ID + " = ? ",
                new String[]{Long.toString(id)});

        db.close();

        return result;
    }

    public long deletePage(long id) {
        SQLiteDatabase db = getWritableDatabase();

        long result = db.delete(Page.TABLE_NAME, Page.COLUMN_PAGE_ID + " = ? ",
                new String[]{Long.toString(id)});

        db.close();

        return result;
    }

    public long deleteComment(long id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(Comment.TABLE_NAME, Comment.COLUMN_COMMENT_ID+ " = ? ",
                new String[]{Long.toString(id)});
    }

    public long deleteImage(long id) {
        SQLiteDatabase db = getWritableDatabase();

        long result = db.delete(Comment.TABLE_NAME, Image.COLUMN_IMAGE_ID + " = ? ",
                new String[]{Long.toString(id)});

        db.close();

        return result;
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

        db.execSQL(getCreateOnCreateNotebookTrigger());
        db.execSQL(getCreateOnCreatePageTrigger());

        insertDefaultNotebook(db);
    }

    private String getCreateNotebookTableSQL() {
        String sql = "CREATE TABLE " + Notebook.TABLE_NAME + "(" +
                Notebook.COLUMN_NOTEBOOK_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Notebook.COLUMN_TITLE           + " TEXT NOT NULL, " +
                Notebook.COLUMN_TITLE_COLOR     + " TEXT NOT NULL, " +
                Notebook.COLUMN_NOTEBOOK_COLOR  + " TEXT NOT NULL, " +
                Notebook.COLUMN_NOTEBOOK_NUMBER + " INTEGER NOT NULL DEFAULT 0, " +
                Notebook.COLUMN_DATE_CREATED    + " TEXT NOT NULL); ";
        return sql;
    }

    private String getCreatePageTableSQL() {
        String sql = "CREATE TABLE " + Page.TABLE_NAME + "(" +
                Page.COLUMN_PAGE_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Page.COLUMN_NOTEBOOK_ID     + " INTEGER NOT NULL, " +
                Page.COLUMN_NAME            + " TEXT NOT NULL, " +
                Page.COLUMN_TEXT            + " TEXT NOT NULL, " +
                Page.COLUMN_PAGE_NUMBER     + " INTEGER NOT NULL DEFAULT 0, " +
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

    private String getCreateOnCreateNotebookTrigger() {
        String sql="CREATE TRIGGER " + Notebook.TRIGGER_ON_CREATE +
                " AFTER INSERT ON " + Notebook.TABLE_NAME +
                " BEGIN\n" +
                " UPDATE " + Notebook.TABLE_NAME + " SET " +
                    Notebook.COLUMN_NOTEBOOK_NUMBER + " = NEW." + Notebook.COLUMN_NOTEBOOK_ID +
                    "\n" +
                " WHERE " + Notebook.COLUMN_NOTEBOOK_ID + " = NEW." +
                    Notebook.COLUMN_NOTEBOOK_ID + ";\n" +
                " END;";

        return sql;
    }

    private String getCreateOnCreatePageTrigger() {
        String sql="CREATE TRIGGER " + Page.TRIGGER_ON_CREATE +
                " AFTER INSERT ON " + Page.TABLE_NAME +
                " BEGIN\n" +
                " UPDATE " + Page.TABLE_NAME + " SET " +
                    Page.COLUMN_PAGE_NUMBER + " = NEW." + Page.COLUMN_PAGE_ID +
                    "\n" +
                " WHERE " + Page.COLUMN_PAGE_ID + " = NEW." +
                Page.COLUMN_PAGE_ID + ";\n" +
                " END;";

        return sql;
    }

    public long insertDefaultNotebook(SQLiteDatabase db) {


        Notebook n = new Notebook()
                .setNotebookColor(Color.parseColor("#e8ead7"))
                .setTitleColor(Color.parseColor("#2d2d2d"))
                .setTitle("Default");


        ContentValues cv = new ContentValues();
        cv.put(Notebook.COLUMN_TITLE, n.getTitle());
        cv.put(Notebook.COLUMN_TITLE_COLOR, n.getTitleColor());
        cv.put(Notebook.COLUMN_NOTEBOOK_COLOR, n.getNotebookColor());
        //cv.put(Notebook.COLUMN_NOTEBOOK_NUMBER, 0);
        cv.put(Notebook.COLUMN_DATE_CREATED, getCurrentDate());

        long result = db.insert(Notebook.TABLE_NAME, null, cv);

        return result;
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
}
