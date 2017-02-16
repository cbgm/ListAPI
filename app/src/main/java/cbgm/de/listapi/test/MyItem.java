package cbgm.de.listapi.test;

import cbgm.de.listapi.test.base.Item;

/**
 * Created by SA_Admin on 06.02.2017.
 */

public class MyItem extends Item {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public MyItem(MyViewHolder holder, int itemResource) {
        super(holder, itemResource);
    }
}
