package cbgm.de.listapi.listener;


import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import cbgm.de.listapi.data.CBAdapter;
import cbgm.de.listapi.data.CBListViewItem;


/**
 * Listener for handling touch events of list items
 * - handles sorting behaviour
 * @author Christian Bergmann
 */

public class SelectListener<E extends CBListViewItem, T extends CBAdapter> implements View.OnTouchListener {
    /* The item which is dragged */
    private E selectedItem = null;
    /* The list of items */
    private List<E> itemList;
    /* The position which is selected */
    private int pos;
    /* The listener to pass the new sorted list */
    private cbgm.de.listapi.listener.IListMenuListener IListMenuListener;
    /* The container of list */
    private ListView listContainer;
    /* The list adapter */
    private T adapter;
    /* The application context */
    private Context context;

    public SelectListener(final List<E> itemList, T baseAdapter, ListView listContainer, Context context) {
        this.itemList = itemList;
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
                this.pos = this.listContainer.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());

                if (this.pos < 0) {
                    return true;
                }
                this.selectedItem = (E) this.listContainer.getAdapter().getItem(this.pos);
                Log.d("LIST API", "Item is clicked for selecting");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("LIST API", "Item released and sorted");
                this.IListMenuListener.handleSelect(this.selectedItem);
                break;
        }
        return true;
    }


    public void setDeleteListener(cbgm.de.listapi.listener.IListMenuListener IListMenuListener) {
        this.IListMenuListener = IListMenuListener;
    }
}
