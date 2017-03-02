package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ViewPageActivity extends AppCompatActivity {

    EditText titlePage;
    EditText text;
    Page page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        titlePage = (EditText) findViewById(R.id.titlePage);
        text = (EditText) findViewById(R.id.pageText);

        page = (Page) getIntent().getExtras().get("Current Page");
        titlePage.setText(page.getName());
        text.setText(page.getText());


    }
}
