package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
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

import static android.support.v7.recyclerview.R.styleable.RecyclerView;
import static java.security.AccessController.getContext;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.notebook_recyclerview);

        ArrayList<Notebook> notebooks = new ArrayList<>();

        // Step 3: Create our adapter
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

        addNotebookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), AddNotebookActivity.class), REQUEST_ADD_NOTEBOOK);
            }
        });

        notebookAdapter.setOnItemClickListener(new NotebookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Notebook notebook, int position) {

                startActivityForResult(new Intent(getBaseContext(), ViewNotebookActivity.class)
                        .putExtra(KEY_NOTEBOOK, notebook)
                        .putExtra(KEY_NOTEBOOK_POSITION, position), REQUEST_EDIT_OR_DELETE_NOTEBOOK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("DELETED", "HINDI");
        if(REQUEST_ADD_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_ADDED){
            notebookAdapter.addNotebook((Notebook) data.getExtras().get(KEY_NOTEBOOK));
        }else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_EDITED){
            notebookAdapter.editNotebook((Notebook) data.getExtras().get(KEY_NOTEBOOK), (int) data.getExtras().get(KEY_NOTEBOOK_POSITION));
        }else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_DELETED){
            notebookAdapter.deleteNotebook((int) data.getExtras().get(KEY_NOTEBOOK_POSITION));
        }
    }
}
