package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBListViewItem;
import cbgm.de.listapi.basic.CBViewHolder;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Class to handle the touch type sort.
 * @author Christian Bergmann
 */

public class CBSortType extends CBTouchType {
    //tells if there is a long press on a list item
    private boolean isLongPress = false;
    //the current position which describes the item
    private int pos;
    //the view holder the list element relies on
    private CBViewHolder holder;
    //the y coordinate where we move from
    private int fromY;

    public CBSortType(List<CBListViewItem> sequenceList, CBAdapter baseAdapter, ListView listContainer, ICBActionNotifier actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    private Runnable draggingRunnable = new Runnable() {
        @SuppressWarnings("unchecked")
        public void run() {
            Log.d("LIST API", "Item is clicked for sorting");
            isLongPress = true;
            modeHelper.setSelectedPosition(pos);

            if (((ColorDrawable)holder.item.getBackground()).getColor() == Color.WHITE) {
                holder.item.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.item.setBackgroundColor(Color.WHITE);
            }
        }
    };

    @Override
    public void onInitialDown(MotionEvent motionEvent) {
        this.pos = this.listContainer.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());

        if (this.pos >= 0) {
            this.longPressHandler.postDelayed(this.draggingRunnable, 400);
            this.fromY = (int)motionEvent.getY();
            this.holder = ((CBListViewItem)listContainer.getAdapter().getItem(this.pos)).getHolder();
        }
        super.onInitialDown(motionEvent);
    }

    @Override
    public void onUp(MotionEvent e) {

        if (this.isLongPress) {
            Log.d("LIST API", "Item released and sorted");
            cleanTouch();
            this.actionNotifier.sortAction(this.sequenceList);
        } else {
            this.longPressHandler.removeCallbacks(this.draggingRunnable);
        }
    }

    @Override
    public void cleanTouch() {
        this.modeHelper.setSelectedPosition(-1);
        this.isLongPress = false;
        this.adapter.init(this.sequenceList);
    }

    @Override
    public void onMove(MotionEvent e) {

        if (this.isLongPress) {
            Log.d("LIST API", "Item moving");
            int toX = (int) e.getX();
            int toY = (int) e.getY();
            int changedPos = this.listContainer.pointToPosition(toX, toY);
            int scrollPos = this.fromY > toY ? this.pos -1 : this.pos +1;
            Log.e("test", "from "+ this.pos + " to "+ scrollPos);
            if (this.pos != -1 && changedPos != -1) {
                this.fromY = toY;
                Collections.swap(this.sequenceList, this.pos, changedPos);
            }
            this.pos = changedPos;
            this.modeHelper.setSelectedPosition(this.pos);
            this.adapter.notifyDataSetChanged();
            this.listContainer.smoothScrollToPosition(scrollPos);
        }
    }
}
