package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBAdapterDelegate;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Class to handle the touch type swipe.
 * @author Christian Bergmann
 */


public class CBSwipeType<I> extends CBTouchType<I> {
    //the current position which describes the item
    private int pos;
    //the old x coordinate of the move
    private float fromX = -1;
    //the view holder the list element relies on
    private CBAdapterDelegate.CBViewHolder holder;

    CBSwipeType(List<I> data, CBAdapter<I> baseAdapter, RecyclerView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        super(data, baseAdapter, listContainer, actionNotifier, context);
    }

    @Override
    public void cleanTouch() {
        Log.d("LIST API", "Item swipe rollback");

        if (this.modeHelper.isSwipeEnabled()) {
            this.holder.getFrontItem().bringToFront();
            doAnimation(-this.fromX, 0);
            this.fromX = -1;
            if (this.modeHelper.isSwipeActive())
                this.actionNotifier.swipeAction();
            this.modeHelper.setSwipeActive(false);
        }
    }

    private void doAnimation(final float startAt, final float endAt) {
        TranslateAnimation animate = new TranslateAnimation(startAt, endAt, 0, 0);
        animate.setDuration(100);
        animate.setFillAfter(true);
        this.holder.getFrontItem().startAnimation(animate);
    }

    @Override
    public void onLongClick(MotionEvent e) {
        Log.d("LIST API", "Item long clicked");

        if (!this.modeHelper.isSwipeActive() && this.modeHelper.isSelectEnabled()) {
            //set active because the touch listener is on the listview, so the drawable of the listitem can react on it
            this.holder.getFrontItem().setActivated(false);
            this.adapter.toggleSelection(this.pos);
        }

        if (!this.modeHelper.isSwipeActive())
            this.actionNotifier.longClickAction(pos);
    }

    @Override
    public void onInitialDown(MotionEvent e) {

        if (isMotionOutside(e, null))
            return;
        View childView = this.listContainer.findChildViewUnder((int) e.getX(), (int) e.getY());
        this.pos = this.listContainer.getChildAdapterPosition(childView);

        if (!this.modeHelper.isSwipeActive() && this.pos != -1) {
            this.fromX = 0;
            this.holder = (CBAdapterDelegate.CBViewHolder) childView.getTag();
            //set active because the touch listener is on the listview, so the drawable of the listitem can react on it
            this.holder.getFrontItem().setActivated(true);
            this.modeHelper.setCurrentPosition(this.pos);
        }
    }


    @Override
    public void onSwipeLeft(MotionEvent start, MotionEvent end) {

        if (isMotionOutside(start, end))
            return;

        if (!this.modeHelper.isSwipeActive() && this.modeHelper.isSwipeEnabled()) {
            float offset = 0;

            Log.e("test", fromX  + ", " + -holder.getButtonContainer().getWidth() / 2);
            if (fromX > holder.getButtonContainer().getWidth() / 2) {
                doAnimation(-fromX, -holder.getButtonContainer().getWidth());
                this.modeHelper.setSwipeActive(true);
                this.actionNotifier.swipeAction();
            } else {
                offset = start.getX() - end.getX();
                doAnimation(-fromX, -offset);
            }

            fromX = offset;
        }
    }

    @Override
    public void onSwipeRight(MotionEvent start, MotionEvent end) {

        if (isMotionOutside(start, end))
            return;

        if (this.modeHelper.isSwipeActive() && this.pos == this.modeHelper.getCurrentPosition())
            cleanTouch();
    }

    @Override
    public void onUp(MotionEvent event) {
        //set active because the touch listener is on the listview, so the drawable of the listitem can react on it
        if (this.holder != null)
            this.holder.getFrontItem().setActivated(false);

        if (this.holder != null &&
                !this.modeHelper.isSwipeActive() &&
                (!this.modeHelper.isButtonClicked() || isMotionOutside(event, null)))
            cleanTouch();
    }

    @Override
    public void onClick(MotionEvent e) {
        if (isMotionOutside(e, null))
            return;

        if (!this.modeHelper.isSwipeActive()) {
            this.actionNotifier.singleClickAction(this.pos);
            return;
        }

        if (this.modeHelper.isSwipeActive() && this.pos == this.modeHelper.getCurrentPosition())
            cleanTouch();
    }
}