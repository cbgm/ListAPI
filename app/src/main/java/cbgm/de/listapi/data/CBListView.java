package cbgm.de.listapi.data;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cbgm.de.listapi.listener.CBDragListener;
import cbgm.de.listapi.listener.CBListMode;
import cbgm.de.listapi.listener.ICBActionDelegate;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Custom ListView to block scrolling action
 * @author Christian Bergmann
 */

public class CBListView<E extends CBListViewItem, T extends CBAdapter> extends ListView implements ICBActionNotifier<E> {
    protected CBListMode mode = CBListMode.SWIPE;
    protected boolean shouldBlockScroll = false;
    protected ICBActionDelegate<E> deletegateListener;
    protected T adapter;

    private ArrayList<Boolean> selectedItems;
    /*first selected position*/
    protected int firstSelectedPosition = -1;

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
            return this.shouldBlockScroll || super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setAdapter(T adapter) {
        this.adapter = adapter;
        this.mode = CBListMode.SWIPE;
        this.adapter.setActionListener(this);
        super.setAdapter(this.adapter);
    }

    public ListAdapter getAdapter() {
        return (T)super.getAdapter();
    }

    @Override
    public void handleDelete(final Object o) {
        Log.d("LIST_API", "button delete clicked");
        deletegateListener.delegateDelete(o);
    }

    @Override
    public void handleEdit(final Object o) {
        Log.d("LIST_API", "button edit clicked");
        deletegateListener.delegateEdit(o);
    }

    @Override
    public void handleShow(final Object o) {
        Log.d("LIST_API", "show clicked");
        deletegateListener.delegateShow(o);
    }

    @Override
    public void handleSort(final List<E> list) {
        Log.d("LIST_API", "item sorted");
        deletegateListener.delegateSort(list);
    }

    public final void handleSelect(final int position) {
        Log.d("LIST_API", "item selected");
        if (position != -1)
            this.selectedItems.set(position, !this.selectedItems.get(position));
        deletegateListener.delegateHandleSelect(position);
    }

    @Override
    public void toggleListViewScrolling(final boolean isActive) {
        Log.d("LIST_API", "list scroll " + isActive);
        this.shouldBlockScroll = isActive;
    }

    @Override
    public void handleSingleClick(final int position) {
        if (this.mode == CBListMode.SELECT) {
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
                switchMode(CBListMode.SWIPE);
                Log.d("LIST API", "off select");
            }
            return;
        }
        deletegateListener.delegateSingleClick(position);
    }

    @Override
    public void handleLongClick(final int position) {
        //check if not in selection mode
        if (this.mode != CBListMode.SELECT) {
            firstSelectedPosition = position;
            switchMode(CBListMode.SELECT);
        }
        deletegateListener.delegateLongClick(position);
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
    protected List<Integer> getSelectedPositions(){
        List<Integer> positions = new ArrayList<>();

        for(int i=0; i < this.selectedItems.size(); i++){

            if (this.selectedItems.get(i)){
                positions.add(i);
            }
        }
        return positions;
    }


    private void switchMode(final CBListMode mode) {
        List<E> temp = new ArrayList<>((List<E>) this.adapter.getData());

        switch (mode) {
            case SWIPE:
                for (E item : temp)
                    item.setFirstSelectedPosition(-1);
                this.adapter.reInit(temp, mode);
                break;
            case SELECT:
                setSelectedItems();
                handleSelect(firstSelectedPosition);
                for (E item : temp)
                    item.setFirstSelectedPosition(firstSelectedPosition);
                firstSelectedPosition = -1;
                Log.d("LIST API", "in select");
                this.adapter.reInit(temp, mode);
                break;
            case SORT:
                for (E item : temp)
                    item.setFirstSelectedPosition(-1);
                this.adapter.reInit(temp, mode);
                CBDragListener<E, T> dragListener = new CBDragListener<>(temp, this.adapter, this, getContext());
                dragListener.setSortListener(this);
                this.setOnTouchListener(dragListener);
                break;
            default:
                break;
        }
        this.mode = mode;
    }

    public void setDelegateListener(final ICBActionDelegate delegateListener) {
        this.deletegateListener = delegateListener;
    }

}
