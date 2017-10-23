package cbgm.de.listapi.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cbgm.de.listapi.listener.IListMenuListener;


/**
 * The adapter for the list
 * @author Christian Bergmann
 */
public abstract class CBAdapter<E extends CBListViewItem> extends BaseAdapter {

    protected Context context;
    protected final LayoutInflater inflator;
    protected IListMenuListener listMenuListener;
    protected List<E> data;
    protected boolean isSortMode = false;
    protected boolean isSelectMode = false;
    protected int highlightPos = -1;

    /**
     * Constructor
     * @param context the application context
     * @param data the data to fill
     */
    public CBAdapter(Context context, List<E> data) {
        this.data = data;
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
        return item.getConvertView(position, convertView, parent, this.isSortMode, this.isSelectMode, listMenuListener, highlightPos, inflator, this.context);
    }

    public void setListMenuListener(final IListMenuListener listMenuListener) {
        this.listMenuListener = listMenuListener;
    }

    /**
     * Method to reninit the listview if some major changes happened
     * @param data the data to update
     */
    public void reInit(final List<E> data, final boolean isSortMode, final boolean isSelectMode) {
        this.isSortMode = isSortMode;
        this.isSelectMode = isSelectMode;
        this.data = data;
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
