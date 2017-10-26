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

    protected Context context;
    protected final LayoutInflater inflator;
    protected ICBActionNotifier listMenuListener;
    protected List<E> data;
    protected CBListMode mode;
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
     * Method to reninit the listview if some major changes happened
     */
    public void reInit(final List<E> data, final CBListMode mode) {
        this.data.clear();
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
