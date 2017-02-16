package cbgm.de.listapi.listener;


import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cbgm.de.listapi.data.CBAdapter;
import cbgm.de.listapi.data.CBListViewItem;


/**
 * Listener for handling touch events of list items
 * - handles sorting behaviour
 * @author Christian Bergmann
 */

public class DragListener<E extends CBListViewItem, T extends CBAdapter> implements View.OnTouchListener {
    private E dragedSequence = null;
    private boolean isLongPressHandlerActivated = false;
    private List<E> sequenceList;
    private int pos;
    private IListMenuListener IListMenuListener;
    private ListView listContainer;
    private T adapter;
    private final Handler longPressHandler = new Handler();

    public DragListener(final List<E> sequenceList, T baseAdapter, ListView listContainer) {
        this.sequenceList = sequenceList;
        this.adapter = baseAdapter;
        this.listContainer = listContainer;
    }


    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        int movingNumber;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.longPressHandler.postDelayed(this.longPressedRunnable, 1000);
                this.pos = this.listContainer.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());

                if (this.pos < 0) {
                    return true;
                }
                this.dragedSequence = (E) this.listContainer.getAdapter().getItem(this.pos);
                movingNumber = this.dragedSequence.getItem().getSequenceNumber();
                Log.e("test","Position: " + movingNumber);
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
                    int x = -1;
                    for(int i = 0; i < this.sequenceList.size(); i++) {
                        if(this.sequenceList.get(i).equals(switchedSequence)) {
                            x = i;
                            break;
                        }
                    }
                    //switch moving numbers
                    int movingFrom = this.sequenceList.get(arFromPos).getItem().getSequenceNumber();
                    int movingTo = this.sequenceList.get(arToPos).getItem().getSequenceNumber();
                    if (movingFrom != movingTo && arFromPos != arToPos) {
                        this.sequenceList.get(arFromPos).getItem().setSequenceNumber(movingTo);
                        this.sequenceList.get(arToPos).getItem().setSequenceNumber(movingFrom);
                        Log.e("test","from: " + arFromPos + "  to: " + arToPos);
                        //swap elements
                        Collections.swap(this.sequenceList, arFromPos, arToPos);
                        this.adapter.setItemToHighlight(this.pos);
                        this.adapter.reInit(this.sequenceList, true);
                    }

                }

                break;
            case MotionEvent.ACTION_UP:
                if (this.isLongPressHandlerActivated) {
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
        return this.isLongPressHandlerActivated ? true : false;
    }


    public void setSortListener(IListMenuListener IListMenuListener) {
        this.IListMenuListener = IListMenuListener;
    }

    private void cleanTouch() {
        this.longPressHandler.removeCallbacks(this.longPressedRunnable);
        this.isLongPressHandlerActivated = false;
        this.adapter.setItemToHighlight(-1);
        this.adapter.reInit(this.sequenceList, true);
    }

    private Runnable longPressedRunnable = new Runnable() {
        public void run() {
            isLongPressHandlerActivated = true;
            adapter.setItemToHighlight(pos);
            adapter.reInit(sequenceList, true);
        }
    };
}
