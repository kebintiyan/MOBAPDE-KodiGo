package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by kevin on 2/27/2017.
 */

public class HTMLTextView extends TextView {

    public HTMLTextView(Context context) {
        super(context);
    }

    public HTMLTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HTMLTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HTMLTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(getHTMLText(text + ""), type);
    }

    private Spanned getHTMLText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, new HTMLImageHandler(), new HTMLTagHandler());
        }
        else {
            return Html.fromHtml(text, new HTMLImageHandler(), new HTMLTagHandler());
        }
    }
}
