package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewPageActivity extends AppCompatActivity {

    EditText titlePage;
    EditText text;
    Button submitButton;
    Page page;
    int currPageID;
    DatabaseOpenHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbhelper = new DatabaseOpenHelper(getApplicationContext());

        titlePage = (EditText) findViewById(R.id.editTitlePage);
        text = (EditText) findViewById(R.id.editPageText);
        submitButton = (Button) findViewById(R.id.submitEditButton);
        currPageID = (int) getIntent().getExtras().get("Current Page");
        Log.i("currPageID", ""+ currPageID);
        page = dbhelper.queryPageByID(currPageID);
        titlePage.setText(page.getName());
        text.setText(page.getText());
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                page.setName(titlePage.getText().toString());
                page.setText(text.getText().toString());
                dbhelper.updatePage(page);
                Intent i = new Intent();
                Log.i("subedit", "ediiit" + page.getName());
                i.putExtra("editedPage", page.getPageID());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
