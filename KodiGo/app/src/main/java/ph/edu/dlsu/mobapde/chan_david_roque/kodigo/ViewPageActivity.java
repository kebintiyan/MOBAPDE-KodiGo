package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_PAGE_EDITED;

public class ViewPageActivity extends AppCompatActivity {

    EditText titlePage;
    EditText text;
    Button submitButton;
    Page page;
    long pageID;
    DatabaseOpenHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbhelper = new DatabaseOpenHelper(getBaseContext());

        titlePage = (EditText) findViewById(R.id.editTitlePage);
        text = (EditText) findViewById(R.id.editPageText);
        submitButton = (Button) findViewById(R.id.submitEditButton);
        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);

        page = dbhelper.queryPageByID(pageID);

        titlePage.setText(page.getName());
        text.setText(page.getText());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                page.setName(titlePage.getText().toString());
                page.setText(text.getText().toString());
                dbhelper.updatePage(page);
                Intent result = new Intent();

                setResult(RESULT_PAGE_EDITED, result);
                finish();
            }
        });
    }
}
