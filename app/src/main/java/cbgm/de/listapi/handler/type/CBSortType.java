package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBListView;
import cbgm.de.listapi.listener.ICBActionNotifier;


public class CBSortType<I> extends CBTouchType<I> {
    //tells if there is a long press on a list item
    private boolean isDragActive = false;
    //the current position which describes the item
    private int pos;
    //the always updated position
    private int updatedPos;

    CBSortType(List<I> sequenceList, CBAdapter<I> baseAdapter, CBListView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    private Runnable draggingRunnable = new Runnable() {
        @SuppressWarnings("unchecked")
        public void run() {
            Log.d("LIST API", "Item is clicked for sorting");
            isDragActive = true;
            adapter.setSingleSelectedItem(pos, true);
        }
    };

    @Override
    public void onInitialDown(MotionEvent motionEvent) {

        if (isMotionOutside(motionEvent, null))
            return;
        View view = this.listContainer.findChildViewUnder((int) motionEvent.getX(), (int) motionEvent.getY());
        this.pos = this.listContainer.getChildAdapterPosition(view);

        if (this.pos >= 0) {
            this.longPressHandler.postDelayed(this.draggingRunnable, 600);
        }
        super.onInitialDown(motionEvent);
    }

    @Override
    public void onUp(MotionEvent e) {

        if (this.isDragActive) {
            Log.d("LIST API", "Item released and sorted");
            cleanTouch();
            this.actionNotifier.sortAction(this.data);
        } else {
            this.longPressHandler.removeCallbacks(this.draggingRunnable);
        }
    }

    @Override
    public void cleanTouch() {
        this.adapter.setSingleSelectedItem(pos, false);
        this.isDragActive = false;
    }

    @Override
    public void onMove(MotionEvent motionEvent) {

        if (isMotionOutside(motionEvent, null))
            return;


        if (this.isDragActive) {
            Log.d("LIST API", "Item moving");
            int toY = (int) motionEvent.getY();
            int toX = (int) motionEvent.getX();
            View view = this.listContainer.findChildViewUnder(toX, toY);
            int curPos = this.listContainer.getChildAdapterPosition(view);

            if (this.pos != -1 && curPos != -1 && this.pos!= curPos) {
                Log.d("LIST API", "Item moved");
                this.adapter.swapItems(this.pos, curPos);
                this.pos = curPos;
            } else {
                this.adapter.setSingleSelectedItem(curPos, true);
            }
            this.updatedPos = curPos;
            scrollList();
        }
    }

    private void scrollList() {
        CBListView.CBLayoutManager layoutManager = (CBListView.CBLayoutManager)this.listContainer.getLayoutManager();

        if (this.updatedPos == layoutManager.findLastVisibleItemPosition())
            this.listContainer.scrollToPosition(layoutManager.findLastVisibleItemPosition() + 1);

        if (this.updatedPos == layoutManager.findFirstVisibleItemPosition() && this.updatedPos != 0)
            this.listContainer.scrollToPosition(layoutManager.findFirstVisibleItemPosition() - 1);
    }
}
