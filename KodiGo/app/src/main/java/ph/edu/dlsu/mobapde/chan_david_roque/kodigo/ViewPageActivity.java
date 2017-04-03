package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;

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
    ToggleButton iconBold;
    ToggleButton iconItalic;
    ToggleButton iconUnderline;
    ToggleButton iconHighlight;
    ImageView iconCamera;
    ImageView iconGallery;
    ImageView iconComment;

    int styleStart;
    int cursorLoc;


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

        editPageText.setText(page.getText(), true);
        editPageText.setHint(getRandomHint());
        editPageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = editPageText.getSelectionStart();
                if (position < 0){
                    position = 0;
                }

                if (position > 0){

                    if (styleStart > position || position > (cursorLoc + 1)){
                        //user changed cursor location, reset
                        styleStart = position - 1;
                    }

                    cursorLoc = position;


                    StyleSpanRemover spanRemover = new StyleSpanRemover();
                    int selectionStart = editPageText.getSelectionStart();
                    int selectionEnd = editPageText.getSelectionEnd();
                    boolean hasSpan = false;

                    if (iconBold.isChecked()){
                        /*StyleSpan[] ss = s.getSpans(styleStart, position, StyleSpan.class);

                        for (int i = 0; i < ss.length; i++) {
                            if (ss[i].getStyle() == android.graphics.Typeface.BOLD) {

                                //s.removeSpan(ss[i]);
                                hasSpan = true;
                                break;
                            }
                        }

                        if (hasSpan) {
                            spanRemover.RemoveStyle(s, styleStart, position, Typeface.BOLD);
                        }*/

                        StyleSpan[] ss = s.getSpans(styleStart, position, StyleSpan.class);

                        for (int i = 0; i < ss.length; i++) {
                            if (ss[i].getStyle() == Typeface.BOLD){
                                s.removeSpan(ss[i]);
                            }
                        }

                        s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (iconItalic.isChecked()){
                        StyleSpan[] ss = s.getSpans(styleStart, position, StyleSpan.class);

                        for (int i = 0; i < ss.length; i++) {
                            if (ss[i].getStyle() == Typeface.ITALIC){
                                s.removeSpan(ss[i]);
                            }
                        }
                        s.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (iconUnderline.isChecked()){
                        UnderlineSpan[] ss = s.getSpans(styleStart, position, UnderlineSpan.class);

                        for (int i = 0; i < ss.length; i++) {
                            s.removeSpan(ss[i]);
                        }
                        s.setSpan(new UnderlineSpan(), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (iconHighlight.isChecked()){
                        BackgroundColorSpan[] ss = s.getSpans(styleStart, position, BackgroundColorSpan.class);

                        for (int i = 0; i < ss.length; i++) {
                            s.removeSpan(ss[i]);
                        }
                        s.setSpan(new BackgroundColorSpan(0xFFFFFF00), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        });

        editPageText.setOnSelectionChangedHandler(new HTMLEditText.OnSelectionChangedHandler() {
            @Override
            public void onSelectionChanged(int selectionStart, int selectionEnd) {

                if (selectionStart > selectionEnd) {
                    int temp = selectionEnd;
                    selectionEnd = selectionStart;
                    selectionStart = temp;
                }

                // Check if has span
                boolean hasBold = false;
                boolean hasItalic = false;

                // Check if has bold
                Spannable str = editPageText.getText();
                StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

                for (int i = 0; i < ss.length; i++) {
                    if (ss[i].getStyle() == Typeface.BOLD){
                        hasBold = true;
                    }
                    else if (ss[i].getStyle() == Typeface.ITALIC){
                        hasItalic = true;
                    }
                }

                iconBold.setChecked(hasBold);
                iconItalic.setChecked(hasItalic);

                UnderlineSpan[] uss = str.getSpans(selectionStart, selectionEnd, UnderlineSpan.class);
                iconUnderline.setChecked(uss.length > 0);

                BackgroundColorSpan[] bss = str.getSpans(selectionStart, selectionEnd, BackgroundColorSpan.class);
                iconHighlight.setChecked(bss.length > 0);
            }
        });


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
        iconBold = (ToggleButton) findViewById(R.id.icon_bold);
        iconBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectionStart = editPageText.getSelectionStart();

                styleStart = selectionStart;

                int selectionEnd = editPageText.getSelectionEnd();

                if (selectionStart > selectionEnd){
                    int temp = selectionEnd;
                    selectionEnd = selectionStart;
                    selectionStart = temp;
                }


                if (selectionEnd > selectionStart)
                {
                    Spannable str = editPageText.getText();
                    StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

                    boolean exists = false;
                    for (int i = 0; i < ss.length; i++) {
                        if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
                            exists = true;
                            break;
                        }
                    }

                    if (!exists){
                        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        StyleSpanRemover spanRemover = new StyleSpanRemover();
                        spanRemover.RemoveStyle(str ,selectionStart, selectionEnd, Typeface.BOLD);
                    }

                    iconBold.setChecked(!exists);
                }
            }
        });

        iconItalic = (ToggleButton) findViewById(R.id.icon_italic);
        iconItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectionStart = editPageText.getSelectionStart();

                styleStart = selectionStart;

                int selectionEnd = editPageText.getSelectionEnd();

                if (selectionStart > selectionEnd){
                    int temp = selectionEnd;
                    selectionEnd = selectionStart;
                    selectionStart = temp;
                }


                if (selectionEnd > selectionStart)
                {
                    Spannable str = editPageText.getText();
                    StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

                    boolean exists = false;
                    for (int i = 0; i < ss.length; i++) {
                        if (ss[i].getStyle() == android.graphics.Typeface.ITALIC){
                            exists = true;
                            break;
                        }
                    }

                    if (!exists){
                        str.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        StyleSpanRemover spanRemover = new StyleSpanRemover();
                        spanRemover.RemoveStyle(str, selectionStart, selectionEnd, Typeface.ITALIC);
                    }

                    iconItalic.setChecked(!exists);
                }

                /*int selectionStart = editPageText.getSelectionStart();
                int selectionEnd = editPageText.getSelectionEnd();

                Spannable ss = editPageText.getText();
                StyleSpan [] spans = ss.getSpans(selectionStart,
                        selectionEnd, StyleSpan.class);

                boolean hasSpan = false;

                for(int i = 0; i < spans.length; i++){
                    if (spans[i].getStyle() == Typeface.ITALIC) {
                        hasSpan = true;
                    }
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

                editPageText.setSelection(selectionStart, selectionEnd);*/
            }
        });

        iconUnderline = (ToggleButton) findViewById(R.id.icon_underline);
        iconUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectionStart = editPageText.getSelectionStart();

                styleStart = selectionStart;

                int selectionEnd = editPageText.getSelectionEnd();

                if (selectionStart > selectionEnd){
                    int temp = selectionEnd;
                    selectionEnd = selectionStart;
                    selectionStart = temp;
                }


                if (selectionEnd > selectionStart)
                {
                    Spannable str = editPageText.getText();
                    UnderlineSpan[] ss = str.getSpans(selectionStart, selectionEnd, UnderlineSpan.class);

                    boolean exists = ss.length > 0;

                    if (!exists){
                        str.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        StyleSpanRemover spanRemover = new StyleSpanRemover();
                        spanRemover.RemoveOne(str ,selectionStart, selectionEnd, UnderlineSpan.class);
                    }

                    iconUnderline.setChecked(!exists);
                }

                /*int selectionStart = editPageText.getSelectionStart();
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

                editPageText.setSelection(selectionStart, selectionEnd);*/
            }
        });

        iconHighlight = (ToggleButton) findViewById(R.id.icon_highlight);
        iconHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = editPageText.getSelectionStart();

                styleStart = selectionStart;

                int selectionEnd = editPageText.getSelectionEnd();

                if (selectionStart > selectionEnd){
                    int temp = selectionEnd;
                    selectionEnd = selectionStart;
                    selectionStart = temp;
                }


                if (selectionEnd > selectionStart)
                {
                    Spannable str = editPageText.getText();
                    BackgroundColorSpan[] ss = str.getSpans(selectionStart, selectionEnd, BackgroundColorSpan.class);

                    boolean exists = ss.length > 0;

                    if (!exists){
                        str.setSpan(new BackgroundColorSpan(0xFFFFFF00), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        StyleSpanRemover spanRemover = new StyleSpanRemover();
                        spanRemover.RemoveOne(str ,selectionStart, selectionEnd, BackgroundColorSpan.class);
                    }

                    iconHighlight.setChecked(!exists);
                }
            }
        });

        iconCamera = (ImageView) findViewById(R.id.icon_camera);
        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iconGallery = (ImageView) findViewById(R.id.icon_gallery);
        iconGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
