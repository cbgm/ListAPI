package cbgm.de.listapi.test;

import cbgm.de.listapi.test.base.Item;

/**
 * Created by SA_Admin on 06.02.2017.
 */

public class MyItem2 extends Item {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String test;

    public MyItem2(MyViewHolder2 holder, int itemResource, int sequenceNumber) {
        super(holder, itemResource, sequenceNumber);
    }
}
