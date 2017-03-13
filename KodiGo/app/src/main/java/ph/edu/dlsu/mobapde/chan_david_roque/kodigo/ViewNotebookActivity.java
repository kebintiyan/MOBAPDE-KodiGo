package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
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

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_PAGE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_DELETED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_PAGE_ADDED;

public class ViewNotebookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addPageButton;
    PageCursorAdapter pageCursorAdapter;

    ActionBar actionBar;
    Notebook notebook;
    DatabaseOpenHelper dbHelper;
    ArrayList<Page> pages;
    ArrayList<Long> pagesID;
    long notebookID;
    ItemTouch it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notebook);

        dbHelper = new DatabaseOpenHelper(getBaseContext());
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);

        notebook = dbHelper.queryNotebookByID(notebookID);

        actionBar = getSupportActionBar();
        actionBar.setTitle(notebook.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.page_recyclerview);

        pages = dbHelper.queryPagesByNotebookID(notebookID);
        // Step 3: Create our adapter
        pageCursorAdapter = new PageCursorAdapter(getBaseContext(), null);

        // Step 4: Attach adapter to UI
        recyclerView.setAdapter(pageCursorAdapter);

        // Step 5: Attach layout manager to UI
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        it = new ItemTouch(pageCursorAdapter, pages);
        it.attachToRecyclerView(recyclerView);

        addPageButton = (FloatingActionButton) findViewById(R.id.addPageButton);

        addPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Page p = new Page();
                p.setName("Untitled");
                p.setNotebookID(notebookID);
                dbHelper.insertPage(p);
                Intent i = new Intent(getBaseContext(), ViewPageActivity.class);
                pageCursorAdapter.getCursor().getColumnIndex(Page.COLUMN_PAGE_ID);
                i.putExtra(KEY_PAGE_ID, pageId);
                startActivityForResult(i,REQUEST_EDIT_PAGE);
            }
        });

        pageCursorAdapter.setOnPageClickListener(new PageCursorAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(long pageId) {
                Intent i = new Intent(getBaseContext(), ViewPageActivity.class);
                i.putExtra(KEY_PAGE_ID, pageId);
                startActivityForResult(i,REQUEST_EDIT_PAGE);
            }
        });

        it.setOnItemMoveListener(new ItemTouch.OnItemMoveListener() {
            @Override
            public void onItemMoveClick(ArrayList arrayList) {
                pages = (ArrayList<Page>) arrayList;
                refreshPosition();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notebook_menu_bar, menu);
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
            case android.R.id.home:finish();
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


    @Override
    protected void onResume() {
        super.onResume();
        pages = dbHelper.queryPagesByNotebookID(notebookID);
        Cursor cursor = dbHelper.queryPagesByNotebookIDAsCursor(notebookID);
        it.setArrayList(pages);
        pageCursorAdapter.changeCursor(cursor);

    }

    protected void refreshPosition() {
        for(int i =0; i< pages.size(); i++){
            pages.get(i).setPageNumber(i);
            dbHelper.updatePage(pages.get(i));
        }
    }
}
