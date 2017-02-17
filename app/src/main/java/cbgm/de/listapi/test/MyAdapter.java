package cbgm.de.listapi.test;

import android.content.Context;

import java.util.List;

import cbgm.de.listapi.data.CBAdapter;
import cbgm.de.listapi.test.base.ViewItem;

/**
 * Created by SA_Admin on 06.02.2017.
 */

public class MyAdapter extends CBAdapter<ViewItem> {
    public MyAdapter(Context context, List<ViewItem> data) {
        super(context, data);
    }

    @Override
    public void handleSingleClick(final int position) {
        final String item = (String) this.data.get(position).getItem();
        //listMenuListener.handleEdit(item);
    }

    @Override
    public void handleLongClick(int position) {

    }
}
