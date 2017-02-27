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
    /* The item which is dragged */
    private E dragedSequence = null;
    /* Tells if the handler which identifies as selection is successful  */
    private boolean isLongPressHandlerActivated = false;
    /* The list of items */
    private List<E> sequenceList;
    /* The position which is moved */
    private int pos;
    /* The listener to pass the new sorted list */
    private IListMenuListener IListMenuListener;
    /* The container of list */
    private ListView listContainer;
    /* The list adapter */
    private T adapter;
    /* The handler which identifies a selection */
    private final Handler longPressHandler = new Handler();
    /* The application context */
    private Context context;

    public DragListener(final List<E> sequenceList, T baseAdapter, ListView listContainer, Context context) {
        this.sequenceList = sequenceList;
        this.adapter = baseAdapter;
        this.listContainer = listContainer;
        this.context = context;
    }

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
                    int x = -1;
                    for(int i = 0; i < this.sequenceList.size(); i++) {
                        if(this.sequenceList.get(i).equals(switchedSequence)) {
                            x = i;
                            break;
                        }
                    }
                    //switch moving numbers

                    if (arFromPos != arToPos) {
                        Log.d("LIST API", "Sorting from: " + arFromPos + "  to: " + arToPos);
                        //swap elements
                        Collections.swap(this.sequenceList, arFromPos, arToPos);
                        this.adapter.setItemToHighlight(this.pos);
                        this.adapter.reInit(this.sequenceList, true);
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
        return this.isLongPressHandlerActivated ? true : false;
    }


    public void setSortListener(IListMenuListener IListMenuListener) {
        this.IListMenuListener = IListMenuListener;
    }

    /**
     * Method to clean up touch events
     */
    private void cleanTouch() {
        this.longPressHandler.removeCallbacks(this.longPressedRunnable);
        this.isLongPressHandlerActivated = false;
        this.adapter.setItemToHighlight(-1);
        this.adapter.reInit(this.sequenceList, true);
    }

    /**
     * Runnable which is used to identify a selection of an item.
     */
    private Runnable longPressedRunnable = new Runnable() {
        public void run() {
            isLongPressHandlerActivated = true;
            adapter.setItemToHighlight(pos);
            adapter.reInit(sequenceList, true);

            AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

            if(am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE || am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
                vibrator.vibrate(40);
            } else if (1 == Settings.System.getInt(context.getContentResolver(), "vibrate_when_ringing", 0)) {
                vibrator.vibrate(40);
            }
            Log.d("LIST API", "Item ready to move");
        }
    };
}
