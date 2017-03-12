package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER on 2/25/2017.
 */

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.NotebookHolder>{

    ArrayList<Notebook> notebooks;

    public NotebookAdapter(ArrayList<Notebook> notebooks){
        this.notebooks = notebooks;
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
    @Override
    public NotebookHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_notebook, null);

        return new NotebookHolder(v);
    }

    @Override
    public void onBindViewHolder(NotebookHolder notebookHolder, final int position) {
        final Notebook notebook = notebooks.get(position);

        notebookHolder.notebookIcon.setBackgroundColor(notebook.getNotebookColor());
        notebookHolder.notebookName.setTextColor(notebook.getTitleColor());
        notebookHolder.notebookName.setText(notebook.getTitle());
        notebookHolder.container.setTag(R.id.key_item_notebook_id, notebook.getNotebookID());

        notebookHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (long) v.getTag(R.id.key_item_notebook_id);
                onNotebookClickListener.onNotebookClick(id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.notebooks.size();
    }

    public void addNotebook(Notebook notebook){
        notebooks.add(notebook);
        notifyItemInserted(getItemCount()-1);
    }

    public void editNotebook(Notebook notebook){
        notebooks.set(notebook.getNotebookNumber(), notebook);
        notifyItemChanged(notebook.getNotebookNumber());
    }

    public void deleteNotebook(int position){

        notebooks.remove(position);
        notifyItemRemoved(position);

    }

    private OnNotebookClickListener onNotebookClickListener;

    public interface OnNotebookClickListener{
        public void onNotebookClick(long notebookId);
    }

    public OnNotebookClickListener getOnNotebookClickListener() {
        return onNotebookClickListener;
    }

    public void setOnNotebookClickListener(OnNotebookClickListener onNotebookClickListener) {
        this.onNotebookClickListener = onNotebookClickListener;
    }
}
