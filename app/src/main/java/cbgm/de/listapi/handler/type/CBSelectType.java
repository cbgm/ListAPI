package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Class to handle the touch type swipe.
 * @author Christian Bergmann
 */

public class CBSelectType<I> extends CBTouchType<I> {

    CBSelectType(List<I> sequenceList, CBAdapter<I> baseAdapter, RecyclerView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    @Override
    public void cleanTouch() {

    }

    @Override
    public void onClick(MotionEvent e) {

        if (isMotionOutside(e, null))
            return;
        View childView = this.listContainer.findChildViewUnder((int) e.getX(), (int) e.getY());
        int pos = this.listContainer.getChildAdapterPosition(childView);
        this.adapter.toggleSelection(pos);
        this.actionNotifier.singleClickAction(pos);

    }
}
