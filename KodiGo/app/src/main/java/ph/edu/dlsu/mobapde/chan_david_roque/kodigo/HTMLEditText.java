package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
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
        if (!renderAsHTML) {
            super.setText(text);
        }
        else {
            super.setText(getHTMLText(text + ""));
        }
    }

    public String getTextAsString(boolean asHTML) {
        if (!asHTML)
            return super.getText().toString();
        else {
            String text;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text = Html.toHtml(super.getText(), Html.FROM_HTML_MODE_LEGACY);
            }
            else {
                text = Html.toHtml(super.getText());
            }
            return text;
        }
    }

    private Spanned getHTMLText(String text) {
        // You might need to remove this:
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
        Log.i("textch", text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, new HTMLImageHandler(getContext()), new HTMLTagHandler(getContext(), HTMLTagHandler.MODE_EDIT));
        }
        else {
            return Html.fromHtml(text, new HTMLImageHandler(getContext()), new HTMLTagHandler(getContext(), HTMLTagHandler.MODE_EDIT));
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
//        super.onSelectionChanged(selStart, selEnd);

        if (onSelectionChangedHandler != null) {
            onSelectionChangedHandler.onSelectionChanged(selStart, selEnd);
        }
    }

    private OnSelectionChangedHandler onSelectionChangedHandler;

    public void setOnSelectionChangedHandler(OnSelectionChangedHandler onSelectionChangedHandler) {
        this.onSelectionChangedHandler = onSelectionChangedHandler;
    }

    public interface OnSelectionChangedHandler {
        void onSelectionChanged(int selectionStart, int selectionEnd);
    }
}
