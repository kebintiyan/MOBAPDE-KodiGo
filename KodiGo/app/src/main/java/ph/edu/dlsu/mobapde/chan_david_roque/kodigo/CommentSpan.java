package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by kevin on 3/26/2017.
 */

public class CommentSpan extends ClickableSpan {

    private Comment comment;
    private String coveredText;
    private boolean isViewing;
    private boolean deleted;

    public CommentSpan(Comment comment, String coveredText, boolean isViewing) {
        super();
        this.comment = comment;
        this.coveredText = coveredText;
        this.isViewing = isViewing;
        this.deleted = false;
    }

    @Override
    public void onClick(final View widget) {
        if (isViewing) {
            new MaterialDialog.Builder(widget.getContext())
                    .title("Comment")
                    .content(comment.getComment())
                    .show();
        }
        else {
            if (!deleted) {
                new MaterialDialog.Builder(widget.getContext())
                        .title("Comment")
                        .positiveText("Save")
                        .negativeText("Delete")
                        .negativeColorRes(R.color.colorSecondaryText)
                        .input("Comment", comment.getComment(), false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                dialog.setContent(input);
                                comment.setComment(input + "");
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new MaterialDialog.Builder(widget.getContext())
                                        .title("Delete?")
                                        .content("Are you sure you want to delete this comment?" +
                                        "\n Note: Changes will be saved on submit.")
                                        .positiveText("Delete")
                                        .positiveColorRes(R.color.colorMaterialRed)
                                        .negativeText("Cancel")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                deleted = true;

                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
            }
            else {
                new MaterialDialog.Builder(widget.getContext())
                        .title("Deleted Comment")
                        .titleColorRes(R.color.colorMaterialRed)
                        .positiveText("Restore")
                        .content(comment.getComment())
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleted = false;
                                CommentSpan.this.onClick(widget);
                            }
                        })
                        .show();
            }
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
