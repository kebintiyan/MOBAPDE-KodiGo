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
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_TITLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_COLOR;

public class AddNotebookActivity extends AppCompatActivity {

    EditText notebookName;
    Button submitButton;
    ImageView titleColor;
    ImageView notebookColor;
    CardView notebookIcon;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_notebook);
        dbhelper = new DatabaseHelper(getApplicationContext());

        notebookName = (EditText) findViewById(R.id.notebookName);
        submitButton = (Button) findViewById(R.id.submitButton);
        notebookColor = (ImageView) findViewById(R.id.notebookColor);
        titleColor = (ImageView) findViewById(R.id.titleColor);
        notebookIcon = (CardView) findViewById(R.id.notebookIcon);



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home: showOnCancelConfirmDialog();
                return true;
            case R.id.action_submit:
                Notebook notebook = new Notebook();
                notebook.setTitle(notebookName.getText().toString());
                ColorDrawable color = (ColorDrawable) titleColor.getBackground();
                notebook.setTitleColor(color.getColor());
                color = (ColorDrawable) notebookColor.getBackground();
                notebook.setNotebookColor(color.getColor());
                Log.i("AddNotebookActivity", "Notebook created");
                dbhelper.insertNotebook(notebook);
                clearFocus();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onBackPressed()
    {
        showOnCancelConfirmDialog();
    }

    protected void showOnCancelConfirmDialog() {
        clearFocus();

        new MaterialDialog.Builder(this)
                .title("Add Notebook")
                .content("Are you sure you want to cancel adding a notebook?")
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        notebookName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void clearFocus() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
