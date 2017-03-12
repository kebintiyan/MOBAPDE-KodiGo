package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by kevin on 2/27/2017.
 */

public class HTMLTagHandler implements Html.TagHandler {

    DatabaseHelper db;

    public HTMLTagHandler(Context context) {
        db = new DatabaseHelper(context);
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        HashMap<String, String> attributes = getAttributes(xmlReader);

        if ("comment".equalsIgnoreCase(tag)) {
            processComment(opening, output, attributes);
        }
        else if ("image".equalsIgnoreCase(tag)) {
            processImage(opening, output, attributes);
        }
    }

    private void processComment(boolean opening, Editable output, HashMap<String, String> attributes) {
        int len = output.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }

            /*@Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }*/
        };

        if(opening) {
            output.setSpan(clickableSpan, len, len, Spannable.SPAN_MARK_MARK);
        }
        else {
            Object obj = getLast(output, ClickableSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(clickableSpan, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void processImage(boolean opening, Editable output, HashMap<String, String> attributes) {
        Image image = db.queryImageByID(Integer.parseInt(attributes.get("id")));



        /*int len = output.length();
        if(opening) {
            output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
        }
        else {
            Object obj = getLast(output, StrikethroughSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(new StrikethroughSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }*/
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

    private HashMap<String, String> getAttributes(final XMLReader xmlReader) {
        HashMap<String, String> attributes = new HashMap<>();

        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[])dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer)lengthField.get(atts);

            for(int i = 0; i < len; i++)
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
        }
        catch (Exception e) {
            Log.d(TAG, "Exception: " + e);
        }

        return attributes;
    }
}
