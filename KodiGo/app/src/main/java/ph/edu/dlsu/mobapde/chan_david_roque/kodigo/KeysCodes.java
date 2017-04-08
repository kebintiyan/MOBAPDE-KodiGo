package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.graphics.Color;

/**
 * Created by USER on 3/4/2017.
 */

public class KeysCodes {
    final static int REQUEST_ADD_NOTEBOOK = 1;
    final static int REQUEST_EDIT_OR_DELETE_NOTEBOOK = 2;
    final static int REQUEST_ADD_PAGE = 3;
    final static int REQUEST_DELETE_PAGE = 4;
    final static int REQUEST_EDIT_PAGE = 5;
    final static int REQUEST_ADD_COLOR_TITLE = 6;
    final static int REQUEST_ADD_COLOR_NOTEBOOK = 7;

    final static int RESULT_NOTEBOOK_ADDED = 1;
    final static int RESULT_NOTEBOOK_EDITED = 2;
    final static int RESULT_NOTEBOOK_DELETED = 3;
    final static int RESULT_PAGE_ADDED = 4;
    final static int RESULT_PAGE_EDITED = 5;
    final static int RESULT_PAGE_DELETED = 6;
    final static int RESULT_COLOR = 7;

    final static String KEY_NOTEBOOK = "notebook";
    final static String KEY_NOTEBOOK_POSITION = "position";
    final static String KEY_NOTEBOOK_ID = "notebookId";
    final static String KEY_COLOR = "color";
    final static String KEY_PAGE = "page";
    final static String KEY_PAGE_ID = "pageId";
    final static String KEY_EDITABLE = "editable";

    final static String KEY_LOAD_NOTEBOOKS = "loadnotebooks";
    final static String KEY_LOAD_PAGES = "loadpages";

    final static int [] KEY_COLORS = new int[] {
        Color.WHITE, Color.rgb(232, 234, 215), Color.parseColor("#F44336"),
            Color.parseColor("#4CAF50"), Color.parseColor("#2196F3"),
            Color.parseColor("#FFEB3B"), Color.rgb(45, 45, 45)};

}
