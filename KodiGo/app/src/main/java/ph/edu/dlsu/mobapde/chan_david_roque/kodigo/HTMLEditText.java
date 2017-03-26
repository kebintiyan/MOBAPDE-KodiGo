package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by kevin on 3/26/2017.
 */

public class HTMLEditText extends AppCompatEditText {

    public HTMLEditText(Context context) {
        super(context);
    }

    public HTMLEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HTMLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(CharSequence text, boolean renderAsHTML) {
        if (!renderAsHTML)
            super.setText(text);
        else {
            super.setText(getHTMLText(text + ""));
        }
    }

    private Spanned getHTMLText(String text) {
        text = text.replaceAll("\\n", "<br />");

        // You might need to remove this:
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, new HTMLImageHandler(), new HTMLTagHandler(getContext(), HTMLTagHandler.MODE_EDIT));
        }
        else {
            return Html.fromHtml(text, new HTMLImageHandler(), new HTMLTagHandler(getContext(), HTMLTagHandler.MODE_EDIT));
        }
    }
}
