package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        ImageView notebookIcon;
        TextView notebookName;
        View container;
        public NotebookHolder (View itemView) {
            super(itemView);
            //notebookIcon = (Button) itemView.findViewById(R.id.notebookIcon);
            notebookIcon = (ImageView) itemView.findViewById(R.id.notebookIcon);
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

        //notebookHolder.notebookIcon.setText(notebook.getTitle());
        //notebookHolder.ivGenre.setImageResource(notebook.getResourceId());
        notebookHolder.notebookIcon.setBackgroundColor(Color.BLUE);
        notebookHolder.notebookName.setText(notebook.getTitle());
        notebookHolder.container.setTag(R.id.key_item_notebook_holder, notebookHolder);
        notebookHolder.container.setTag(R.id.key_item_notebook, notebook);
        //Notebook n2 = (Notebook) notebookHolder.container.getTag(R.id.key_item_notebook);
        //Log.i("loadaz", n2.getTitle());
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
                Log.i("onclick", "yes");
                Notebook n = (Notebook)v.getTag(R.id.key_item_notebook);
                if(onNotebookClickListener != null){
                    Log.i("onclicknull", "no");
                    Log.i("currnb", n.getTitle());

                    onNotebookClickListener.onNotebookClick(v, n);
                }
                Log.i("onclicknull", "yes");

                /*Notebook n = (Notebook) v.getTag(R.id.key_item_notebook);
                Log.d("NotebookAdapterName", n.getTitle());
//                v.getContext().startActivity(new Intent(v.getContext(),ViewNotebookActivity.class)
//                        .putExtra(MainActivity.KEY_TITLE, s.getTitle())
//                        .putExtra(MainActivity.KEY_LYRICS, s.getLyrics()));
                v.getContext().startActivity(new Intent(v.getContext(), LoadPagesActivity.class)
                        .putExtra(MainActivity.KEY_NOTEBOOK, n));*/
            }
        });

        notebookHolder.notebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onclick", "yes");
                Notebook n = notebooks.get(position);
                if(onNotebookClickListener != null){
                    Log.i("onclicknull", "no");
                    Log.i("currnb", n.getTitle());

                    onNotebookClickListener.onNotebookClick(v, n);
                }
                Log.i("onclicknull", "yes");
                /*
                Log.d("NotebookAdapter", "hi pos=" + position);
                Notebook n = (Notebook) v.getTag(R.id.key_item_notebook);
                Log.d("NotebookAdapterName", n.getTitle());
//                v.getContext().startActivity(new Intent(v.getContext(),ViewNotebookActivity.class)
//                        .putExtra(MainActivity.KEY_TITLE, s.getTitle())
//                        .putExtra(MainActivity.KEY_LYRICS, s.getLyrics()));
                v.getContext().startActivity(new Intent(v.getContext(), LoadPagesActivity.class)
                        .putExtra(MainActivity.KEY_NOTEBOOK, n));*/
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

    private OnNotebookClickListener onNotebookClickListener;

    public interface OnNotebookClickListener{
        public void onNotebookClick(View view,  Notebook n);
    }

    public OnNotebookClickListener getOnNotebookClickListener() {
        return onNotebookClickListener;
    }

    public void setOnNotebookClickListener(OnNotebookClickListener onNotebookClickListener) {
        this.onNotebookClickListener = onNotebookClickListener;
    }
}
