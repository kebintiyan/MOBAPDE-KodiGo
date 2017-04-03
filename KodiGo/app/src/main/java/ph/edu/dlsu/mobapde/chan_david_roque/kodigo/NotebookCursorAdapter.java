package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.R.id.container;

/**
 * Created by ddavid on 3/7/2017.
 */

public class NotebookCursorAdapter extends CursorRecyclerViewAdapter<NotebookCursorAdapter.NotebookHolder> {

    public boolean isLongPressed;
    public NotebookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(NotebookHolder viewHolder, Cursor cursor) {

        viewHolder.notebookIcon.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(Notebook.COLUMN_NOTEBOOK_COLOR)));
        viewHolder.notebookName.setText(cursor.getString(cursor.getColumnIndex(Notebook.COLUMN_TITLE)));
        viewHolder.notebookName.setTextColor(cursor.getInt(cursor.getColumnIndex(Notebook.COLUMN_TITLE_COLOR)));

        int noteId = cursor.getInt(cursor.getColumnIndex(Notebook.COLUMN_NOTEBOOK_ID));
        viewHolder.container.setTag(R.id.key_item_notebook_id, noteId);
        viewHolder.container.setTag(R.id.key_item_notebook, viewHolder.container);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotebookClickListener.onNotebookClick(Long.parseLong(v.getTag(R.id.key_item_notebook_id).toString()));
            }
        });

        /*viewHolder.container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v = (View) v.getTag(R.id.key_item_notebook);
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_UP:
                        Log.i("IAMHERE", "HEREIAM");
                        if(isLongPressed) {
                            v.setAnimation(null);
                            isLongPressed = false;
                            return true;
                        }

                }
                return false;
            }
        });*/

        viewHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override

            public boolean onLongClick(View v) {
                isLongPressed = true;
                v = (View) v.getTag(R.id.key_item_notebook);
                //onNotebookLongClickListener.onNotebookLongClick(v);

                return true;
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
        CardView notebookIcon;
        TextView notebookName;
        View container;
        public NotebookHolder (View itemView) {
            super(itemView);
            //notebookIcon = (Button) itemView.findViewById(R.id.notebookIcon);
            notebookIcon = (CardView) itemView.findViewById(R.id.notebookIcon);
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

    private NotebookCursorAdapter.OnNotebookLongClickListener onNotebookLongClickListener;

    public interface OnNotebookLongClickListener{
        public void onNotebookLongClick(View v);
    }

    public void setOnNotebookLongClickListener(NotebookCursorAdapter.OnNotebookLongClickListener onNotebookLongClickListener) {
        this.onNotebookLongClickListener = onNotebookLongClickListener;
    }

    private NotebookCursorAdapter.OnNotebookReleaseListener onNotebookReleaseListener;

    public interface OnNotebookReleaseListener{
        public void onNotebookRelease(View v);
    }

    public void setOnNotebookReleaseListener(NotebookCursorAdapter.OnNotebookReleaseListener onNotebookReleaseListener) {
        this.onNotebookReleaseListener = onNotebookReleaseListener;
    }

}
