package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment{




    public static final int REQUEST_STORAGE= 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;


    String imageFile = "";
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
    String imageFileLocation;
    String editPageTextTemp;

    // Icons
    ToggleButton iconBold;
    ToggleButton iconItalic;
    ToggleButton iconUnderline;
    ToggleButton iconHighlight;
    ImageView iconCamera;
    ImageView iconGallery;
    ImageView iconComment;
    ImageView imageView;

    int styleStart;
    int cursorLoc;

    Bitmap imagefs;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(long notebookID, long pageID) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_PAGE_ID, pageID);
        args.putLong(KEY_NOTEBOOK_ID, notebookID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_view_page_slide, container, false);

        dbHelper    = new DatabaseHelper(getActivity().getBaseContext());
        isEditable  = Boolean.parseBoolean(getActivity().getIntent().getExtras().getLong(KEY_PAGE_ID)+"");
        page        = dbHelper.queryPageByID(getArguments().getLong(KEY_PAGE_ID));

        initViews(rootView);
        initActionBar(rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.page_menu_bar, menu);
        Log.i("A", "hello po");
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(false);
        deletePage = menu.findItem(R.id.action_delete);
        toggleEdit(isEditable);
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

                new MaterialDialog.Builder(getActivity())
                        .title("Delete?")
                        .content("Are you sure you want to delete this page?")
                        .positiveText("Ok")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dbHelper.deletePage(page.getPageID());
                                getActivity().finish();
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
                        getActivity().finish();
                }
                else
                    getActivity().finish();
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
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            editPageText.setSelection(editPageText.getText().toString().length());
        }

    }

/*
    @Override
    public void onBackPressed() {
        clearFocus();
        if (isEditable) {

            showOnCancelConfirmDialog();
        }
        else {
            super.getActivity().onBackPressed();
        }
    }
*/
    public void showOnCancelConfirmDialog() {
        clearFocus();
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Discard?")
                .content("Are you sure you want to discard your changes?")
                .positiveText("Ok")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().finish();
                    }
                })
                .show();
    }

    public void clearFocus() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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

    private void initViews(View rootView) {
        editPageText        = (HTMLEditText)            rootView.findViewById(R.id.editPageText);
        viewPageText        = (HTMLTextView)            rootView.findViewById(R.id.viewPageText);
        toolbar             = (HorizontalScrollView)    rootView.findViewById(R.id.my_toolbar);
        toggleEditButton    = (FloatingActionButton)    rootView.findViewById(R.id.toggleEditButton);
        toolbarTitle        = (TextView)                rootView.findViewById(R.id.toolbarTitle);

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

        initToolbarIcons(rootView);

    }

    private void initActionBar(View rootView) {
        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) rootView.findViewById(R.id.toolbar));
        toolbarTitle.setText(page.getName());
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTitleDialog();
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void showEditTitleDialog() {
        new MaterialDialog.Builder(getActivity())
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

    private void initToolbarIcons(View rootView) {
        // Icons
        iconBold = (ToggleButton) rootView.findViewById(R.id.icon_bold);
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

        iconItalic = (ToggleButton) rootView.findViewById(R.id.icon_italic);
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

        iconUnderline = (ToggleButton) rootView.findViewById(R.id.icon_underline);
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

        iconHighlight = (ToggleButton) rootView.findViewById(R.id.icon_highlight);
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

        iconCamera = (ImageView) rootView.findViewById(R.id.icon_camera);
        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        iconGallery = (ImageView) rootView.findViewById(R.id.icon_gallery);
        iconGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
            }
        });

        iconComment = (ImageView) rootView.findViewById(R.id.icon_comment);
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
        Log.i("eptgette", editPageText.getTextAsString(true));
        page.setText(editPageText.getTextAsString(true));
        dbHelper.updatePage(page);

        SpannableString s = new SpannableString(page.getText());
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(editPageText.getText());

        viewPageText.setText(s);
        editPageText.setText(s, true);
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



    private void createImageSpan(Uri imageURI){

        try {

            InputStream imageStream = getActivity().getContentResolver().openInputStream(imageURI);
            imagefs = BitmapFactory.decodeStream(imageStream);

            int selectionStart = editPageText.getSelectionStart();

            HTMLImageSpan imageSpan = new HTMLImageSpan(getActivity().getBaseContext(), imageURI);

            if(selectionStart == editPageText.getText().toString().length()) {
                editPageText.getText().insert(selectionStart, " ");
                if(selectionStart>0)
                    editPageText.setSelection(selectionStart-1);
            }
            SpannableStringBuilder spanText = new SpannableStringBuilder(editPageText.getText());

            spanText.append("\r");
            spanText.append("\n");
            spanText.setSpan(imageSpan, selectionStart, selectionStart+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ClickableSpan cs = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //popup  fullscreen
                    //ImageView iv = (ImageView) findViewById(R.id.iv_fullscreen);
                    //iv.setImageBitmap(imagefs);
                    Toast.makeText(getActivity().getBaseContext(), "img clicked", Toast.LENGTH_SHORT);
                }
            };
            spanText.setSpan(cs, selectionStart, selectionStart+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanText.append("\n");

            editPageText.setText(spanText);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public File createImageFile() throws IOException {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        String imageFileName = page.getNotebookID() +"-" +page.getPageID() +""+seconds;
        File photo;
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        photo = File.createTempFile(imageFileName, ".jpg",storageDirectory);
        imageFileLocation = photo.getAbsolutePath();
        return photo;
    }

    public boolean checkStoragePermission(){
        if(ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_STORAGE){
            takePhoto();
        }
    }

    public void takePhoto(){
        if(checkStoragePermission()) {

            Intent cameraIntent = new Intent();
            cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            File photoFIle = null;
            try {
                photoFIle = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageFile = photoFIle.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFIle));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap scaled = Bitmap.createScaledBitmap(inImage, 48*10, 48*7, true);


        scaled.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), scaled, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bitmap bitmap = BitmapFactory.decodeFile(imageFileLocation);
            createImageSpan(getImageUri(getActivity().getBaseContext(),bitmap));
        }else if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK){
            createImageSpan(data.getData());
        }
    }
}