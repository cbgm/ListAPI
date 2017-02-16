package cbgm.de.listapi.test;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import cbgm.de.listapi.R;
import cbgm.de.listapi.data.CBListActivity;
import cbgm.de.listapi.test.base.Item;
import cbgm.de.listapi.test.base.ViewItem;

public class MainActivity extends CBListActivity<ViewItem, Item, MyAdapter> {
    private static final int MENU_ITEM_ITEM1 = 1;
    List<ViewItem> test;

    public MyAdapter initAdapter() {
        int type = 1;
        int[] temp = {5, 7 ,2 , 3 ,4 , 10, 8, 6 ,1 ,9};
        test = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            if (type == 1) {
                MyItem item = new MyItem(new MyViewHolder(), R.layout.backitem_standard, temp[i]);
                item.setName("test " + i);
                MyListViewItem li = new MyListViewItem(item);
                test.add(li);
                type = 2;
            } else {
                MyItem2 item2 = new MyItem2(new MyViewHolder2(), R.layout.backitem_standard2, temp[i]);
                item2.setName("test " + i);
                MyListViewItemSec li2 = new MyListViewItemSec(item2);
                test.add(li2);
                type = 1;
            }
        }
        sortList();
        return new MyAdapter(this, test);
    }

    public List<ViewItem> getUpdatedData() {
        return this.test;
    }


    @Override
    public void handleDelete(Item o) {
        Toast.makeText(getBaseContext(), "delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleEdit(Item o) {
        Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleShow(Item o) {
        Toast.makeText(getApplicationContext(), "show", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleSort(List<ViewItem> list) {
        updateAdapter();
        Toast.makeText(getApplicationContext(), "sort", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "Sort: " + isSortMode).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_ITEM1:
                this.isSortMode = !this.isSortMode;
                item.setTitle("Sort: " + isSortMode);
                resetList();
                return true;

            default:
                return false;
        }
    }

    public void sortList() {
        Collections.sort(test, new Comparator<ViewItem>() {
            @Override
            public int compare(ViewItem a, ViewItem b) {
                return ((Integer)a.getItem().getSequenceNumber()).compareTo((Integer)b.getItem().getSequenceNumber());
            }
        });
    }
}

