package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBViewHolder;
import cbgm.de.listapi.data.CBListItem;
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

    public CBSelectType(List<CBListItem> sequenceList, CBAdapter baseAdapter, RecyclerView listContainer, ICBActionNotifier actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    @Override
    public void cleanTouch() {

    }

    @Override
    public void onInitialDown(MotionEvent e) {
        View view = this.listContainer.findChildViewUnder((int) e.getX(), (int) e.getY());
        this.pos = this.listContainer.getChildAdapterPosition(view);

        if(this.pos == -1)
            super.onInitialDown(e);

        this.holder = (CBViewHolder) view.getTag();

    }

    @Override
    public void onClick(MotionEvent e) {
        //highlight item when selected
        if (((ColorDrawable)this.holder.getFrontItem().getBackground()).getColor() == Color.WHITE) {
            this.holder.getFrontItem().setBackgroundColor(Color.LTGRAY);
        } else {
            this.holder.getFrontItem().setBackgroundColor(Color.WHITE);
        }
        this.actionNotifier.singleClickAction(this.pos);

    }
}
