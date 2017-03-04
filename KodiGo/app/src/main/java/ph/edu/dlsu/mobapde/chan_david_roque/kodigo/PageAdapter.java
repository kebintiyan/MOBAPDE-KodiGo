package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageHolder>{

    ArrayList<Page> pages;

    final static int REQUEST_ADD_PAGE = 0;
    final static String KEY_PAGE = "page";

    public PageAdapter(ArrayList<Page> pages){
        this.pages = pages;
    }

    public class PageHolder extends RecyclerView.ViewHolder{
        Button pageIcon;
        View container;
        public PageHolder (View itemView) {
            super(itemView);
            pageIcon = (Button) itemView.findViewById(R.id.pageIcon);
            container = itemView.findViewById(R.id.container);

        }
    }
    @Override
    public PageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_page, null);

        return new PageHolder(v);
    }

    @Override
    public void onBindViewHolder(PageHolder pageHolder, int position) {
        Page page = pages.get(position);
        pageHolder.pageIcon.setText(page.getName());
        //pageHolder.ivGenre.setImageResource(page.getResourceId());
        pageHolder.container.setTag(R.id.key_item_page_holder, pageHolder);
        pageHolder.container.setTag(R.id.key_item_page, page);

        pageHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                int position = ((PageHolder)v.getTag(R.id.key_item_holder)).getAdapterPosition();
//                pages.remove(position);
//                notifyItemRemoved(position);
                return false;
            }
        });

        pageHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Page s = (Page) v.getTag(R.id.key_item_page);
////                v.getContext().startActivity(new Intent(v.getContext(),ViewPageActivity.class)
////                        .putExtra(MainActivity.KEY_TITLE, s.getTitle())
////                        .putExtra(MainActivity.KEY_LYRICS, s.getLyrics()));
//                v.getContext().startActivity(new Intent(v.getContext(), ViewPageActivity.class)
//                        .putExtra(MainActivity.KEY_PAGE, s));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.pages.size();
    }

    public void addPage(Page page){
        pages.add(page);
        notifyItemInserted(getItemCount()-1);
    }
}
