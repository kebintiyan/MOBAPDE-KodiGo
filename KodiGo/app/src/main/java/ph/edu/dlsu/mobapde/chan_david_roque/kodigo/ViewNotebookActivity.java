package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;

public class ViewNotebookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addPageButton;
    PageAdapter pageAdapter;


    final static int REQUEST_ADD_PAGE = 0;
    final static String KEY_PAGE = "page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notebook);

        Page n = (Page) getIntent().getExtras().get(MainActivity.KEY_NOTEBOOK);
        // Step 1: create recycler view
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
        addPageButton = (FloatingActionButton) findViewById(R.id.addPageButton);

        addPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageAdapter.addPage(new Page());
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
            pageAdapter.addPage((Page) data.getExtras().get(KEY_PAGE));
        }
    }
}
