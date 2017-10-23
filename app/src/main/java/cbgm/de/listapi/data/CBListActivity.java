package cbgm.de.listapi.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import cbgm.de.listapi.R;
import cbgm.de.listapi.listener.DragListener;
import cbgm.de.listapi.listener.IListMenuListener;


/**
 * Activity to work with Results and show them in a list
 * @author Christian Bergmann
 */

public abstract class CBListActivity<E extends CBListViewItem, T extends CBAdapter> extends AppCompatActivity implements IListMenuListener<E> {
    /* The lists adapter */
    protected T adapter;
    /* The activitys menu */
    protected Menu menu;
    /* Set if sort mode of list should be active  */
    protected boolean isSortMode = false;
    /* Set if select mode of list should be active  */
    protected boolean isSelectMode = false;
    /* The lists container within the layout */
    protected CBListView listContainer;
    /*The selected items */
    private ArrayList<Boolean> selectedItems;
    /*first selected position*/
    protected int firstSelectedPosition = -1;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateAdapter();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        contentView();
        this.listContainer = (CBListView) findViewById(R.id.list_container);
        this.adapter = initAdapter();
        this.adapter.setListMenuListener(this);
        listContainer.setAdapter(this.adapter);
    }

    /**
     * Method for setting up the adapter and do some initialization.
     */
    public abstract T initAdapter();

    /**
     * Method to set the contentView, use setContentView()
     */
    public abstract void contentView();

    /**
     * Method for updating the data after changes
     */
    public abstract void updateData();

    /**
     * Method to update the adapter
     */
    @SuppressWarnings("unchecked")
    public void updateAdapter(){

        // if not in sort mode remove touch from list container
        if(!this.isSortMode && !this.isSelectMode) {

            this.listContainer.setOnTouchListener(null);

            this.adapter.reInit(getUpdatedData(), this.isSortMode, this.isSelectMode);
        } else if (this.isSortMode && !this.isSelectMode) {
            // add touch to list container if in sort
            this.adapter.reInit(getUpdatedData(), this.isSortMode, this.isSelectMode);
            DragListener<E, T> dragListener = new DragListener<>(this.adapter.getData(), this.adapter, this.listContainer, getApplicationContext());
            dragListener.setSortListener(this);
            this.listContainer.setOnTouchListener(dragListener);
        } else if (!this.isSortMode && this.isSelectMode) {
            this.adapter.reInit(getUpdatedData(), this.isSortMode, this.isSelectMode);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Method to get the updated data to use it in updateAdapter
     * @return the updated data
     */
    public abstract List<E>  getUpdatedData();


    public final void handleSingleClick(final int position) {
        if (this.isSelectMode) {
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
                initSelectionMode();
                Log.d("LIST API", "off select");
            }
            return;
        }
        delegateSingleClick(position);
    }

    public abstract void delegateSingleClick(final int position);

    @Override
    public final void handleLongClick(final int position) {
        //check if not in selection mode
        if (!this.isSelectMode) {
            firstSelectedPosition = position;
            initSelectionMode();
            firstSelectedPosition = -1;
            Log.d("LIST API", "in select");
        }
    }

    public void initSelectionMode() {
        this.isSelectMode = !this.isSelectMode;
        setSelectedItems();
        updateData();
        updateAdapter();
        handleSelect(firstSelectedPosition);
    }

    public void cancelSelectionMode() {
        this.isSelectMode = false;
        this.selectedItems.clear();
    }

    public void toggleListViewScrolling(final boolean isActive) {
        this.listContainer.setShouldScroll(isActive);
    }

    /**
     * Method to initialize the selected items with a default value
     */
    public void setSelectedItems(){
        this.selectedItems = new ArrayList<>(this.adapter.getCount());

        for(int i=0; i < this.adapter.getCount(); i++){
            this.selectedItems.add(false);
        }
    }

    public final void handleSelect(final int position) {
        if (position != -1)
            this.selectedItems.set(position, !this.selectedItems.get(position));
        delegateHandleSelect(position);
    }

    public abstract void delegateHandleSelect(final int position);

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

}
