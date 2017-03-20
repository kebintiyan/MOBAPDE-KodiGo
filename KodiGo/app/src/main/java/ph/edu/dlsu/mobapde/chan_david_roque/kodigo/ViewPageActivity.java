package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_DELETED;

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
    MenuItem deletePage;
    Page page;
    long pageID;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbHelper = new DatabaseHelper(getBaseContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("");
        isEditable = (boolean) getIntent().getExtras().get(KEY_EDITABLE);
        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);

        Log.i("AAA", isEditable+"");


        page = dbHelper.queryPageByID(pageID);

        editTitlePage = (EditText) findViewById(R.id.editTitlePage);
        editPageText = (EditText) findViewById(R.id.editPageText);
        editTitlePage.setText(page.getName());
        editPageText.setText(page.getText());
        editPageText.setHint(getRandomHint());


        viewTitlePage = (TextView) findViewById(R.id.viewTitlePage);
        viewPageText = (TextView) findViewById(R.id.viewPageText);
        viewTitlePage.setText(page.getName());
        viewPageText.setText(page.getText());


        toolbar = (LinearLayout) findViewById(R.id.my_toolbar);

        toggleEditButton = (FloatingActionButton) findViewById(R.id.toggleEditButton);

        toggleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEdit(true);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.page_menu_bar, menu);
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
                page.setName(editTitlePage.getText().toString());
                page.setText(editPageText.getText().toString());
                dbHelper.updatePage(page);
                toggleEdit(false);
                return true;
            case R.id.action_delete:
<<<<<<< HEAD
                clearFocus();
                dbHelper.deletePage(pageID);
                finish();
=======
                new MaterialDialog.Builder(this)
                        .title("Confirm Delete")
                        .content("Are you sure you want to delete this page?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dbHelper.deletePage(pageID);
                                finish();
                            }
                        })
                        .show();
>>>>>>> 7187ba729c8f7a381ff6f7909575ac9e801643e7
                return true;
            case android.R.id.home:
                clearFocus();
                if(isEditable) {
                    showOnCancelConfirmDialog();
                }
                else
                    finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toggleEdit(boolean isEditable){

        int textView, editText;
        this.isEditable = isEditable;

        clearFocus();
        if(isEditable){
            textView = View.INVISIBLE;
            editText = View.VISIBLE;
            saveItem.setVisible(true);
            deletePage.setVisible(false);

            editTitlePage.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }else {
            textView = View.VISIBLE;
            editText = View.INVISIBLE;
            viewTitlePage.setText(page.getName());
            viewPageText.setText(page.getText());
            saveItem.setVisible(false);
            deletePage.setVisible(true);
        }

        viewTitlePage.setVisibility(textView);
        viewPageText.setVisibility(textView);
        toggleEditButton.setVisibility(textView);


        editTitlePage.setVisibility(editText);
        editPageText.setVisibility(editText);
        toolbar.setVisibility(editText);

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
                .title("Discard Changes")
                .content("Are you sure you want to discard your changes?")
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        toggleEdit(false);
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
}
