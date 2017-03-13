package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;

public class ViewPageActivity extends AppCompatActivity {

    EditText editTitlePage;
    EditText editPageText;
    TextView viewTitlePage;
    TextView viewPageText;
    FloatingActionButton toggleEditButton;
    LinearLayout toolbar;
    MenuInflater inflater;
    Menu menu;
    MenuItem saveItem;
    Page page;
    long pageID;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        dbHelper = new DatabaseHelper(getBaseContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pageID = (long) getIntent().getExtras().get(KEY_PAGE_ID);

        page = dbHelper.queryPageByID(pageID);

        editTitlePage = (EditText) findViewById(R.id.editTitlePage);
        editPageText = (EditText) findViewById(R.id.editPageText);
        editTitlePage.setText(page.getName());
        editPageText.setText(page.getText());

        viewTitlePage = (TextView) findViewById(R.id.viewTitlePage);
        viewPageText = (TextView) findViewById(R.id.viewPageText);
        viewTitlePage.setText(page.getName());
        viewPageText.setText(page.getText());

        toolbar = (LinearLayout) findViewById(R.id.my_toolbar);

        toggleEditButton = (FloatingActionButton) findViewById(R.id.toggleEditButton);

        toggleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditable(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.page_menu_bar, menu);
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                page.setName(editTitlePage.getText().toString());
                page.setText(editPageText.getText().toString());
                dbHelper.updatePage(page);
                isEditable(false);
                return true;
            case android.R.id.home:finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void isEditable(boolean isEditable){

        int textView, editText;

        if(isEditable){
            textView = View.INVISIBLE;
            editText = View.VISIBLE;
            saveItem.setVisible(true);
        }else {
            textView = View.VISIBLE;
            editText = View.INVISIBLE;
            viewTitlePage.setText(page.getName());
            viewPageText.setText(page.getText());
            saveItem.setVisible(false);
        }

        viewTitlePage.setVisibility(textView);
        viewPageText.setVisibility(textView);
        toggleEditButton.setVisibility(textView);


        editTitlePage.setVisibility(editText);
        editPageText.setVisibility(editText);
        toolbar.setVisibility(editText);

    }
}
