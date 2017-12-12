package cbgm.de.listapi.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

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

@SuppressWarnings("unused")
public class CBListView<H extends CBViewHolder<I>, I, A extends CBAdapter<H, I>> extends RecyclerView implements ICBActionNotifier<I> {
    /*Listener to forward list item click events*/
    protected ICBActionDelegate<I> deletegateListener;
    /*The list adapter*/
    protected A adapter;
    //touch handler for switching the possible touch types (swipe, sort, select)
    protected CBTouchHandler<A, I, H> touchHandler;

    protected CBModeHelper modeHelper = CBModeHelper.getInstance();
    //array mirrors the positions of the selected items
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

    public void setAdapter(A adapter) {
        this.adapter = adapter;
        this.adapter.setActionListener(this);
        super.setAdapter(this.adapter);
    }

    public CBAdapter<H, I> getAdapter() {
        return this.adapter;
    }

    @Override
    public void deleteAction(final I o) {
        Log.d("LIST_API", "button delete clicked");
        this.touchHandler.informButtonClick();
        deletegateListener.delegateDeleteAction(o);
    }

    @Override
    public void editAction(final I o) {
        Log.d("LIST_API", "button edit clicked");
        this.touchHandler.informButtonClick();
        deletegateListener.delegateEditAction(o);
    }

    @Override
    public void sortAction(List<I> list) {
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
        this.selectedItems = new ArrayList<>(getAdapter().getItemCount());

        for(int i=0; i < getAdapter().getItemCount(); i++){
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
    public void init(final List<I> data, A adapter) {
        this.adapter = adapter;
        this.touchHandler = new CBTouchHandler<>(data, this.adapter, this, this, getContext());
        this.setOnTouchListener(touchHandler);
        this.adapter.init(data);
        setAdapter(this.adapter);
    }

    /**
     * Method to set the delegate listener for forwarding list item click events
     * @param delegateListener the listener
     */
    public void setDelegateListener(final ICBActionDelegate<I> delegateListener) {
        this.deletegateListener = delegateListener;
    }

    /**
     * Method to get the touch handler (can bes used to clean up touch events)
     * @return the CBTouchHandler
     */
    public CBTouchHandler getTouchHandler() {
        return this.touchHandler;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
