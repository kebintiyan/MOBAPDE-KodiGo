package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_DELETED;

public class ViewPageActivity extends AppCompatActivity {

    TextView toolbarTitle;
    HTMLEditText editPageText;
    HTMLTextView viewPageText;

    boolean isEditable;
    FloatingActionButton toggleEditButton;
    HorizontalScrollView toolbar;
    MenuItem saveItem;
    MenuItem deletePage;
    Page page;
    DatabaseHelper dbHelper;

    // Icons
    ImageView iconBold;
    ImageView iconItalic;
    ImageView iconUnderline;
    ImageView iconComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbHelper    = new DatabaseHelper(getBaseContext());
        isEditable  = (boolean) getIntent().getExtras().get(KEY_EDITABLE);
        page        = dbHelper.queryPageByID((long) getIntent().getExtras().get(KEY_PAGE_ID));

        initViews();
        initActionBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu_bar, menu);
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(false);
        deletePage = menu.findItem(R.id.action_delete);
        toggleEdit(isEditable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            case R.id.action_delete:
                clearFocus();

                new MaterialDialog.Builder(this)
                        .title("Delete?")
                        .content("Are you sure you want to delete this page?")
                        .positiveText("Ok")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dbHelper.deletePage(page.getPageID());
                                finish();
                            }
                        })
                        .show();
                return true;
            case android.R.id.home:
                clearFocus();
                if(isEditable) {
                    if (!page.getText().equals(editPageText.getTextAsString(true)))
                        showOnCancelConfirmDialog();
                    else
                        finish();
                }
                else
                    finish();
                return true;
            default:
                Log.i("CLICK", "HERE");
                return super.onOptionsItemSelected(item);
        }
    }

    public void toggleEdit(boolean isEditable){

        int textView, editText;
        this.isEditable = isEditable;

        clearFocus();
        if(isEditable){
            textView = View.GONE;
            editText = View.VISIBLE;
            saveItem.setVisible(true);
            deletePage.setVisible(false);



        }else {
            textView = View.VISIBLE;
            editText = View.GONE;
            saveItem.setVisible(false);
            deletePage.setVisible(true);
        }

        viewPageText.setVisibility(textView);
        toggleEditButton.setVisibility(textView);

        editPageText.setVisibility(editText);
        toolbar.setVisibility(editText);

        if (isEditable) {
            editPageText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editPageText.setSelection(editPageText.getText().toString().length());
        }

    }

    @Override
    public void onBackPressed() {
        clearFocus();
        if (isEditable) {

            showOnCancelConfirmDialog();
        }
        else {
            super.onBackPressed();
        }
    }

    public void showOnCancelConfirmDialog() {
        clearFocus();
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Discard?")
                .content("Are you sure you want to discard your changes?")
                .positiveText("Ok")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

    public void clearFocus() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public String getRandomHint() {
        Random rand = new Random();

        switch(rand.nextInt(4)) {
            case 0:
                return "What's on your mind?";
            case 1:
                return "Start being productive here...";
            case 2:
                return "Start writing here...";
            case 3:
                return "Start your notes here...";
            default:
                return "How about something creative...";
        }
    }

    private void initViews() {
        editPageText        = (HTMLEditText)            findViewById(R.id.editPageText);
        viewPageText        = (HTMLTextView)            findViewById(R.id.viewPageText);
        toolbar             = (HorizontalScrollView)    findViewById(R.id.my_toolbar);
        toggleEditButton    = (FloatingActionButton)    findViewById(R.id.toggleEditButton);
        toolbarTitle        = (TextView)                findViewById(R.id.toolbarTitle);


        /*Spanned pageText;
        String originalText = page.getText();
        originalText = originalText.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pageText = Html.fromHtml(originalText, Html.FROM_HTML_MODE_LEGACY, new HTMLImageHandler(),
                    new HTMLTagHandler(getBaseContext(), HTMLTagHandler.MODE_EDIT));
        }
        else {
            pageText = Html.fromHtml(originalText, new HTMLImageHandler(), new HTMLTagHandler(getBaseContext(),
                    HTMLTagHandler.MODE_EDIT));
        }*/

        editPageText.setText(page.getText(), true);
        editPageText.setHint(getRandomHint());


        viewPageText.setText(page.getText());

        toggleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEdit(true);
            }
        });

        initToolbarIcons();

    }

    private void initActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbarTitle.setText(page.getName());
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTitleDialog();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void showEditTitleDialog() {
        new MaterialDialog.Builder(this)
                .title("Edit Title")
                .inputRange(1, 24)
                .input("Page title", page.getName(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!page.getName().equals(input + "")) {
                            page.setName(input + "");
                            dbHelper.updatePage(page);
                            toolbarTitle.setText(page.getName());
                        }
                    }
                })
                .show();
    }

    private void initToolbarIcons() {
        // Icons
        iconBold = (ImageView) findViewById(R.id.icon_bold);
        iconBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = editPageText.getSelectionStart();
                int selectionEnd = editPageText.getSelectionEnd();

                Spannable ss = editPageText.getText();
                StyleSpan [] spans = ss.getSpans(selectionStart,
                        selectionEnd, StyleSpan.class);


                boolean hasSpan = false;

                for(int i = 0; i < spans.length; i++){
                    if (spans[i].getStyle() == Typeface.BOLD) {
                        hasSpan = true;
                    }
                    //ss.removeSpan(spans[i]);
                }

                Log.i("HAS SPAN", hasSpan + "");

                if (!hasSpan) {
                    Spannable spanText = Spannable.Factory.getInstance().newSpannable(editPageText.getText());
                    if (selectionStart != selectionEnd) {
                        spanText.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        spanText.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    editPageText.setText(spanText);
                }
                else {
                    StyleSpanRemover spanRemover = new StyleSpanRemover();
                    spanRemover.RemoveStyle(ss ,selectionStart, selectionEnd, Typeface.BOLD);
                    editPageText.setText(ss);
                }

                editPageText.setSelection(selectionStart, selectionEnd);
            }
        });

        iconItalic = (ImageView) findViewById(R.id.icon_italic);
        iconItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = editPageText.getSelectionStart();
                int selectionEnd = editPageText.getSelectionEnd();

                Spannable ss = editPageText.getText();
                StyleSpan [] spans = ss.getSpans(selectionStart,
                        selectionEnd, StyleSpan.class);


                boolean hasSpan = false;

                for(int i = 0; i < spans.length; i++){
                    if (spans[i].getStyle() == Typeface.ITALIC) {
                        hasSpan = true;
                    }
                    //ss.removeSpan(spans[i]);
                }

                if (!hasSpan) {
                    Spannable spanText = Spannable.Factory.getInstance().newSpannable(editPageText.getText());
                    spanText.setSpan(new StyleSpan(Typeface.ITALIC), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editPageText.setText(spanText);


                }
                else {
                    StyleSpanRemover spanRemover = new StyleSpanRemover();
                    spanRemover.RemoveStyle(ss ,selectionStart, selectionEnd, Typeface.ITALIC);
                    editPageText.setText(ss);
                }

                editPageText.setSelection(selectionStart, selectionEnd);
            }
        });

        iconUnderline = (ImageView) findViewById(R.id.icon_underline);
        iconUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = editPageText.getSelectionStart();
                int selectionEnd = editPageText.getSelectionEnd();

                Spannable ss = editPageText.getText();
                UnderlineSpan [] spans = ss.getSpans(selectionStart,
                        selectionEnd, UnderlineSpan.class);


                boolean hasSpan = spans.length > 0;


                if (!hasSpan) {
                    Spannable spanText = Spannable.Factory.getInstance().newSpannable(editPageText.getText());
                    spanText.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editPageText.setText(spanText);


                }
                else {
                    StyleSpanRemover spanRemover = new StyleSpanRemover();
                    spanRemover.RemoveOne(ss ,selectionStart, selectionEnd, UnderlineSpan.class);
                    editPageText.setText(ss);
                }

                editPageText.setSelection(selectionStart, selectionEnd);
            }
        });

        iconComment = (ImageView) findViewById(R.id.icon_comment);
        iconComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = editPageText.getSelectionStart();
                int selectionEnd = editPageText.getSelectionEnd();

                if (selectionStart == selectionEnd) {
                    selectionEnd = editPageText.getText().toString().length();
                }

                Spannable ss = editPageText.getText();
                CommentSpan [] spans = ss.getSpans(selectionStart,
                        selectionEnd, CommentSpan.class);

                boolean hasSpan = spans.length > 0;

                if (hasSpan) {
                    spans[0].onClick(v);
                }
                else if (selectionStart != editPageText.getSelectionEnd()) {

                    new MaterialDialog.Builder(v.getContext())
                            .title("Add Comment")
                            .positiveText("Save")
                            .negativeText("Cancel")
                            .input("Comment", "", false, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    createComment(input + "");
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void createComment(String text) {
        Comment comment = new Comment();
        comment.setComment(text);
        comment.setPageID(page.getPageID());

        int selectionStart = editPageText.getSelectionStart();
        int selectionEnd = editPageText.getSelectionEnd();

        CommentSpan commentSpan = new CommentSpan(comment, false);

        Spannable spanText = Spannable.Factory.getInstance().newSpannable(editPageText.getText());
        spanText.setSpan(commentSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editPageText.setText(spanText);

        editPageText.setSelection(selectionStart, selectionEnd);
    }

    private void save() {
        saveComments();
        page.setText(editPageText.getTextAsString(true));
        dbHelper.updatePage(page);
        viewPageText.setText(page.getText());
        editPageText.setText(page.getText(), true);
        toggleEdit(false);
    }

    private void saveComments() {
        CharSequence charSequence = editPageText.getText();
        if (charSequence instanceof Spannable) {
            Spannable spannableText = (Spannable)charSequence;
            CommentSpan[] spans = spannableText.getSpans(0, editPageText.length(), CommentSpan.class);
            for (CommentSpan span : spans) {

                // If comment span is deleted, delete from db
                if (span.isDeleted() && span.getComment().getCommentID() != 0) {
                    dbHelper.deleteComment(span.getComment().getCommentID());
                }
                else {
                    long id = span.getComment().getCommentID();
                    int start = spannableText.getSpanStart(span);
                    int end = spannableText.getSpanEnd(span);

                    CharSequence before = spannableText.subSequence(0, start);
                    CharSequence middle = spannableText.subSequence(start, end);
                    CharSequence after = spannableText.subSequence(end, spannableText.length());

                    if (id != 0) {
                        // If has id, update comment in db
                        dbHelper.updateComment(span.getComment());
                    }
                    else {
                        // If no id, create comment in db
                        // Set id
                        id = dbHelper.insertComment(span.getComment());
                    }

                    // Append tags
                    CharSequence newText = TextUtils.concat(before, "<comment_" + id + ">", middle,
                            "</comment_" + id + ">", after);

                    editPageText.setText(newText);
                }
            }
        }
    }
}
