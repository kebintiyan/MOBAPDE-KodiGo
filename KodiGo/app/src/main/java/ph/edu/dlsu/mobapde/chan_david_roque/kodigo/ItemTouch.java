package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ddavid on 3/3/2017.
 */

public class ItemTouch{

    ItemTouchHelper ith;
    RecyclerView.Adapter adapter;
    ArrayList arrayList;

    ItemTouch(RecyclerView.Adapter adapter, ArrayList arrayList) {
        this.adapter = adapter;
        this.arrayList = arrayList;
        createItemTouchCallback();
        ith = new ItemTouchHelper(createItemTouchCallback());
    }
    // Extend the Callback class

    ItemTouchHelper.Callback createItemTouchCallback() {
        return new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them



                Collections.swap(arrayList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                onItemMoveListener.onItemMoveClick(arrayList);

                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        ith.attachToRecyclerView(recyclerView);
    }

    private OnItemMoveListener onItemMoveListener;

    public interface OnItemMoveListener{
        public void onItemMoveClick(ArrayList arrayList);
    }

    public OnItemMoveListener getOnNotebookClickListener() {
        return onItemMoveListener;
    }

    public void setOnNotebookClickListener(OnItemMoveListener onItemMoveListener) {
        this.onItemMoveListener = onItemMoveListener;
    }
}
