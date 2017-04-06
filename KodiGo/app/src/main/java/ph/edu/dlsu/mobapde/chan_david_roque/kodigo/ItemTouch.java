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

    // Extend the Callback class
    ItemTouchHelper.Callback createItemTouchCallback() {
        return new ItemTouchHelper.Callback() {

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                Log.i("Function", "clearView");

                onItemClearViewListener.onItemClearView(arrayList, viewHolder);
                isAnimated = false;
                animating = false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if(isAnimated && !animating) {
                    onItemLongClickListener.onItemLongClick(viewHolder);
                    animating = true;
                }
            }

            @Override
            public boolean isLongPressDragEnabled() {
                if(!isAnimated)
                    isAnimated = true;
                Log.i("Function", "isLongpress");
                return super.isLongPressDragEnabled();
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.i("Function", "onSwiped");
            }

            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Log.i("Function", "onMove");

                //Collections.swap(arrayList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.i("initial", viewHolder.getAdapterPosition()+"");
                Log.i("Target", target.getAdapterPosition()+"");
                arrayList.add(target.getAdapterPosition(), arrayList.remove(viewHolder.getAdapterPosition()));
                // and notify the adapter that its dataset has changed
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                onItemMoveListener.onItemMoveClick(arrayList);

                return false;
            }


            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Log.i("Function", "getMovementFlags");
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
        public void onItemClearView(ArrayList arrayList, RecyclerView.ViewHolder viewHolder);
    }

    public void setOnItemClearViewListener(OnItemClearViewListener onItemClearViewListener) {
        this.onItemClearViewListener = onItemClearViewListener;
    }
}
