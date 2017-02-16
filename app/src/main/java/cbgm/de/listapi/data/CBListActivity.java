package cbgm.de.listapi.data;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import cbgm.de.listapi.R;
import cbgm.de.listapi.listener.DragListener;
import cbgm.de.listapi.listener.IListMenuListener;


/**
 * Activity to work with Results and show them in a list
 * @author Christian Bergmann
 */

public abstract class CBListActivity<E extends CBListViewItem, M extends CBItem, T extends CBAdapter> extends FragmentActivity implements IListMenuListener<E, M> {

    protected T adapter;
    protected Menu menu;
    protected List<E> resortedList = null;
    protected boolean isSortMode = false;
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
     * Method for setting up the adapter.
     */
    public abstract T initAdapter();

    public void resetList() {
        if(!this.isSortMode) {
            this.listContainer.setOnTouchListener(null);
            listContainer.setAdapter(this.adapter);
            this.adapter.reInit(getUpdatedData(), this.isSortMode);
        } else {
            DragListener<E, T> dragListener = new DragListener<>(this.adapter.getData(), this.adapter, this.listContainer);
            dragListener.setSortListener(this);
            listContainer.setAdapter(this.adapter);
            this.listContainer.setOnTouchListener(dragListener);
            this.adapter.reInit(getUpdatedData(), this.isSortMode);
        }
    };

    public void updateAdapter(){

        if(!this.isSortMode) {
            this.listContainer.setOnTouchListener(null);

            this.adapter.reInit(getUpdatedData(), this.isSortMode);
        } else {
            this.adapter.reInit(getUpdatedData(), this.isSortMode);
            DragListener<E, T> dragListener = new DragListener<>(this.adapter.getData(), this.adapter, this.listContainer);
            dragListener.setSortListener(this);
            this.listContainer.setOnTouchListener(dragListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public abstract List<E> getUpdatedData();

}
