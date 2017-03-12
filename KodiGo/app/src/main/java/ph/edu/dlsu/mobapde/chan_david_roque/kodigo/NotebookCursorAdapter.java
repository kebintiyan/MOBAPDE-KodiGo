package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ddavid on 3/7/2017.
 */

public class NotebookCursorAdapter extends CursorRecyclerViewAdapter<NotebookCursorAdapter.NotebookHolder> {

    public NotebookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(NotebookHolder viewHolder, Cursor cursor) {

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
    public NotebookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notebook, parent, false);
        return new NotebookHolder(v);
    }

    public class NotebookHolder extends RecyclerView.ViewHolder{
        //Button notebookIcon;
        RelativeLayout notebookIcon;
        TextView notebookName;
        View container;
        public NotebookHolder (View itemView) {
            super(itemView);
            //notebookIcon = (Button) itemView.findViewById(R.id.notebookIcon);
            notebookIcon = (RelativeLayout) itemView.findViewById(R.id.notebookIcon);
            notebookName = (TextView) itemView.findViewById(R.id.notebookName);
            container = itemView.findViewById(R.id.container);

        }
    }

    private NotebookCursorAdapter.OnNotebookClickListener onNotebookClickListener;

    public interface OnNotebookClickListener{
        public void onNotebookClick(long notebookId);
    }

    public NotebookCursorAdapter.OnNotebookClickListener getOnNotebookClickListener() {
        return onNotebookClickListener;
    }

    public void setOnNotebookClickListener(NotebookCursorAdapter.OnNotebookClickListener onNotebookClickListener) {
        this.onNotebookClickListener = onNotebookClickListener;
    }
}
