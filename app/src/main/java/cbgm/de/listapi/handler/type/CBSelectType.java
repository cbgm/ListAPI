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
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Class to handle the touch type swipe.
 * @author Christian Bergmann
 */

public class CBSelectType<H extends CBViewHolder<I>, I> extends CBTouchType<H, I> {

    //the current position which describes the item
    private int pos;
    //the view holder the list element relies on
    private CBViewHolder holder;

    CBSelectType(List<I> sequenceList, CBAdapter<H, I> baseAdapter, RecyclerView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    @Override
    public void cleanTouch() {

    }

    @Override
    public void onInitialDown(MotionEvent e) {

        if (isMotionOutside(e, null))
            return;
        View childView = this.listContainer.findChildViewUnder((int) e.getX(), (int) e.getY());
        this.pos = this.listContainer.getChildAdapterPosition(childView);

        if(this.pos == -1)
            super.onInitialDown(e);
        this.holder = (CBViewHolder) childView.getTag();

    }

    @Override
    public void onClick(MotionEvent e) {
        if (isMotionOutside(e, null))
            return;
        //highlight item when selected
        if (((ColorDrawable)this.holder.getFrontItem().getBackground()).getColor() == Color.WHITE) {
            this.holder.getFrontItem().setBackgroundColor(this.modeHelper.getSelectColor());
        } else {
            this.holder.getFrontItem().setBackgroundColor(Color.WHITE);
        }
        this.actionNotifier.singleClickAction(this.pos);

    }
}
