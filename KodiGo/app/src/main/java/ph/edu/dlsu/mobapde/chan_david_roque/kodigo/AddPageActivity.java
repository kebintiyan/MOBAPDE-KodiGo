package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AddPageActivity extends AppCompatActivity {
    Notebook currentNotebook;
    DatabaseOpenHelper dbhelper;
    int currentNotebookID;
    int numOfPages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DatabaseOpenHelper(getApplicationContext());
        currentNotebookID = (int)getIntent().getExtras().get("currentNotebook");
        numOfPages = (int) getIntent().getExtras().get("numOfPages");
        currentNotebook = dbhelper.queryNotebookByID(currentNotebookID);
        Page newPage = new Page();
        newPage.setNotebookID(currentNotebookID);
        newPage.setPageNumber(numOfPages+1);
        Log.i("numpaage", newPage.getPageNumber()+"");
        Intent i = new Intent(getBaseContext(), LoadPagesActivity.class);
        dbhelper.insertPage(newPage);

        i.putExtra("currentNotebook", currentNotebookID);
        startActivity(i);

    }
}
