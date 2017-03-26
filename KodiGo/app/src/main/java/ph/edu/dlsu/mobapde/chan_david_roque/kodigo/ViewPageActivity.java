package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;

public class ViewPageActivity extends AppCompatActivity {

    EditText editTitlePage;
    EditText editPageText;
    TextView viewTitlePage;
    TextView viewPageText;
    boolean isEditable;
    FloatingActionButton toggleEditButton;
    LinearLayout toolbar;
    MenuInflater inflater;
    Menu menu;
    MenuItem saveItem;
    Page page;
    long pageID, notebookID;
    DatabaseHelper dbHelper;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbHelper = new DatabaseHelper(getBaseContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isEditable = (boolean) getIntent().getExtras().get(KEY_EDITABLE);
        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);

        Log.i("AAA", isEditable+"");


        dbHelper = new DatabaseHelper(getBaseContext());
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);
        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), dbHelper.queryPagesByNotebookID(notebookID));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        int pageNumber = dbHelper.queryPageByID(pageID).getPageNumber();
        mViewPager.setCurrentItem(pageNumber-1);


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
}
