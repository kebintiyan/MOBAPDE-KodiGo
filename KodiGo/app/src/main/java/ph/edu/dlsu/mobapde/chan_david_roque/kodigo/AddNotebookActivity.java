package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_TITLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_ADDED;

public class AddNotebookActivity extends AppCompatActivity {

    EditText notebookName;
    Button submitButton;
    ImageView titleColor;
    ImageView notebookColor;
    RelativeLayout notebookIcon;
    DatabaseOpenHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_notebook);
        dbhelper = new DatabaseOpenHelper(getApplicationContext());

        notebookName = (EditText) findViewById(R.id.notebookName);
        submitButton = (Button) findViewById(R.id.submitButton);
        notebookColor = (ImageView) findViewById(R.id.notebookColor);
        titleColor = (ImageView) findViewById(R.id.titleColor);
        notebookIcon = (RelativeLayout) findViewById(R.id.notebookIcon);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();

                Notebook notebook = new Notebook();
                notebook.setTitle(notebookName.getText().toString());
                ColorDrawable color = (ColorDrawable) titleColor.getBackground();
                notebook.setTitleColor(color.getColor());
                color = (ColorDrawable) notebookColor.getBackground();
                notebook.setNotebookColor(color.getColor());
                Log.i("AddNotebookActivity", "Notebook created");
                dbhelper.insertNotebook(notebook);
//                notebook.setNotebookID(notebookId);
//                result.putExtra(KEY_NOTEBOOK_ID, notebookId);
//                setResult(RESULT_NOTEBOOK_ADDED, result);
//                Log.i("AddNotebookActivity", "Notebook created");
                finish();

            }
        });


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
            case android.R.id.home:finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void colorPicker(int requestCode) {
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
}
