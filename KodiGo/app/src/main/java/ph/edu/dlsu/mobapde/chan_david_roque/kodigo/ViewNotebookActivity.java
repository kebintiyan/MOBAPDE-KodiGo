package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_EDITABLE;
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
    DatabaseHelper dbHelper;
    ArrayList<Page> pages;
    ArrayList<Long> pagesID;
    long notebookID;
    ItemTouch it;

    TextView tvNoPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notebook);

        dbHelper = new DatabaseHelper(getBaseContext());
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);

        tvNoPages = (TextView) findViewById(R.id.tv_no_pages);

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
                p.setText("");
                p.setNotebookID(notebookID);

                Intent i = new Intent(getBaseContext(), ViewPageActivity.class);
                long pageId = dbHelper.insertPage(p);
                Log.i("PAGEID: ", pageId+"");
                i.putExtra(KEY_PAGE_ID, pageId);
                i.putExtra(KEY_EDITABLE, true);
                startActivityForResult(i,REQUEST_EDIT_PAGE);
            }
        });

        pageCursorAdapter.setOnPageClickListener(new PageCursorAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(long pageId) {
                Intent i = new Intent(getBaseContext(), ViewPageActivity.class);
                i.putExtra(KEY_PAGE_ID, pageId);
                i.putExtra(KEY_EDITABLE, false);
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

        it.setOnItemLongClickListener(new ItemTouch.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {

                Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                viewHolder.itemView.setAnimation(AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.elevate ));
            }
        });
        it.setOnItemClearViewListener(new ItemTouch.OnItemClearViewListener() {
            @Override
            public void onItemClearView(ArrayList arrayList, RecyclerView.ViewHolder viewHolder) {
                viewHolder.itemView.clearAnimation();
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
            case R.id.action_delete:
                new MaterialDialog.Builder(this)
                        .title("Delete?")
                        .content("Are you sure you want to delete this notebook?")
                        .positiveText("Ok")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent result = new Intent();

                                dbHelper.deleteNotebook(notebookID);
                                //result.putExtra(KEY_NOTEBOOK_POSITION, notebook.getNotebookNumber());
                                setResult(RESULT_NOTEBOOK_DELETED, result);
                                Log.i("DeleteNotebookActivity", "Notebook deleted");
                                finish();
                            }
                        })
                        .show(); return true;
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
//            Page p = dbHelper.queryPageByID((long) data.getExtras().get(KEY_PAGE_ID));
//            p.setPageNumber(pageAdapter.getItemCount());
//            dbHelper.updatePage(p);
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

        if (pages.size() == 0) {
            tvNoPages.setVisibility(View.VISIBLE);
        }
        else {
            tvNoPages.setVisibility(View.INVISIBLE);
        }

    }

    protected void refreshPosition() {
        for(int i =0; i< pages.size(); i++){
            pages.get(i).setPageNumber(i);
            dbHelper.updatePage(pages.get(i));
        }
    }
}
