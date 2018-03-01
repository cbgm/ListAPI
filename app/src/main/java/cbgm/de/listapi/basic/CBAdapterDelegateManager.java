package cbgm.de.listapi.basic;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Manager class to get and set the right ViewHolder by its AdapterDelegate.
 * @author Christian Bergmann
 */

@SuppressWarnings("unused")
public class CBAdapterDelegateManager<I> {

    private CBAdapter<I> adapter;

    private List<CBAdapterDelegate<I>> delegates = new ArrayList<>();

    public CBAdapterDelegateManager(CBAdapter<I> adapter)  {
        this.adapter = adapter;
    }

    public void addAdapterDelegate(final CBAdapterDelegate<I> delegate) {
        delegate.itemType = delegates.size();
        delegate.setAdapter(this.adapter);
        delegates.add(delegate);
    }

    public void removeDelegate(final CBAdapterDelegate delegate) {
        delegates.remove(delegate);
    }

    public void removeDelegate(final int viewType) {
        delegates.remove(viewType);
    }

    public void onBindViewHolder(final CBAdapterDelegate.CBViewHolder holder, final int position, final List<I> data, final ICBActionNotifier<I> actionNotifier) {
        delegates.get(holder.getItemViewType()).onBindViewHolder(holder, position, data, actionNotifier);
    }

    public CBAdapterDelegate.CBViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return delegates.get(viewType).onCreateViewHolder(parent, viewType);
    }

    public int getItemViewType(final List<I> data, final int position) {
        for (CBAdapterDelegate<I> delegate : delegates)
            if (delegate.isTypeOf(data, position)) return  delegate.itemType;
        return -1;
    }
}
