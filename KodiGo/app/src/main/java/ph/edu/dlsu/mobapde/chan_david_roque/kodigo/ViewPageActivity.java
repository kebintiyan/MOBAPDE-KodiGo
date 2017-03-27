package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
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
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
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

    Long pageID,notebookID;
    // Icons
    ImageView iconBold;
    ImageView iconItalic;
    ImageView iconUnderline;
    ImageView iconComment;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbHelper    = new DatabaseHelper(getBaseContext());

        isEditable = (boolean) getIntent().getExtras().get(KEY_EDITABLE);
        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), dbHelper.queryPagesByNotebookID(notebookID));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        int pageNumber = dbHelper.queryPageByID(pageID).getPageNumber();
        mViewPager.setCurrentItem(pageNumber-1);

    }

}
