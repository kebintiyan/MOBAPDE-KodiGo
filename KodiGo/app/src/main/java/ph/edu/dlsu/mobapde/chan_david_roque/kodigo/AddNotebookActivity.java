package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static android.app.Activity.RESULT_OK;

public class AddNotebookActivity extends AppCompatActivity {

    EditText notebookName;
    Button submitButton;
    Button cancelButton;
    ImageView titleColor;
    ImageView notebookColor;
    RelativeLayout notebookIcon;

    final static int REQUEST_ADD_COLOR_TITILE = 0;
    final static int REQUEST_ADD_COLOR_NOTEBOOK = 1;
    final static String KEY_COLOR = "color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notebook);

        notebookName = (EditText) findViewById(R.id.notebookName);
        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
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

                result.putExtra(MainActivity.KEY_NOTEBOOK, (Parcelable) notebook);
                setResult(RESULT_OK, result);
                Log.i("AddNotebookActivity", "Notebook created");
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                colorPicker(REQUEST_ADD_COLOR_TITILE);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void colorPicker(int requestCode) {
        startActivityForResult(new Intent(getBaseContext(), ColorpickerActivity.class), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ADD_COLOR_TITILE == requestCode && resultCode == RESULT_OK){
            titleColor.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookName.setTextColor((Integer) data.getExtras().get(KEY_COLOR));;
        }
        else if(REQUEST_ADD_COLOR_NOTEBOOK == requestCode && resultCode == RESULT_OK){
            notebookColor.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookIcon.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
        }
    }
}
