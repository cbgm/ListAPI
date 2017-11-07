package cbgm.de.listapi.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cbgm.de.listapi.listener.CBListMode;
import cbgm.de.listapi.listener.ICBActionNotifier;


/**
 * The adapter for the list
 * @author Christian Bergmann
 */
public abstract class CBAdapter<E extends CBListViewItem> extends BaseAdapter {
    /*The application context*/
    protected Context context;
    /*The layout inflator*/
    protected final LayoutInflater inflator;
    /*Listener for list item click events*/
    protected ICBActionNotifier listMenuListener;
    /*The list items*/
    protected List<E> data;
    /*The list mode*/
    protected CBListMode mode;
    /*The position to highlight in sort mode*/
    protected int highlightPos = -1;

    /**
     * Constructor
     * @param context the application context
     * @param data the data to fill
     */
    public CBAdapter(final Context context, final List<E> data, final CBListMode mode) {
        this.data = data;
        this.mode = mode;
        this.context= context;
        this.inflator = LayoutInflater.from(context);
    }
    /**
     * Constructor
     * @param context the application context
     */
    public CBAdapter(final Context context) {
        this.context = context;
        this.inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(final int position) {
        return this.data.get(position);

    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        @SuppressWarnings("unchecked")
        final E item =  (E) getItem(position);
        return item.getConvertView(position, convertView, parent, this.mode, listMenuListener, highlightPos, inflator, this.context);
    }

    public void setActionListener(final ICBActionNotifier listMenuListener) {
        this.listMenuListener = listMenuListener;
    }

    /**
     * Method to init the adapter
     */
    public void init(final List<E> data, final CBListMode mode) {
       /* if (mode != CBListMode.SORT && this.data != null)
            this.data.clear();*/
        this.data = data;
        if (mode != CBListMode.NULL)
            this.mode = mode;
        notifyDataSetChanged();
    }

    /**
     * Method for setting the position to highlight when sorting.
     * @param highlightPos the position to highlight
     */
    public void setItemToHighlight(int highlightPos) {
        this.highlightPos = highlightPos;
    }

    /**
     * Method to the adapters data.
     * @return the list
     */
    public List<E> getData(){
        return this.data;
    }

}
