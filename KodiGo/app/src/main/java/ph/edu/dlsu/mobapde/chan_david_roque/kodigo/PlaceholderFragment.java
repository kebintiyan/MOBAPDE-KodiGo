package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

/**
 * Created by user on 3/19/2017.
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_PAGE_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    EditText editTitlePage;
    EditText editPageText;
    TextView viewTitlePage;
    TextView viewPageText;
    boolean isEditable;
    FloatingActionButton toggleEditButton;
    LinearLayout toolbar;
    MenuInflater inflater;
    Menu menu;
    MenuItem saveItem;
    Page page;
    long pageID;
    DatabaseHelper dbHelper;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(long notebookID, long pageID) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_PAGE_ID, pageID);
        args.putLong(KEY_NOTEBOOK_ID, notebookID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_page_slide, container, false);

        dbHelper = new DatabaseHelper(rootView.getContext());

        Log.i("pgas", getActivity().getIntent().getExtras().getLong(KEY_PAGE_ID)+"");
        isEditable = Boolean.parseBoolean(getActivity().getIntent().getExtras().getLong(KEY_PAGE_ID)+"");

        pageID = getArguments().getLong(KEY_PAGE_ID);

        page = dbHelper.queryPageByID(pageID);
        Log.i("pagename", page.getName());
        editTitlePage = (EditText) rootView.findViewById(R.id.editTitlePage);
        editPageText = (EditText) rootView.findViewById(R.id.editPageText);
        editTitlePage.setText(page.getName());
        editPageText.setText(page.getText());

        viewTitlePage = (TextView) rootView.findViewById(R.id.viewTitlePage);
        viewPageText = (TextView) rootView.findViewById(R.id.viewPageText);
        viewTitlePage.setText(page.getName());
        viewPageText.setText(page.getText());

        toolbar = (LinearLayout) rootView.findViewById(R.id.my_toolbar);

        toggleEditButton = (FloatingActionButton) rootView.findViewById(R.id.toggleEditButton);

        toggleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEdit(true);
            }
        });
        setHasOptionsMenu(true);
        return rootView;
    }



    public String getRandomHint() {
        Random rand = new Random();

        switch(rand.nextInt(4)) {
            case 0:
                return "What's on your mind?";
            case 1:
                return "Start being productive here...";
            case 2:
                return "Start writing here...";
            case 3:
                return "Start your notes here...";
            default:
                return "How about something creative...";
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.page_menu_bar, menu);
        saveItem = menu.findItem(R.id.action_save);
        Log.i("HEassaL", "asd");
        saveItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                page.setName(editTitlePage.getText().toString());
                page.setText(editPageText.getText().toString());
                dbHelper.updatePage(page);
                toggleEdit(false);
                return true;
            case android.R.id.home: if(isEditable)
                                        toggleEdit(false);
                                    else
                                        getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toggleEdit(boolean isEditable){

        int textView, editText;
        this.isEditable = isEditable;
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