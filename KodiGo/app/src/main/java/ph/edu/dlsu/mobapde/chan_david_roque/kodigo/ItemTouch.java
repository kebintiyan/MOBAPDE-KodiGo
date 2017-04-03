package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ddavid on 3/3/2017.
 */

public class ItemTouch{

    ItemTouchHelper ith;
    CursorRecyclerViewAdapter adapter;
    ArrayList arrayList;
    boolean isAnimated = false;
    boolean animating = false;

    ItemTouch(CursorRecyclerViewAdapter adapter, ArrayList arrayList) {
        this.adapter = adapter;

        this.arrayList = arrayList;
        createItemTouchCallback();
        ith = new ItemTouchHelper(createItemTouchCallback());
    }

    ItemTouchHelper.ViewDropHandler viewDropHandler() {
        return new ItemTouchHelper.ViewDropHandler() {
            @Override
            public void prepareForDrop(View view, View target, int x, int y) {
                Log.i("World", "helo");
            }
        };
    }
    // Extend the Callback class
    ItemTouchHelper.Callback createItemTouchCallback() {
        return new ItemTouchHelper.Callback() {

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Log.i("MAGIC", "Mike");
                onItemClearViewListener.onItemClearView(recyclerView, viewHolder);
                isAnimated = false;
                animating = false;
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if(isAnimated && !animating) {
                    Log.i("ANIM", "ate");
                    onItemLongClickListener.onItemLongClick(viewHolder);
                    animating = true;
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                if(!isAnimated)
                    isAnimated = true;
                //onItemLongClickListener.onItemLongClick(adapter);
                return super.isLongPressDragEnabled();
            }

            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them

                Collections.swap(arrayList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                onItemMoveListener.onItemMoveClick(arrayList);

                return false;
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

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        this.onItemMoveListener = onItemMoveListener;
    }

    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener{
        public void onItemLongClick(RecyclerView.ViewHolder viewHolder);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    private OnItemClearViewListener onItemClearViewListener;

    public interface OnItemClearViewListener{
        public void onItemClearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }

    public void setOnItemClearViewListener(OnItemClearViewListener onItemClearViewListener) {
        this.onItemClearViewListener = onItemClearViewListener;
    }

}
