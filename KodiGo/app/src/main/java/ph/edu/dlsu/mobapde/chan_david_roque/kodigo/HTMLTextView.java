package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by kevin on 2/27/2017.
 */

public class HTMLTextView extends AppCompatTextView {
    Context context;
    public HTMLTextView(Context context) {
        super(context);
        this.context=context;
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

        super.setText(getHTMLText(text + ""), type);
    }

    private Spanned getHTMLText(String text) {
        // You might need to remove this:
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
        text = text.replaceAll("\n;", "<br>");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, new HTMLImageHandler(context), new HTMLTagHandler(context, HTMLTagHandler.MODE_VIEW));
        }
        else {
            return Html.fromHtml(text, new HTMLImageHandler(context), new HTMLTagHandler(context, HTMLTagHandler.MODE_VIEW));
        }
    }
}
