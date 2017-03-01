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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jrummyapps.android.colorpicker.ColorPanelView;
import com.jrummyapps.android.colorpicker.ColorPickerView;

import static android.app.Activity.RESULT_OK;

public class ColorpickerActivity extends AppCompatActivity implements ColorPickerView.OnColorChangedListener, View.OnClickListener {

    EditText notebookName;
    Button submitButton;
    Button cancelButton;
    ColorPickerView colorPickerView;
    ColorPanelView newColorPanelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);
        getWindow().setFormat(PixelFormat.RGBA_8888);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int initialColor = prefs.getInt("color_3", 0xFF000000);

        colorPickerView = (ColorPickerView) findViewById(R.id.cpv_color_picker_view);
        ColorPanelView colorPanelView = (ColorPanelView) findViewById(R.id.cpv_color_panel_old);
        newColorPanelView = (ColorPanelView) findViewById(R.id.cpv_color_panel_new);

        Button btnOK = (Button) findViewById(R.id.okButton);
        Button btnCancel = (Button) findViewById(R.id.cancelButton);

        ((LinearLayout) colorPanelView.getParent())
                .setPadding(colorPickerView.getPaddingLeft(), 0, colorPickerView.getPaddingRight(), 0);

        colorPickerView.setOnColorChangedListener(this);
        colorPickerView.setColor(initialColor, true);
        colorPanelView.setColor(initialColor);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }


    @Override
    public void onColorChanged(int newColor) {
        newColorPanelView.setColor(colorPickerView.getColor());
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okButton:

                Intent result = new Intent();
                result.putExtra(AddNotebookActivity.KEY_COLOR, colorPickerView.getColor());
                setResult(RESULT_OK, result);

                finish();
                break;
            case R.id.cancelButton:
                finish();
                break;
        }
    }

}
