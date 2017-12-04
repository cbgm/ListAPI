package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.widget.ListView;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBListViewItem;
import cbgm.de.listapi.basic.CBViewHolder;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Class to handle the touch type swipe.
 * @author Christian Bergmann
 */

public class CBSelectType extends CBTouchType {

    //the current position which describes the item
    private int pos;
    //the view holder the list element relies on
    private CBViewHolder holder;

    public CBSelectType(List sequenceList, CBAdapter baseAdapter, ListView listContainer, ICBActionNotifier actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    @Override
    public void cleanTouch() {

    }

    @Override
    public void onInitialDown(MotionEvent e) {
        this.pos = this.listContainer.pointToPosition((int) e.getX(), (int) e.getY());

        if(this.pos == -1)
            super.onInitialDown(e);

        this.holder = ((CBListViewItem)listContainer.getAdapter().getItem(this.pos)).getHolder();

    }

    @Override
    public void onClick(MotionEvent e) {
        //highlight item when selected
        if (((ColorDrawable)this.holder.item.getBackground()).getColor() == Color.WHITE) {
            this.holder.item.setBackgroundColor(Color.LTGRAY);
        } else {
            this.holder.item.setBackgroundColor(Color.WHITE);
        }
        this.actionNotifier.singleClickAction(this.pos);

    }
}
