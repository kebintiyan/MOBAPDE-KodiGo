package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ddavid on 3/7/2017.
 */

public class PageCursorAdapter extends CursorRecyclerViewAdapter<PageCursorAdapter.PageHolder> {

    @Override
    public void onBindViewHolder(PageHolder viewHolder, Cursor cursor) {

        viewHolder.pageName.setText(cursor.getString(cursor.getColumnIndex(Page.COLUMN_NAME)));
        viewHolder.pageText.setText(cursor.getString(cursor.getColumnIndex(Page.COLUMN_TEXT)));
        viewHolder.container.setTag(R.id.key_item_page_holder, pageHolder);
        viewHolder.container.setTag(R.id.key_item_page, page);



        viewHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                int position = ((PageHolder)v.getTag(R.id.key_item_holder)).getAdapterPosition();
//                pages.remove(position);
//                notifyItemRemoved(position);
                return false;
            }
        });

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPageClickListener != null){
                    onPageClickListener.onPageClick((Page)v.getTag(R.id.key_item_page));
                }

            }
        });

        viewHolder.notebookIcon.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(Notebook.COLUMN_NOTEBOOK_COLOR)));
        viewHolder.notebookName.setText(cursor.getString(cursor.getColumnIndex(Notebook.COLUMN_TITLE)));
        viewHolder.notebookName.setTextColor(cursor.getInt(cursor.getColumnIndex(Notebook.COLUMN_TITLE_COLOR)));

        int noteId = cursor.getInt(cursor.getColumnIndex(Notebook.COLUMN_NOTEBOOK_ID));
        viewHolder.container.setTag(noteId);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotebookClickListener.onNotebookClick(Long.parseLong(v.getTag().toString()));
            }
        });
    }

    @Override
    public PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public class PageHolder extends RecyclerView.ViewHolder{
        //Button pageIcon;
        TextView pageName;
        TextView pageText;
        View container;
        public PageHolder (View itemView) {
            super(itemView);
            // pageIcon = (Button) itemView.findViewById(R.id.pageIcon);
            pageName = (TextView) itemView.findViewById(R.id.pageName);
            pageText = (TextView) itemView.findViewById(R.id.pageText);
            container = itemView.findViewById(R.id.container);

        }
    }
    private PageAdapter.OnPageClickListener onPageClickListener;

    public void setOnPageClickListener(PageAdapter.OnPageClickListener onPageClickListener){
        this.onPageClickListener = onPageClickListener;
    }

    public interface OnPageClickListener{
        public void onPageClick(Page page);
    }
}
