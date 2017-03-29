package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
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
        ArrayList<Page> pages = dbHelper.queryPagesByNotebookID(notebookID);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), pages);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        int pageNumber = dbHelper.queryPageByID(pageID).getPageNumber();

        mViewPager.setCurrentItem(pageNumber-1);



    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
        Toast toast = Toast.makeText(getBaseContext(), "IM HERE", Toast.LENGTH_SHORT);
    }


}
