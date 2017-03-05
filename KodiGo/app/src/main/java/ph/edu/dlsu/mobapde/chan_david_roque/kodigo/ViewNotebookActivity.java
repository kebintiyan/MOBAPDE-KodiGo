package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_POSITION;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_DELETED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;

public class ViewNotebookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addPageButton;
    PageAdapter pageAdapter;
    ActionBar actionBar;
    Notebook n;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notebook);

        n = (Notebook) getIntent().getExtras().get(KEY_NOTEBOOK);
        position = (int) getIntent().getExtras().get(KEY_NOTEBOOK_POSITION);
        // Step 1: create recycler view

        Log.i("title", n.getTitle());
        actionBar = getSupportActionBar();
        actionBar.setTitle(n.getTitle());

        //getActionBar().setTitle(n.getTitle());

        recyclerView = (RecyclerView) findViewById(R.id.page_recyclerview);

        ArrayList<Page> pages = new ArrayList<>();

        // Step 3: Create our adapter
        pageAdapter = new PageAdapter(pages);

        // Step 4: Attach adapter to UI
        recyclerView.setAdapter(pageAdapter);

        // Step 5: Attach layout manager to UI
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editbutton, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_edit:
                startActivityForResult(new Intent(getBaseContext(), EditNotebookActivity.class)
                        .putExtra(KEY_NOTEBOOK, n)
                        .putExtra(KEY_NOTEBOOK_POSITION, position)
                        , REQUEST_EDIT_OR_DELETE_NOTEBOOK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //rest of app
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ADD_PAGE == requestCode && resultCode == RESULT_OK){
            pageAdapter.addPage((Page) data.getExtras().get(KEY_PAGE));
        }
        else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_EDITED) {
            //EDIT
            Intent result = new Intent();
            result.putExtra(KEY_NOTEBOOK, (Notebook) data.getExtras().get(KEY_NOTEBOOK));
            result.putExtra(KEY_NOTEBOOK_POSITION, (int) data.getExtras().get(KEY_NOTEBOOK_POSITION));
            setResult(RESULT_NOTEBOOK_EDITED, result);
            finish();
        }
        else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_DELETED) {
            //DELETE
            Intent result = new Intent();
            result.putExtra(KEY_NOTEBOOK_POSITION, (int) data.getExtras().get(KEY_NOTEBOOK_POSITION));
            setResult(RESULT_NOTEBOOK_DELETED, result);
            finish();
        }
    }
}
