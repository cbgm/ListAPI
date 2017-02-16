package cbgm.de.listapi.test.base;

import cbgm.de.listapi.data.CBItem;
import cbgm.de.listapi.test.MyViewHolder;

/**
 * Created by SA_Admin on 06.02.2017.
 */

public class Item extends CBItem<MyHolder> {

    public Item(MyHolder holder, int itemResource, int sequenceNumber) {
        super(holder, itemResource, sequenceNumber);
    }
}
