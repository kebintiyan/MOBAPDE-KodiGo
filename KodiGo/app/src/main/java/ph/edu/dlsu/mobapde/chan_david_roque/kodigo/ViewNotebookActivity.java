package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class ViewNotebookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addPageButton;
    PageAdapter pageAdapter;
    View container;
    DatabaseOpenHelper dbhelper;
    Notebook currentNotebook;
    ArrayList<Page> pages;
    ArrayList<Integer> pagesID;
    int currNotebookID;
    final static int REQUEST_ADD_PAGE = 0;
    final static int REQUEST_EDIT_PAGE = 1;
    final static String KEY_PAGE = "page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notebook);

        dbhelper = new DatabaseOpenHelper(getApplicationContext());
        container = findViewById(R.id.activity_view_page);
        currNotebookID = (int) getIntent().getExtras().get("currentNotebook");

        currentNotebook = dbhelper.queryNotebookByID(currNotebookID);

        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.page_recyclerview);

        pagesID = (ArrayList<Integer>) getIntent().getExtras().get("LoadedPages");
        pages = new ArrayList<>();
        for(int i=0; i<pagesID.size();i++){
            Log.i("pageforID", ""+pagesID.get(i));
            pages.add(dbhelper.queryPageByID(pagesID.get(i)));
        }

      //  ArrayList<Page> pages =  (ArrayList<Page>)
        //pages.add((Page)getIntent().getExtras().get("editedPage"));
        // Step 3: Create our adapter
        pageAdapter = new PageAdapter(pages);

        pageAdapter.setOnPageClickListener(new PageAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(Page page) {
                Intent i = new Intent(getBaseContext(), ViewPageActivity.class);
                i.putExtra("Current Page", page.getPageID());
                startActivityForResult(i,REQUEST_EDIT_PAGE);
            }
        });

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
                Log.i("newpage", "Page created");
                int currentNotebookID = (int)getIntent().getExtras().get("currentNotebook");
                int numOfPages = (int)getIntent().getExtras().get("numOfPages");
                Intent i = new Intent(getBaseContext(), AddPageActivity.class);
                Log.i("currNoteToadd", ""+currentNotebookID);
                i.putExtra("currentNotebook", currentNotebookID);
                i.putExtra("numOfPages", numOfPages);
                startActivity(i);
            }
        });



        //addPageButton = (FloatingActionButton) findViewById(R.id.addPageButton);

//        addPageButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //startActivityForResult(new Intent(getBaseContext(), AddPageActivity.class), REQUEST_ADD_NOTEBOOK);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(REQUEST_ADD_PAGE == requestCode && resultCode == RESULT_OK){
            String newPageID = data.getExtras().get("newPage").toString();
            Page p = dbhelper.queryPageByID(Integer.parseInt(newPageID));
            pageAdapter.addPage(p);
        }else if(REQUEST_EDIT_PAGE == requestCode && resultCode == RESULT_OK){

            Page p = dbhelper.queryPageByID((int) data.getExtras().get("editedPage"));
            Log.i("GOTOEDIT", "PLS" +p.getName());
            pageAdapter.editPage(p);
        }
    }
}
