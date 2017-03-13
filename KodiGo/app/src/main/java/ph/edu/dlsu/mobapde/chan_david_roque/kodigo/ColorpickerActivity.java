package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jrummyapps.android.colorpicker.ColorPanelView;
import com.jrummyapps.android.colorpicker.ColorPickerView;

import static android.app.Activity.RESULT_OK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_COLOR;

public class ColorpickerActivity extends AppCompatActivity implements ColorPickerView.OnColorChangedListener, View.OnClickListener {

    EditText notebookName;
    Button submitButton;
    ColorPickerView colorPickerView;
    ColorPanelView newColorPanelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int initialColor = prefs.getInt("color_3", 0xFF000000);

        colorPickerView = (ColorPickerView) findViewById(R.id.cpv_color_picker_view);
        ColorPanelView colorPanelView = (ColorPanelView) findViewById(R.id.cpv_color_panel_old);
        newColorPanelView = (ColorPanelView) findViewById(R.id.cpv_color_panel_new);

        Button btnOK = (Button) findViewById(R.id.okButton);

        ((LinearLayout) colorPanelView.getParent())
                .setPadding(colorPickerView.getPaddingLeft(), 0, colorPickerView.getPaddingRight(), 0);

        colorPickerView.setOnColorChangedListener(this);
        colorPickerView.setColor(initialColor, true);
        colorPanelView.setColor(initialColor);

        btnOK.setOnClickListener(this);

    }


    @Override
    public void onColorChanged(int newColor) {
        newColorPanelView.setColor(colorPickerView.getColor());
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okButton:

                Intent result = new Intent();
                result.putExtra(KEY_COLOR, colorPickerView.getColor());
                setResult(RESULT_COLOR, result);

                finish();
                break;
        }
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
}
