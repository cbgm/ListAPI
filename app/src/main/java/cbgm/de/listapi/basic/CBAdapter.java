package cbgm.de.listapi.basic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

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

    /**
     * Constructor
     * @param context the application context
     * @param data the data to fill
     */
    public CBAdapter(final Context context, final List<E> data) {
        this.data = data;
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
        return item.getConvertView(position, convertView, parent, listMenuListener, inflator, this.context);
    }

    public void setActionListener(final ICBActionNotifier listMenuListener) {
        this.listMenuListener = listMenuListener;
    }

    /**
     * Method to init the adapter
     */
    public void init(final List<E> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * Method to the adapters data.
     * @return the list
     */
    public List<E> getData(){
        return this.data;
    }

}
