package cbgm.de.listapi.data;

/**
 * Created by SA_Admin on 06.02.2017.
 */
public abstract class CBItem<V extends CBViewHolder> {
    protected transient V holder;

    public CBItem(final V holder, final int itemResource) {
        this.holder = holder;
        this.itemResource = itemResource;
    }

    public int getItemResource() {
        return itemResource;
    }

    public void setItemResource(int itemResource) {
        this.itemResource = itemResource;
    }

    public V getHolder() {
        return holder;
    }

    public void setHolder(V holder) {
        this.holder = holder;
    }

    protected transient int itemResource;
}
