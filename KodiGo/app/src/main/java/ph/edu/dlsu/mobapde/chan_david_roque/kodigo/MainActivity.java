package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addNotebookButton;
    NotebookAdapter notebookAdapter;

    final static int REQUEST_ADD_NOTEBOOK = 0;
    final static String KEY_NOTEBOOK = "notebook";
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

        addNotebookButton = (FloatingActionButton) findViewById(R.id.addNotebookButton);

        addNotebookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), AddNotebookActivity.class), REQUEST_ADD_NOTEBOOK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ADD_NOTEBOOK == requestCode && resultCode == RESULT_OK){
            notebookAdapter.addNotebook((Notebook) data.getExtras().get(KEY_NOTEBOOK));
        }
    }
}
