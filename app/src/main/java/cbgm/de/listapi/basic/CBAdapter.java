package cbgm.de.listapi.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import cbgm.de.listapi.listener.ICBActionNotifier;


/**
 * The adapter for the list
 * @author Christian Bergmann
 */
public abstract class CBAdapter<H extends CBViewHolder<I>, I> extends RecyclerView.Adapter<H> {
    /*The application context*/
    protected Context context;
    /*Listener for list item click events*/
    protected ICBActionNotifier<I> actionNotifier;
    /*The list items*/
    protected List<I> data;

    /**
     * Constructor
     * @param context the application context
     */
    public CBAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    protected I getItem(final int position) {
        return this.data.get(position);
    }

    @Override
    public void onBindViewHolder(final H holder, final int position) {
        holder.addFunctionalityOnView(getItem(position), position, this.actionNotifier, this.context);
    }

    @Override
    public int getItemViewType(final int position) {
        return super.getItemViewType(position);
    }

    @Override
    public H onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    public void setActionListener(final ICBActionNotifier<I> actionNotifier) {
        this.actionNotifier = actionNotifier;
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
}
