package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_EDIT_OR_DELETE_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_PAGE_EDITED;

public class ViewPageActivity extends AppCompatActivity {

    EditText editTitlePage;
    EditText editPageText;
    TextView viewTitlePage;
    TextView viewPageText;
    FloatingActionButton toggleEditButton;
    MenuInflater inflater;
    Menu menu;
    Page page;
    long pageID;
    DatabaseOpenHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbhelper = new DatabaseOpenHelper(getBaseContext());

        editTitlePage = (EditText) findViewById(R.id.editTitlePage);
        editPageText = (EditText) findViewById(R.id.editPageText);

        viewTitlePage = (TextView) findViewById(R.id.viewTitlePage);
        viewPageText = (TextView) findViewById(R.id.viewPageText);

        toggleEditButton = (FloatingActionButton) findViewById(R.id.toggleEditButton);
        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);

        page = dbhelper.queryPageByID(pageID);

        viewTitlePage.setText(page.getName());
        viewPageText.setText(page.getText());

        toggleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTitlePage.setVisibility(View.INVISIBLE);
                viewPageText.setVisibility(View.INVISIBLE);
                toggleEditButton.setVisibility(View.INVISIBLE);
                inflater.inflate(R.menu.page_menu_bar, menu);
                editTitlePage.setVisibility(View.VISIBLE);
                editPageText.setVisibility(View.VISIBLE);
                editTitlePage.setText(page.getName());
                editPageText.setText(page.getText());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_edit:
                page.setName(editTitlePage.getText().toString());
                page.setText(editPageText.getText().toString());
                dbhelper.updatePage(page);
                Intent result = new Intent();

                setResult(RESULT_PAGE_EDITED, result);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
