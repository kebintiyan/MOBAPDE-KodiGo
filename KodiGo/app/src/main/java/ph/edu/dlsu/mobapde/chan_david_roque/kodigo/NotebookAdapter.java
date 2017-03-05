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
    public void onBindViewHolder(NotebookHolder notebookHolder, int position) {
        Notebook notebook = notebooks.get(position);
        //notebookHolder.notebookIcon.setText(notebook.getTitle());
        //notebookHolder.ivGenre.setImageResource(notebook.getResourceId());
        notebookHolder.notebookIcon.setBackgroundColor(notebook.getNotebookColor());
        notebookHolder.notebookName.setTextColor(notebook.getTitleColor());
        notebookHolder.notebookName.setText(notebook.getTitle());
        notebookHolder.container.setTag(R.id.key_item_notebook_position, position);
        notebookHolder.container.setTag(R.id.key_item_notebook, notebook);

        notebookHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                int position = ((NotebookHolder)v.getTag(R.id.key_item_holder)).getAdapterPosition();
//                notebooks.remove(position);
//                notifyItemRemoved(position);
                return false;
            }
        });

        notebookHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NotebookAdapter", "hi");
                Notebook n = (Notebook) v.getTag(R.id.key_item_notebook);
                int position = (int) v.getTag(R.id.key_item_notebook_position);
                onItemClickListener.onItemClick(n, position);
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

    public void editNotebook(Notebook notebook, int position){
        notebooks.set(position,notebook);
        notifyItemChanged(position);
    }

    public void deleteNotebook(int position){

        notebooks.remove(position);
        notifyItemRemoved(position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(Notebook notebook, int position);
    }
}
