package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_PAGE_ADDED;

public class AddPageActivity extends AppCompatActivity {

    EditText pageText;
    Notebook currentNotebook;
    DatabaseOpenHelper dbhelper;
    long notebookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DatabaseOpenHelper(getApplicationContext());
        notebookID = (long)getIntent().getExtras().get(KEY_NOTEBOOK_ID);
        pageText = (EditText) findViewById(R.id.page_text);

        Page p = new Page();
        p.setName("page");
        p.setNotebookID(notebookID);
        p.setText(pageText.getText().toString());
        p.setPageID(dbhelper.insertPage(p));

        Intent result = new Intent();
        result.putExtra(KEY_PAGE_ID, p.getPageID());
        setResult(RESULT_PAGE_ADDED, result);
        finish();

    }
}
