package cbgm.de.listapi.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cbgm.de.listapi.listener.ICBActionNotifier;


/**
 * The adapter for the list
 * @author Christian Bergmann
 */
@SuppressWarnings("unused")
public class CBAdapter<I> extends RecyclerView.Adapter<CBAdapterDelegate.CBViewHolder> {
     /*The application context*/
    protected Context context;
    /*Listener for list item click events*/
    protected ICBActionNotifier<I> actionNotifier;
    /*The list items*/
    protected List<I> data;
    /*the selected position(s) for select and drag*/
    protected SparseBooleanArray selectedItems;
    /*the manager for view binding*/
    protected CBAdapterDelegateManager<I> delegateManager = new CBAdapterDelegateManager<>(this);

    /**
     * Constructor
     * @param context the application context
     */
    public CBAdapter(final Context context, final List<I> data) {
        this.context = context;
        this.data = data;
        this.selectedItems = new SparseBooleanArray(data.size());
    }

    /**
     * Constructor
     * @param context the application context
     */
    public CBAdapter(final Context context, final List<I> data, final ICBActionNotifier<I> actionNotifier) {
        this.context = context;
        this.data = data;
        this.selectedItems = new SparseBooleanArray(data.size());
        this.actionNotifier = actionNotifier;
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @SuppressWarnings("unused")
    protected I getItem(final int position) {
        return this.data.get(position);
    }

    @Override
    public void onBindViewHolder(final CBAdapterDelegate.CBViewHolder holder, final int position) {
        delegateManager.onBindViewHolder(holder, position, data, actionNotifier);
    }

    @Override
    public int getItemViewType(final int position) {
        return delegateManager.getItemViewType(data, position);
    }

    @Override
    public CBAdapterDelegate.CBViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return delegateManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    public void setActionListener(final ICBActionNotifier<I> actionNotifier) {
        this.actionNotifier = actionNotifier;
    }

    @SuppressWarnings("unchecked")
    public void addAdapterDelegate(final CBAdapterDelegate delegate) {
        this.delegateManager.addAdapterDelegate(delegate);
    }

    /**
     * Method to swap two items
     * @param fromPos the initial position
     * @param toPos the postion to swap with
     */
    public void swapItems(final int fromPos, final int toPos) {
        Collections.swap(this.data, fromPos, toPos);
        setSingleSelectedItem(toPos, true);
        notifyItemChanged(fromPos);
        notifyItemChanged(toPos);
    }

    /**
     * Method to add a single item
     * @param item the item ref
     */
    public void addItem(final I item) {
        this.data.add(item);
        this.selectedItems = new SparseBooleanArray(data.size());
        notifyItemInserted(data.size());
    }

    /**
     * Method to add a single item
     * @param pos the position for inserting
     * @param item the item ref
     */
    @SuppressWarnings("SameParameterValue")
    public void addItem(final int pos, final I item) {
        this.data.add(pos, item);
        this.selectedItems = new SparseBooleanArray(data.size());
        notifyItemChanged(pos);
        notifyItemInserted(pos);

    }

    /**
     * Method to update a single item
     * @param pos the item position
     * @param item the item ref
     */
    public void changeItem(final int pos, final I item) {
        notifyItemChanged(pos, item);
    }

    /**
     * Method to delete a single item
     * @param pos the pos of the item
     */
    public void deleteItem(final int pos) {
        this.data.remove(pos);
        this.selectedItems = new SparseBooleanArray(data.size());
        notifyItemRemoved(pos);
    }

    /**
     * Method to delete multiple items
     * @param items the items to delete
     */
    public void deleteItems(final ArrayList<I> items) {
        for (I item : items) {
            int pos = this.data.indexOf(item);
            this.data.remove(pos);
            notifyItemRemoved(pos);
        }
        this.selectedItems = new SparseBooleanArray(data.size());
    }

    /**
     * Method to init the adapter
     */
    public void init(final List<I> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * Method to the adapters data.
     * @return the list
     */
    public List<I> getData(){
        return this.data;
    }

    /**
     * Method to toggle a selected item
     * @param pos of the item
     */
    public void toggleSelection(final int pos) {
        if (this.selectedItems.get(pos, false)) {
            this.selectedItems.delete(pos);
        }
        else {
            this.selectedItems.put(pos, true);
        }
        if (this.selectedItems.size() > 1)
            notifyItemChanged(pos);
        else
            notifyDataSetChanged();
    }

    /**
     * Method to set a single selected item
     * @param pos the position of the item
     * @param selected the selection state
     */
    public void setSingleSelectedItem(final int pos, final boolean selected) {
        this.selectedItems.clear();
        this.selectedItems.put(pos, selected);
        notifyItemChanged(pos);
    }

    /**
     * Method to clear all selections
     */
    public void clearSelections() {
        this.selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return this.selectedItems.size();
    }

    /**
     * Method to get all selected items as list
     * @return the list of items
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(this.selectedItems.size());

        for (int i = 0; i < this.selectedItems.size(); i++) {
            items.add(this.selectedItems.keyAt(i));
        }
        return items;
    }

    /**
     * Method to check if a specific item is selected
     * @param position the postion of the item
     * @return selected
     */
    public boolean isItemSelected(final int position) {
        return this.selectedItems.get(position, false);
    }

}

