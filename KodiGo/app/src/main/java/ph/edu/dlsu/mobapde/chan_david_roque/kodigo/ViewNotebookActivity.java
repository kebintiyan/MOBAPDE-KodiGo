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

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_LOAD_PAGES;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_POSITION;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_DELETED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_PAGE_ADDED;

public class ViewNotebookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addPageButton;
    PageAdapter pageAdapter;
    ActionBar actionBar;
    Notebook notebook;
    DatabaseOpenHelper dbhelper;
    ArrayList<Page> pages;
    ArrayList<Long> pagesID;
    long notebookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notebook);

        dbhelper = new DatabaseOpenHelper(getApplicationContext());
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);

        notebook = dbhelper.queryNotebookByID(notebookID);

        actionBar = getSupportActionBar();
        actionBar.setTitle(notebook.getTitle());

        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.page_recyclerview);

//        pagesID = (ArrayList<Long>) getIntent().getExtras().get(KEY_LOAD_PAGES);
//        pages = new ArrayList<>();
//
//        for(int i=0; i< pagesID.size();i++){
//            pages.add(dbHelper.queryPageByID(pagesID.get(i)));
//        }

        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);
        pages = dbhelper.queryPagesByNotebookID(notebookID);
        // Step 3: Create our adapter
        pageAdapter = new PageAdapter(pages);

        // Step 4: Attach adapter to UI
        recyclerView.setAdapter(pageAdapter);

        // Step 5: Attach layout manager to UI
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));
        addPageButton = (FloatingActionButton) findViewById(R.id.addPageButton);

        addPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), AddPageActivity.class)
                        .putExtra(KEY_NOTEBOOK_ID, notebookID), REQUEST_ADD_PAGE);
            }
        });

        pageAdapter.setOnPageClickListener(new PageAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(Page page) {
                Intent i = new Intent(getBaseContext(), ViewPageActivity.class);
                i.putExtra(KEY_PAGE_ID, page.getPageID());
                startActivityForResult(i,REQUEST_EDIT_PAGE);
            }
        });

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
                        .putExtra(KEY_NOTEBOOK_ID, notebookID)
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

//        if(REQUEST_ADD_PAGE == requestCode && resultCode == RESULT_OK){
//            String newPageID = data.getExtras().get("newPage").toString();
//            Page p = dbHelper.queryPageByID(Integer.parseInt(newPageID));
//            pageAdapter.addPage(p);
//        }else if(REQUEST_EDIT_PAGE == requestCode && resultCode == RESULT_OK){
//
//            Page p = dbHelper.queryPageByID((int) data.getExtras().get("editedPage"));
//            Log.i("GOTOEDIT", "PLS" +p.getName());
//            pageAdapter.editPage(p);
//        }
        if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_EDITED) {
            //EDIT
            Intent result = new Intent();
//            result.putExtra(KEY_NOTEBOOK_ID, (long) data.getExtras().get(KEY_NOTEBOOK_ID));
            setResult(RESULT_NOTEBOOK_EDITED, result);
            finish();
        }
        else if(REQUEST_EDIT_OR_DELETE_NOTEBOOK == requestCode && resultCode == RESULT_NOTEBOOK_DELETED) {
            //DELETE
            Intent result = new Intent();
//            result.putExtra(KEY_NOTEBOOK_POSITION, (int) data.getExtras().get(KEY_NOTEBOOK_POSITION));
            setResult(RESULT_NOTEBOOK_DELETED, result);
            finish();
        }else if(REQUEST_ADD_PAGE == requestCode && resultCode == RESULT_PAGE_ADDED) {
//            Page p = dbhelper.queryPageByID((long) data.getExtras().get(KEY_PAGE_ID));
//            p.setPageNumber(pageAdapter.getItemCount());
//            dbhelper.updatePage(p);
//
//            pageAdapter.addPage(p);
        }
    }
}
