package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by user on 4/8/2017.
 */

public class ClickableImageSpan extends ClickableSpan {
    String source;
    public ClickableImageSpan(String source){
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    public void onClick(View widget) {

    }
}
