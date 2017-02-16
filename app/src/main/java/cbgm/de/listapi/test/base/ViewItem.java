package cbgm.de.listapi.test.base;

import cbgm.de.listapi.data.CBListViewItem;
import cbgm.de.listapi.test.MyItem;
import cbgm.de.listapi.test.MyViewHolder;

/**
 * Created by SA_Admin on 14.02.2017.
 */

public abstract class ViewItem extends CBListViewItem<MyHolder, Item>  {
    public ViewItem(Item item) {
        super(item);
    }
}
