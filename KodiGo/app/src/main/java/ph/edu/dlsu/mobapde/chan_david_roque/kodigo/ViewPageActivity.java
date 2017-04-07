package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;

public class ViewPageActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    boolean isEditable;
    Long pageID,notebookID;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_slide);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);
        dbHelper    = new DatabaseHelper(getBaseContext());
        ArrayList<Page> pages = dbHelper.queryPagesByNotebookID(notebookID);
        isEditable = (boolean) getIntent().getExtras().get(KEY_EDITABLE);

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
        Toast.makeText(getBaseContext(), "IM HERE", Toast.LENGTH_SHORT);
    }




}
