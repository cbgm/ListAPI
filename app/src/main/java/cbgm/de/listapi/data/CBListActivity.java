package cbgm.de.listapi.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import java.util.List;

import cbgm.de.listapi.R;
import cbgm.de.listapi.listener.DragListener;
import cbgm.de.listapi.listener.IListMenuListener;
import cbgm.de.listapi.listener.SelectListener;


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

            this.adapter.reInit(getUpdatedData(), this.isSortMode);
        } else if (this.isSortMode && !this.isSelectMode) {
            // add touch to list container if in sort
            this.adapter.reInit(getUpdatedData(), this.isSortMode);
            DragListener<E, T> dragListener = new DragListener<>(this.adapter.getData(), this.adapter, this.listContainer, getApplicationContext());
            dragListener.setSortListener(this);
            this.listContainer.setOnTouchListener(dragListener);
        } else if (!this.isSortMode && this.isSelectMode) {
            this.adapter.reInit(getUpdatedData(), this.isSortMode);
            SelectListener<E, T> deleteListener = new SelectListener<>(this.adapter.getData(), this.adapter, this.listContainer, getApplicationContext());
            deleteListener.setDeleteListener(this);
            this.listContainer.setOnTouchListener(deleteListener);
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

    public void toggleListViewScrolling(final boolean isActive) {
        this.listContainer.setShouldScroll(isActive);
    }

}
