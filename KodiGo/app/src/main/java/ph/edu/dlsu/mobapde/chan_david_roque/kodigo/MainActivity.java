package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addNotebookButton;
    NotebookCursorAdapter notebookCursorAdapter;
    DatabaseHelper dbHelper;
    ArrayList<Notebook> notebooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(getBaseContext());
        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.notebook_recyclerview);

        notebooks = dbHelper.queryAllNotebooks();

        notebookCursorAdapter = new NotebookCursorAdapter(getBaseContext(), null);

        // Step 4: Attach adapter to UI
        recyclerView.setAdapter(notebookCursorAdapter);
        // Step 5: Attach layout manager to UI

        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        ItemTouch it = new ItemTouch(notebookCursorAdapter, notebooks);
        it.attachToRecyclerView(recyclerView);

        addNotebookButton = (FloatingActionButton) findViewById(R.id.addNotebookButton);

        addNotebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), AddNotebookActivity.class), REQUEST_ADD_NOTEBOOK);
            }
        });

        notebookCursorAdapter.setOnNotebookClickListener(new NotebookCursorAdapter.OnNotebookClickListener() {
            @Override
            public void onNotebookClick(long notebookId) {
                startActivityForResult(new Intent(getBaseContext(), ViewNotebookActivity.class)
                        .putExtra(KEY_NOTEBOOK_ID, notebookId), REQUEST_EDIT_OR_DELETE_NOTEBOOK);
            }
        });

        it.setOnNotebookClickListener(new ItemTouch.OnItemMoveListener() {
            @Override
            public void onItemMoveClick(ArrayList arrayList) {
                notebooks = (ArrayList<Notebook>) arrayList;
                refreshPosition();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(REQUEST_ADD_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_ADDED){
//            Notebook n = dbHelper.queryNotebookByID((long) data.getExtras().get(KEY_NOTEBOOK_ID));
//            n.setNotebookNumber(notebookAdapter.getItemCount());
//            dbHelper.updateNotebook(n);
//            notebookAdapter.addNotebook(n);
//        }else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_EDITED){
//            Notebook n = dbHelper.queryNotebookByID((long) data.getExtras().get(KEY_NOTEBOOK_ID));
//            notebookAdapter.editNotebook(n);
//
//        }else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_DELETED) {
//
//            notebookAdapter.deleteNotebook((int) data.getExtras().get(KEY_NOTEBOOK_POSITION));
//            refreshPosition();
//        }
    }

    protected void refreshPosition() {
        for(int i =0; i< notebooks.size(); i++){
            notebooks.get(i).setNotebookNumber(i);
            dbHelper.updateNotebook(notebooks.get(i));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = dbHelper.queryAllNotebooksAsCursor();
        notebookCursorAdapter.changeCursor(cursor);
    }


}
