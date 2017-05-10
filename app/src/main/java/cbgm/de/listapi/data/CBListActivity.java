package cbgm.de.listapi.data;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ListView;

import java.util.List;

import cbgm.de.listapi.R;
import cbgm.de.listapi.listener.DragListener;
import cbgm.de.listapi.listener.IListMenuListener;


/**
 * Activity to work with Results and show them in a list
 * @author Christian Bergmann
 */

public abstract class CBListActivity<E extends CBListViewItem, T extends CBAdapter> extends FragmentActivity implements IListMenuListener<E> {
    /* The lists adapter */
    protected T adapter;
    /* The activitys menu */
    protected Menu menu;
    /* Set if sort mode of list should be active  */
    protected boolean isSortMode = false;
    /* The lists container within the layout */
    protected ListView listContainer;

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
        setContentView(R.layout.cb_list_activity);
        this.listContainer = (ListView) findViewById(R.id.list_container);
        this.adapter = initAdapter();
        this.adapter.setListMenuListener(this);
        listContainer.setAdapter(this.adapter);
    }

    /**
     * Method for setting up the adapter and do some initialization.
     */
    public abstract T initAdapter();

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
        if(!this.isSortMode) {

            this.listContainer.setOnTouchListener(null);

            this.adapter.reInit(getUpdatedData(), this.isSortMode);
        } else {
            // add touch to list container if in sort
            this.adapter.reInit(getUpdatedData(), this.isSortMode);
            DragListener<E, T> dragListener = new DragListener<>(this.adapter.getData(), this.adapter, this.listContainer, getApplicationContext());
            dragListener.setSortListener(this);
            this.listContainer.setOnTouchListener(dragListener);
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

}
