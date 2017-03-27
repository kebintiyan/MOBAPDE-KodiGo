package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

/**
 * Created by user on 3/19/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    EditText editTitlePage;
    TextView viewTitlePage;
    MenuInflater inflater;
    Menu menu;
    long pageID;
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


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        View rootView = inflater.inflate(R.layout.fragment_view_page_slide, container, false);

        dbHelper = new DatabaseHelper(rootView.getContext());

        Log.i("pgas", getActivity().getIntent().getExtras().getLong(KEY_PAGE_ID)+"");
        isEditable = Boolean.parseBoolean(getActivity().getIntent().getExtras().getLong(KEY_PAGE_ID)+"");

        pageID = getArguments().getLong(KEY_PAGE_ID);

        dbHelper    = new DatabaseHelper(getActivity().getBaseContext());
//        isEditable  = (boolean) getActivity().getIntent().getExtras().get(KEY_EDITABLE);
        page        = dbHelper.queryPageByID(getArguments().getLong(KEY_PAGE_ID));

        initViews(rootView);
        initActionBar(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.page_menu_bar, menu);
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(false);
        deletePage = menu.findItem(R.id.action_delete);
        toggleEdit(isEditable);
        super.onCreateOptionsMenu(menu, inflater);
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
                    if (!page.getText().equals(editPageText.getText().toString()))
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
            Log.i("isedit","yes");
            textView = View.GONE;
            editText = View.VISIBLE;
            saveItem.setVisible(true);
            deletePage.setVisible(false);
        }else {
            textView = View.VISIBLE;
            editText = View.GONE;
            viewPageText.setText(page.getText());
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

    public void onBackPressed() {
        clearFocus();
        if (isEditable) {

            showOnCancelConfirmDialog();
        }
        else {
            super.getActivity().onBackPressed();
        }
    }

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
        iconBold = (ImageView) rootView.findViewById(R.id.icon_bold);
        iconBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = editPageText.getSelectionStart();
                int selectionEnd = editPageText.getSelectionEnd();

                Spannable ss = editPageText.getText();
                StyleSpan[] spans = ss.getSpans(selectionStart,
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

        iconItalic = (ImageView) rootView.findViewById(R.id.icon_italic);
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

        iconUnderline = (ImageView) rootView.findViewById(R.id.icon_underline);
        iconUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = editPageText.getSelectionStart();
                int selectionEnd = editPageText.getSelectionEnd();

                Spannable ss = editPageText.getText();
                UnderlineSpan[] spans = ss.getSpans(selectionStart,
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
                    // Show dialog for creating comment
                    // Create comment
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
        page.setText(editPageText.getTextAsString(true));
        Log.i("SAAAAAAAAVE", page.getText());
        dbHelper.updatePage(page);
        toggleEdit(false);
    }




}