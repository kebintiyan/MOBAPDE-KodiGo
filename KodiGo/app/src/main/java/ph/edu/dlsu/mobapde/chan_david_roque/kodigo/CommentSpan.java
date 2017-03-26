package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.support.annotation.NonNull;
import android.text.style.ClickableSpan;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by kevin on 3/26/2017.
 */

public class CommentSpan extends ClickableSpan {

    private Comment comment;
    private boolean isViewing;

    public CommentSpan(Comment comment, boolean isViewing) {
        super();
        this.comment = comment;
        this.isViewing = isViewing;
    }

    @Override
    public void onClick(View widget) {
        if (isViewing) {
            new MaterialDialog.Builder(widget.getContext())
                    .title("Comment")
                    .content(comment.getComment())
                    .show();
        }
        else {
            new MaterialDialog.Builder(widget.getContext())
                    .title("Comment")
                    .input("Comment", comment.getComment(), false, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            dialog.setContent(input);
                        }
                    })
                    .show();
        }
    }

    public Comment getComment() {
        return this.comment;
    }

    public CommentSpan setComment(Comment comment) {
        this.comment = comment;
        return this;
    }
}
