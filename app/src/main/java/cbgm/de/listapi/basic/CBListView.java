package cbgm.de.listapi.basic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cbgm.de.listapi.data.CBListMode;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.handler.CBTouchHandler;
import cbgm.de.listapi.listener.ICBActionDelegate;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Custom ListView to block scrolling action
 * @author Christian Bergmann
 */

public class CBListView<E extends CBListViewItem, T extends CBAdapter> extends ListView implements ICBActionNotifier<E> {
    /*Listener to forward list item click events*/
    protected ICBActionDelegate<E> deletegateListener;
    /*The list adapter*/
    protected T adapter;

    protected CBTouchHandler touchHandler;

    protected CBModeHelper modeHelper = CBModeHelper.getInstance();

    protected ArrayList<Boolean> selectedItems;

    public CBListView(Context context) {
        super(context);
    }

    public CBListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CBListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        final int action = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_MOVE) {
            return this.modeHelper.isSwipeActive() || super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

     public void setAdapter(T adapter) {
        this.adapter = adapter;
        this.adapter.setActionListener(this);
        super.setAdapter(this.adapter);
    }

    public ListAdapter getAdapter() {
        return (T)super.getAdapter();
    }

    @Override
    public void deleteAction(final Object o) {
        Log.d("LIST_API", "button delete clicked");
        this.touchHandler.informButtonClick();
        deletegateListener.delegateDeleteAction(o);
    }

    @Override
    public void editAction(final Object o) {
        Log.d("LIST_API", "button edit clicked");
        this.touchHandler.informButtonClick();
        deletegateListener.delegateEditAction(o);
    }

    @Override
    public void sortAction(List list) {
        Log.d("LIST_API", "item sorted");
        deletegateListener.delegateSortAction(list);
    }

    public final void handleSelect(final int position) {
        Log.d("LIST_API", "item selected");

        if (position != -1)
            this.selectedItems.set(position, !this.selectedItems.get(position));
        deletegateListener.delegateSelectAction(position);
    }

    @Override
    public void swipeAction() {
        deletegateListener.delegateSwipeAction();
    }

    @Override
    public void singleClickAction(final int position) {
        if (this.modeHelper.getListMode() == CBListMode.SELECT) {
            Log.d("LIST API", "selecting");
            handleSelect(position);
            Log.d("LIST API", "value " + this.selectedItems.get(position));
            Log.d("LIST API", "size " + getSelectedPositions().size());
            boolean hasSelection = false;

            for(Boolean value: this.selectedItems){
                if(value){
                    hasSelection = true;
                    break;
                }
            }

            if (!hasSelection) {
                this.modeHelper.setListMode(CBListMode.SWIPE);
                this.modeHelper.setSelectedPosition(-1);
                Log.d("LIST API", "off select");
            }
            return;
        }
        deletegateListener.delegateSingleClickAction(position);
    }

    @Override
    public void longClickAction(final int position) {
        //check if not in selection mode
        if (this.modeHelper.getListMode() != CBListMode.SELECT) {
            setSelectedItems();
            this.modeHelper.setListMode(CBListMode.SELECT);
            handleSelect(position);
        }
        deletegateListener.delegateLongClickAction(position);
    }

    /**
     * Method to initialize the selected items with a default value
     */
    protected void setSelectedItems(){
        this.selectedItems = new ArrayList<>(getAdapter().getCount());

        for(int i=0; i < getAdapter().getCount(); i++){
            this.selectedItems.add(false);
        }
    }

    /**
     * Method to get the selected positions of the tetruns for further processing
     * @return the positions
     */
    public List<Integer> getSelectedPositions(){
        List<Integer> positions = new ArrayList<>();

        for(int i=0; i < this.selectedItems.size(); i++){

            if (this.selectedItems.get(i)){
                positions.add(i);
            }
        }
        return positions;
    }

    /**
     * Method to initialize the list view
     * @param data the list items
     * @param adapter the adapter
     */
    public void init(final List<E> data, T adapter) {
        this.adapter = adapter;
        this.touchHandler = new CBTouchHandler(data, this.adapter, this, this, getContext());
        this.setOnTouchListener(touchHandler);
        this.adapter.init(data);
        setAdapter(this.adapter);
    }
    /**
     * Method to initialize the list view
     * @param data the list items
     */
    public void init(final List<E> data) {
        CBTouchHandler touchHandler = new CBTouchHandler(data, this.adapter, this, this, getContext());
        this.setOnTouchListener(touchHandler);
    }

    /**
     * Method to set the delegate listener for forwarding list item click events
     * @param delegateListener the listener
     */
    public void setDelegateListener(final ICBActionDelegate delegateListener) {
        this.deletegateListener = delegateListener;
    }

    /**
     * Method to get the touch handler (can bes used to clean up touch events)
     * @return the CBTouchHandler
     */
    public CBTouchHandler getToucHandler() {
        return this.touchHandler;
    }
}
