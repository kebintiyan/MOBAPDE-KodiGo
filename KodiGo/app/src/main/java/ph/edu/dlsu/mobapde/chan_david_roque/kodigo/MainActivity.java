package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.support.v7.recyclerview.R.styleable.RecyclerView;
import static java.security.AccessController.getContext;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_LOAD_NOTEBOOKS;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_POSITION;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_ADDED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_DELETED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addNotebookButton;
    NotebookAdapter notebookAdapter;
    DatabaseOpenHelper dbhelper ;
    ArrayList<Notebook> notebooks;
    ArrayList<Long> notebooksID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhelper = new DatabaseOpenHelper(getApplicationContext());
        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.notebook_recyclerview);

        notebooksID = (ArrayList<Long>) getIntent().getExtras().get(KEY_LOAD_NOTEBOOKS);
        notebooks = new ArrayList<>();
        for(int i=0; i<notebooksID.size();i++){
            notebooks.add(dbhelper.queryNotebookByID(notebooksID.get(i)));
        }

        //notebooks = new ArrayList<>();
        notebookAdapter = new NotebookAdapter(notebooks);

        // Step 4: Attach adapter to UI
        recyclerView.setAdapter(notebookAdapter);
        // Step 5: Attach layout manager to UI
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        ItemTouch it = new ItemTouch(notebookAdapter, notebooks);
        it.attachToRecyclerView(recyclerView);

        addNotebookButton = (FloatingActionButton) findViewById(R.id.addNotebookButton);

        addNotebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), AddNotebookActivity.class), REQUEST_ADD_NOTEBOOK);
            }
        });

        notebookAdapter.setOnNotebookClickListener(new NotebookAdapter.OnNotebookClickListener() {
            @Override
            public void onNotebookClick(long notebookId) {
                startActivityForResult(new Intent(getBaseContext(), ViewNotebookActivity.class)
                        .putExtra(KEY_NOTEBOOK_ID, notebookId), REQUEST_EDIT_OR_DELETE_NOTEBOOK);
            }
        });

        it.setOnNotebookClickListener(new ItemTouch.OnItemMoveListener() {
            @Override
            public void onItemMoveClick(ArrayList arrayList) {
                ArrayList<Notebook> notebooks = (ArrayList<Notebook>) arrayList;
                refreshPosition();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(REQUEST_ADD_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_ADDED){
            Notebook n = dbhelper.queryNotebookByID((long) data.getExtras().get(KEY_NOTEBOOK_ID));
            n.setNotebookNumber(notebookAdapter.getItemCount());
            dbhelper.updateNotebook(n);
            notebookAdapter.addNotebook(n);
        }else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_EDITED){
            Notebook n = dbhelper.queryNotebookByID((long) data.getExtras().get(KEY_NOTEBOOK_ID));
            notebookAdapter.editNotebook(n);

        }else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_DELETED) {

            notebookAdapter.deleteNotebook((int) data.getExtras().get(KEY_NOTEBOOK_POSITION));
            refreshPosition();
        }
    }

    protected void refreshPosition() {
        for(int i =0; i< notebooks.size(); i++){
            notebooks.get(i).setNotebookNumber(i);
            Log.i("HI", notebooks.get(i).getNotebookNumber()+"");
            dbhelper.updateNotebook(notebooks.get(i));
        }
    }


}
