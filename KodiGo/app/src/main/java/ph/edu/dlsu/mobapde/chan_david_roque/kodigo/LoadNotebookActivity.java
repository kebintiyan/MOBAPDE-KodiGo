package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_LOAD_NOTEBOOKS;

public class LoadNotebookActivity extends AppCompatActivity {
    DatabaseOpenHelper dbhelper;
    NotebookAdapter notebookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbhelper = new DatabaseOpenHelper(this);

        ArrayList<Notebook> notebooks = new ArrayList<>();
        ArrayList<Long> notebooksID = new ArrayList<>();
        Log.i("HELLOW", "BOBO KA");
        notebooks = dbhelper.queryAllNotebooks();
        Log.i("HELLOW", notebooks.size()+"BOBO KA");
        for(int i=0; i<notebooks.size();i++){
            notebooksID.add(notebooks.get(i).getNotebookID());
        }

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        i.putExtra(KEY_LOAD_NOTEBOOKS, notebooksID);
        startActivity(i);
    }
}
