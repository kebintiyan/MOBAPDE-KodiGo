package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by kevin on 2/27/2017.
 */

public class HTMLTextView extends android.support.v7.widget.AppCompatTextView {

    public HTMLTextView(Context context) {
        super(context);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public HTMLTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public HTMLTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(getHTMLText(text + "", HTMLTagHandler.MODE_VIEW), type);
    }

    private Spanned getHTMLText(String text, int mode) {
        // You might need to remove this:
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");

        Log.i("GET HTML TEXT", mode + "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, new HTMLImageHandler(), new HTMLTagHandler(getContext(), mode));
        }
        else {
            return Html.fromHtml(text, new HTMLImageHandler(), new HTMLTagHandler(getContext(), mode));
        }
    }

    public void setListViewText(CharSequence text, BufferType type) {
        super.setText(getHTMLText(text + "", HTMLTagHandler.MODE_VIEW_LIST), type);
    }
}
