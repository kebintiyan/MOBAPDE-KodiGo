package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by kevin on 2/27/2017.
 */

public class HTMLTagHandler implements Html.TagHandler {

    private final static String TAG_COMMENT = "comment";
    private final static String TAG_IMAGE = "image";
    public final static int MODE_VIEW = 0;
    public final static int MODE_EDIT = 1;
    public final static int MODE_VIEW_LIST = 2;

    DatabaseHelper dbHelper;
    int mode;

    public HTMLTagHandler(Context context, int mode) {
        dbHelper = new DatabaseHelper(context);
        this.mode = mode;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(tag.indexOf(TAG_COMMENT) == 0) {
            String id = tag.split("_")[1];
            processComment(opening, output, id);
        }
        else if (tag.indexOf(TAG_IMAGE) == 0) {
            String id = tag.substring(TAG_IMAGE.length() + 1);
            processImage(opening, output, id);
        }
    }

    private void processComment(boolean opening, Editable output, String commentID) {
        int len = output.length();

        Comment comment = dbHelper.queryCommentByID(Long.parseLong(commentID));
        CommentSpan commentSpan = new CommentSpan(comment, mode);

        if(opening) {
            output.setSpan(commentSpan, len, len, Spannable.SPAN_MARK_MARK);
        }
        else {
            Object obj = getLast(output, CommentSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(commentSpan, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void processImage(boolean opening, Editable output, String imageID) {
        Image image = dbHelper.queryImageByID(Integer.parseInt(imageID));
        Drawable d = Drawable.createFromPath(image.getUrl());

        int len = output.length();
        if(opening) {
            output.setSpan(new ImageSpan(d), len, len, Spannable.SPAN_MARK_MARK);
        }
        else {
            Object obj = getLast(output, ImageSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(new ImageSpan(d), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            for(int i = objs.length;i>0;i--) {
                if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i-1];
                }
            }
            return null;
        }
    }
}
