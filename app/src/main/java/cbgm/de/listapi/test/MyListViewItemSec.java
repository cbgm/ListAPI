package cbgm.de.listapi.test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cbgm.de.listapi.R;
import cbgm.de.listapi.data.CBBaseButton;
import cbgm.de.listapi.data.LayoutID;
import cbgm.de.listapi.listener.IListMenuListener;
import cbgm.de.listapi.listener.IOneClickListener;
import cbgm.de.listapi.listener.SwipeListener;
import cbgm.de.listapi.test.base.Item;
import cbgm.de.listapi.test.base.MyHolder;
import cbgm.de.listapi.test.base.ViewItem;

/**
 * Created by SA_Admin on 06.02.2017.
 */

public class MyListViewItemSec extends ViewItem {
    public MyListViewItemSec(MyItem2 item) {
        super(item);
    }

    public MyHolder setUpView(final int position, View convertView, final ViewGroup parent, final boolean isSortMode, final IListMenuListener listMenuListener, final int highlightPos, final IOneClickListener oneClickListener, final LayoutInflater inflater, final SwipeListener swipeListener) {

        MyViewHolder2 test = (MyViewHolder2)holder;
        MyItem2 t = (MyItem2)item;
        test.name.setText(t.getName());
        test.name.setTextColor(Color.BLACK);
        test.name.setEnabled(true);

        if (isSortMode) {
            test.name.setOnClickListener(null);

            if (highlightPos == position && highlightPos != -1) {
                holder.item.setBackgroundColor(Color.rgb(219,235,226));
            } else {
                holder.item.setBackgroundColor(Color.WHITE);
            }
        } else {
            test.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listMenuListener.handleShow(item);
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeListener.rollback();
                    listMenuListener.handleEdit(item);
                }
            });
        }
        return test;
    }

    public MyHolder initView(View itemView, final Context context) {
        MyViewHolder2 test = (MyViewHolder2)holder;
        test.name = (TextView) itemView.findViewById(R.id.txt_type);
        test.buttonContainer.addView(CBBaseButton.getButton(LayoutID.EDIT_BUTTON_ID, context, R.color.cb_edit_background_color, R.mipmap.edit_icon));
        test.edit = (LinearLayout) itemView.findViewById(LayoutID.EDIT_BUTTON_ID);
        return test;
    }
}
