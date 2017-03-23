package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
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

    public PageCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(PageHolder viewHolder, Cursor cursor) {

        viewHolder.pageName.setText(cursor.getString(cursor.getColumnIndex(Page.COLUMN_NAME)));
        viewHolder.pageText.setText(cursor.getString(cursor.getColumnIndex(Page.COLUMN_TEXT)));
        int pageId = cursor.getInt(cursor.getColumnIndex(Page.COLUMN_PAGE_ID));
        viewHolder.container.setTag(pageId);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPageClickListener != null){
                    onPageClickListener.onPageClick(Long.parseLong(v.getTag().toString()));
                }

            }
        });

        viewHolder.pageText.setTag(pageId);
        viewHolder.pageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPageClickListener != null){
                    onPageClickListener.onPageClick(Long.parseLong(v.getTag().toString()));
                }
            }
        });

    }

    @Override
    public PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_page, parent, false);
        return new PageCursorAdapter.PageHolder(v);
    }

    public class PageHolder extends RecyclerView.ViewHolder{
        //Button pageIcon;
        TextView pageName;
        HTMLTextView pageText;
        View container;
        public PageHolder (View itemView) {
            super(itemView);
            // pageIcon = (Button) itemView.findViewById(R.id.pageIcon);
            pageName = (TextView) itemView.findViewById(R.id.pageName);
            pageText = (HTMLTextView) itemView.findViewById(R.id.pageText);
            container = itemView.findViewById(R.id.container);

        }
    }
    private PageCursorAdapter.OnPageClickListener onPageClickListener;

    public void setOnPageClickListener(PageCursorAdapter.OnPageClickListener onPageClickListener){
        this.onPageClickListener = onPageClickListener;
    }

    public interface OnPageClickListener{
        public void onPageClick(long pageId);
    }
}
