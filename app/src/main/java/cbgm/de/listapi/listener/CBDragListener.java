package cbgm.de.listapi.listener;


import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import cbgm.de.listapi.data.CBAdapter;
import cbgm.de.listapi.data.CBListViewItem;


/**
 * Listener for handling touch events of list items
 * - handles sorting behaviour
 * @author Christian Bergmann
 */

public class CBDragListener<E extends CBListViewItem, T extends CBAdapter> implements View.OnTouchListener {
    /* The item which is dragged */
    private E dragedSequence = null;
    /* Tells if the handler which identifies as selection is successful  */
    private boolean isLongPressHandlerActivated = false;
    /* The list of items */
    private List<E> sequenceList;
    /* The position which is moved */
    private int pos;
    /* The listener to pass the new sorted list */
    private ICBActionNotifier IListMenuListener;
    /* The container of list */
    private ListView listContainer;
    /* The list adapter */
    private T adapter;
    /* The handler which identifies a selection */
    private final Handler longPressHandler = new Handler();
    /* The application context */
    private Context context;

    public CBDragListener(final List<E> sequenceList, T baseAdapter, ListView listContainer, Context context) {
        this.sequenceList = sequenceList;
        this.adapter = baseAdapter;
        this.listContainer = listContainer;
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {

            //When an item is clicked start handler to tell if sort is wanted and get the items current position for later use
            case MotionEvent.ACTION_DOWN:
                this.longPressHandler.postDelayed(this.longPressedRunnable, 1000);
                this.pos = this.listContainer.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());

                if (this.pos < 0) {
                    return true;
                }
                this.dragedSequence = (E) this.listContainer.getAdapter().getItem(this.pos);
                Log.d("LIST API", "Item is clicked for sorting");
                break;

            case MotionEvent.ACTION_MOVE:

                if (this.isLongPressHandlerActivated) {
                    int toX = (int) motionEvent.getX();
                    int toY = (int) motionEvent.getY();
                    this.pos = this.listContainer.pointToPosition(toX, toY);

                    if (this.pos < 0) {
                        return true;
                    }
                    int offset = toY < 0 ? this.pos - 1 : this.pos + 1;
                    listContainer.smoothScrollToPosition(offset);
                    E switchedSequence = (E) listContainer.getAdapter().getItem(this.pos);
                    int arFromPos = this.sequenceList.indexOf(this.dragedSequence);
                    int arToPos = this.sequenceList.indexOf(switchedSequence);
                    //switch moving numbers

                    if (arFromPos != arToPos) {
                        Log.d("LIST API", "Sorting from: " + arFromPos + "  to: " + arToPos);
                        //swap elements
                        Collections.swap(this.sequenceList, arFromPos, arToPos);
                        this.adapter.setItemToHighlight(this.pos);
                        this.adapter.init(this.sequenceList, CBListMode.SORT);
                    }

                }

                break;
            case MotionEvent.ACTION_UP:
                if (this.isLongPressHandlerActivated) {
                    Log.d("LIST API", "Item released and sorted");
                    cleanTouch();
                    this.IListMenuListener.handleSort(this.sequenceList);
                } else {
                    cleanTouch();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                cleanTouch();
                break;
        }
        return this.isLongPressHandlerActivated;
    }


    public void setSortListener(ICBActionNotifier IListMenuListener) {
        this.IListMenuListener = IListMenuListener;
    }

    /**
     * Method to clean up touch events
     */
    @SuppressWarnings("unchecked")
    private void cleanTouch() {
        this.longPressHandler.removeCallbacks(this.longPressedRunnable);
        this.isLongPressHandlerActivated = false;
        this.adapter.setItemToHighlight(-1);
        this.adapter.init(this.sequenceList, CBListMode.SORT);
    }

    /**
     * Runnable which is used to identify a selection of an item.
     */
    @SuppressWarnings("unchecked")
    private Runnable longPressedRunnable = new Runnable() {
        @SuppressWarnings("unchecked")
        public void run() {
            isLongPressHandlerActivated = true;
            adapter.setItemToHighlight(pos);
            adapter.init(sequenceList, CBListMode.SORT);
        }
    };
}