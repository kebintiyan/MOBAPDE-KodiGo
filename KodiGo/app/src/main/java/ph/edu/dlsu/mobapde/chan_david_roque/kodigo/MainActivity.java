package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addNotebookButton;
    NotebookCursorAdapter notebookCursorAdapter;
    DatabaseOpenHelper dbHelper;
    ArrayList<Notebook> notebooks;
    ItemTouch it;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseOpenHelper(getBaseContext());
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
        it = new ItemTouch(notebookCursorAdapter, notebooks);
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

        notebookCursorAdapter.setOnNotebookLongClickListener(new NotebookCursorAdapter.OnNotebookLongClickListener() {
            @Override
            public void onNotebookLongClick(View v) {
                view = v;
                v.setAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.shake));
            }
        });

        notebookCursorAdapter.setOnNotebookReleaseListener(new NotebookCursorAdapter.OnNotebookReleaseListener() {
            @Override
            public void onNotebookRelease(View v) {
                v.setAnimation(null);
            }
        });

        it.setOnItemMoveListener(new ItemTouch.OnItemMoveListener() {
            @Override
            public void onItemMoveClick(ArrayList arrayList) {

                notebooks = (ArrayList<Notebook>) arrayList;
                refreshPosition();
                view.setAnimation(null);
                notebookCursorAdapter.isLongPressed= false;
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
        notebooks = dbHelper.queryAllNotebooks();
        Cursor cursor = dbHelper.queryAllNotebooksAsCursor();
        it.setArrayList(notebooks);
        notebookCursorAdapter.changeCursor(cursor);
    }


}
