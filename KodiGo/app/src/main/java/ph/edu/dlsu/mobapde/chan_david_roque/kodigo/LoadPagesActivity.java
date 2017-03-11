package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class LoadPagesActivity extends AppCompatActivity {
    DatabaseOpenHelper dbhelper;
    NotebookAdapter notebookAdapter;
    ArrayList<Page> pages;
    ArrayList<Long> pagesID;
    int currNotebookID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteDatabase db = null;
        dbhelper = new DatabaseOpenHelper(this);

        pages = new ArrayList<>();
        currNotebookID = (int) getIntent().getExtras().get("currentNotebook");
       // Notebook notebook = dbHelper.queryNotebookByID(currNotebookID);
        //pages = dbHelper.queryPagesByNotebookID(notebook.getNotebookID());
        pagesID = new ArrayList<>();
        pages = dbhelper.queryPagesByNotebookID(currNotebookID);
        for(int i=0; i<pages.size();i++){
            pagesID.add(pages.get(i).getPageID());
        }
        Log.i("num", "siiiize"+pages.size());
        Log.i("currNbLoad", currNotebookID +"");
        Intent i = new Intent(getBaseContext(), ViewNotebookActivity.class);
        i.putExtra("LoadedPages", pagesID);
        i.putExtra("currentNotebook", currNotebookID);
        i.putExtra("numOfPages", pages.size());
        startActivity(i);
    }
}
