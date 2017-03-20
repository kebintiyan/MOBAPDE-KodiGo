package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_TITLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;

public class EditNotebookActivity extends AppCompatActivity {

    EditText notebookName;
    Button updateButton;
    Button deleteButton;
    ImageView titleColor;
    ImageView notebookColor;
    CardView notebookIcon;
    Notebook notebook;
    DatabaseHelper dbHelper;
    long notebookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notebook);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(getApplicationContext());
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);
        notebook = dbHelper.queryNotebookByID(notebookID);
        getSupportActionBar().setTitle(notebook.getTitle());

        notebookName = (EditText) findViewById(R.id.notebookName);
        notebookColor = (ImageView) findViewById(R.id.notebookColor);
        titleColor = (ImageView) findViewById(R.id.titleColor);
        notebookIcon = (CardView) findViewById(R.id.notebookIcon);

        notebookName.setText(notebook.getTitle());
        notebookName.setTextColor(notebook.getTitleColor());
        titleColor.setBackgroundColor(notebook.getTitleColor());
        notebookIcon.setBackgroundColor(notebook.getNotebookColor());
        notebookColor.setBackgroundColor(notebook.getNotebookColor());

        notebookColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorPicker(REQUEST_ADD_COLOR_NOTEBOOK);
            }
        });

        titleColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorPicker(REQUEST_ADD_COLOR_TITLE);
            }
        });

    }

    public void colorPicker(int requestCode) {
        clearFocus();
        startActivityForResult(new Intent(getBaseContext(), ColorpickerActivity.class), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ADD_COLOR_TITLE == requestCode && resultCode == RESULT_COLOR){
            titleColor.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookName.setTextColor((Integer) data.getExtras().get(KEY_COLOR));;
        }
        else if(REQUEST_ADD_COLOR_NOTEBOOK == requestCode && resultCode == RESULT_COLOR){
            notebookColor.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookIcon.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:

                showOnCancelConfirmDialog();
                return true;
            case R.id.action_submit:
                Intent result = new Intent();
                notebook.setTitle(notebookName.getText().toString());
                ColorDrawable color = (ColorDrawable) titleColor.getBackground();
                notebook.setTitleColor(color.getColor());
                color = (ColorDrawable) notebookColor.getBackground();
                notebook.setNotebookColor(color.getColor());

                dbHelper.updateNotebook(notebook);
//                result.putExtra(KEY_NOTEBOOK_ID, notebookID);
                setResult(RESULT_NOTEBOOK_EDITED, result);
                Log.i("EdiNotebookActivity", "Notebook Edited");
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        showOnCancelConfirmDialog();
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
}
