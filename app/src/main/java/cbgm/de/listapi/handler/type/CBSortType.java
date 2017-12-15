package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collections;
import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBViewHolder;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Class to handle the touch type sort.
 * @author Christian Bergmann
 */

public class CBSortType<H extends CBViewHolder<I>, I> extends CBTouchType<H, I> {
    //tells if there is a long press on a list item
    private boolean isLongPress = false;
    //the current position which describes the item
    private int pos;
    //the view holder the list element relies on
    private CBViewHolder holder;
    //the y coordinate where we move from
    private int fromY;

    CBSortType(List<I> sequenceList, CBAdapter<H, I> baseAdapter, RecyclerView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    private Runnable draggingRunnable = new Runnable() {
        @SuppressWarnings("unchecked")
        public void run() {
            Log.d("LIST API", "Item is clicked for sorting");
            isLongPress = true;
            modeHelper.setSelectedPosition(pos);

            if (((ColorDrawable)holder.getFrontItem().getBackground()).getColor() == Color.WHITE) {
                holder.getFrontItem().setBackgroundColor(modeHelper.getSelectColor());
            } else {
                holder.getFrontItem().setBackgroundColor(Color.WHITE);
            }
        }
    };

    @Override
    public void onInitialDown(MotionEvent motionEvent) {

        if (isMotionOutside(motionEvent, null))
            return;
        View view = this.listContainer.findChildViewUnder((int) motionEvent.getX(), (int) motionEvent.getY());
        this.pos = this.listContainer.getChildAdapterPosition(view);

        if (this.pos >= 0) {
            this.longPressHandler.postDelayed(this.draggingRunnable, 400);
            this.fromY = (int)motionEvent.getY();
            this.holder = (CBViewHolder) view.getTag();
        }
        super.onInitialDown(motionEvent);
    }

    @Override
    public void onUp(MotionEvent e) {

        if (this.isLongPress) {
            Log.d("LIST API", "Item released and sorted");
            cleanTouch();
            this.actionNotifier.sortAction(this.data);
        } else {
            this.longPressHandler.removeCallbacks(this.draggingRunnable);
        }
    }

    @Override
    public void cleanTouch() {
        this.modeHelper.setSelectedPosition(-1);
        this.isLongPress = false;
        this.adapter.init(this.data);
    }

    @Override
    public void onMove(MotionEvent motionEvent) {

        if (isMotionOutside(motionEvent, null))
            return;

        if (this.isLongPress) {
            Log.d("LIST API", "Item moving");
            int toY = (int) motionEvent.getY();
            View view = this.listContainer.findChildViewUnder((int) motionEvent.getX(), (int) motionEvent.getY());
            int changedPos = this.listContainer.getChildAdapterPosition(view);
            int scrollPos = this.fromY > toY ? this.pos -1 : this.pos +1;
            Log.e("test", "from "+ this.pos + " to "+ scrollPos);
            if (this.pos != -1 && changedPos != -1) {
                this.fromY = toY;
                Collections.swap(this.data, this.pos, changedPos);
            }
            this.pos = changedPos;
            this.modeHelper.setSelectedPosition(this.pos);
            this.adapter.notifyDataSetChanged();
            try {
                this.listContainer.smoothScrollToPosition(scrollPos);

            } catch (Exception ex){
                Log.e("LIST_API" ,"scroll error");
            }
        }
    }
}
