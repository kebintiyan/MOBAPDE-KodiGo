package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_COLORS;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_TITLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;

public class EditNotebookActivity extends AppCompatActivity {

    EditText notebookName;
    LineColorPicker notebookColorPicker;
    LineColorPicker titleColorPicker;
    CardView notebookIcon;
    Notebook notebook;
    DatabaseHelper dbHelper;
    Button customNotebookColorPicker;
    Button customTitleColorPicker;
    long notebookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notebook);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Notebook");

        dbHelper = new DatabaseHelper(getApplicationContext());
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);
        notebook = dbHelper.queryNotebookByID(notebookID);

        notebookName = (EditText) findViewById(R.id.notebookName);
        notebookIcon = (CardView) findViewById(R.id.notebookIcon);
        customNotebookColorPicker = (Button) findViewById(R.id.customNotebookColorPicker);
        customTitleColorPicker = (Button) findViewById(R.id.customTitleColorPicker);
        customNotebookColorPicker.setBackgroundColor(notebook.getNotebookColor());
        customTitleColorPicker.setBackgroundColor(notebook.getTitleColor());
        notebookName.setText(notebook.getTitle());
        notebookName.setTextColor(notebook.getTitleColor());
        notebookIcon.setCardBackgroundColor(notebook.getNotebookColor());

        notebookColorPicker = (LineColorPicker) findViewById(R.id.notebookColor);

        notebookColorPicker.setColors(KEY_COLORS);

        notebookColorPicker.setSelectedColor(notebook.getNotebookColor());

        notebookColorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                notebookIcon.setCardBackgroundColor(c);
            }
        });

        titleColorPicker = (LineColorPicker) findViewById(R.id.titleColor);
        titleColorPicker.setColors(KEY_COLORS);

        titleColorPicker.setSelectedColor(notebook.getTitleColor());

        titleColorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                notebookName.setTextColor(c);
            }
        });

        customNotebookColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker(REQUEST_ADD_COLOR_NOTEBOOK);
            }
        });

        customTitleColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker(REQUEST_ADD_COLOR_TITLE);
            }
        });

    }

    public void colorPicker(int requestCode){
        clearFocus();
        startActivityForResult(new Intent(getBaseContext(), ColorpickerActivity.class), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(REQUEST_ADD_COLOR_TITLE == requestCode && resultCode == RESULT_COLOR){
            customTitleColorPicker.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookName.setTextColor((Integer) data.getExtras().get(KEY_COLOR));;
        }
        else if(REQUEST_ADD_COLOR_NOTEBOOK == requestCode && resultCode == RESULT_COLOR){
            customNotebookColorPicker.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookIcon.setCardBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
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
                int titleColorValue = titleColorPicker.getColor();
                int notebookColorValue = notebookColorPicker.getColor();

                if (!notebook.getTitle().equals(notebookName.getText().toString()) ||
                        notebook.getTitleColor() != titleColorValue ||
                        notebook.getNotebookColor() != notebookColorValue)
                    showOnCancelConfirmDialog();
                else
                    finish();

                return true;
            case R.id.action_submit:
                if (!validateInput())
                    return true;

                Intent result = new Intent();
                notebook.setTitle(notebookName.getText().toString());
                notebook.setTitleColor(notebookName.getTextColors().getDefaultColor());
                notebook.setNotebookColor(notebookIcon.getCardBackgroundColor().getDefaultColor());

                dbHelper.updateNotebook(notebook);
//                result.putExtra(KEY_NOTEBOOK_ID, notebookID);
                setResult(RESULT_NOTEBOOK_EDITED, result);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateInput() {
        String name = notebookName.getText().toString();
        if(name.length() == 0)
        {
            notebookName.setError("You cannot have an empty title.");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        showOnCancelConfirmDialog();
    }

    public void showOnCancelConfirmDialog() {
        clearFocus();
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Discard?")
                .content("Are you sure you want to discard your changes?")
                .positiveText("Ok")
                .negativeText("Cancel")
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
