package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBViewHolder;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Class to handle the touch type swipe.
 * @author Christian Bergmann
 */


public class CBSwipeType<H extends CBViewHolder<I>, I> extends CBTouchType<H, I> {
    //the current position which describes the item
    private int pos;
    //the old x coordinate of the move
    private float fromX = -1;
    //the view holder the list element relies on
    private CBViewHolder holder;

    CBSwipeType(List<I> data, CBAdapter<H, I> baseAdapter, RecyclerView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        super(data, baseAdapter, listContainer, actionNotifier, context);
    }

    @Override
    public void cleanTouch() {
        Log.d("LIST API", "Item swipe rollback");
        this.holder.getFrontItem().bringToFront();
        doAnimation(-this.fromX, 0);
        this.fromX = -1;
        this.modeHelper.setSwipeActive(false);
        this.actionNotifier.swipeAction();
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

        if (!this.modeHelper.isSwipeActive()) {
            int color = ((ColorDrawable)holder.getFrontItem().getBackground()).getColor();
            if (color == Color.WHITE || color == this.modeHelper.getHighlightColor()) {
                holder.getFrontItem().setBackgroundColor(this.modeHelper.getSelectColor());
            } else {
                holder.getFrontItem().setBackgroundColor(Color.WHITE);
            }
            //cleanTouch();
            actionNotifier.longClickAction(pos);
        }
    }

    @Override
    public void onInitialDown(MotionEvent e) {
        View view = this.listContainer.findChildViewUnder((int) e.getX(), (int) e.getY());
        this.pos = this.listContainer.getChildAdapterPosition(view);

        if (!this.modeHelper.isSwipeActive() && this.pos != -1) {
            this.fromX = 0;
            this.holder = (CBViewHolder) view.getTag();
            this.holder.getFrontItem().setBackgroundColor(this.modeHelper.getHighlightColor());
            this.modeHelper.setCurrentPosition(this.pos);
        }
    }


    @Override
    public void onSwipeLeft(MotionEvent start, MotionEvent end) {
        if (!this.modeHelper.isSwipeActive()) {
            float offset = 0;

            Log.e("test", fromX  + ", " + -holder.getButtonContainer().getWidth() / 2);
            if (fromX > holder.getButtonContainer().getWidth() / 2) {
                doAnimation(-fromX, -holder.getButtonContainer().getWidth());
                this.modeHelper.setSwipeActive(true);
            } else {
                offset = start.getX() - end.getX();
                doAnimation(-fromX, -offset);
            }

            fromX = offset;
        }
    }

    @Override
    public void onSwipeRight(MotionEvent start, MotionEvent end) {
        if (this.modeHelper.isSwipeActive() && this.pos == this.modeHelper.getCurrentPosition())
            cleanTouch();
    }

    @Override
    public void onUp(MotionEvent event) {
        this.holder.getFrontItem().setBackgroundColor(Color.WHITE);
        if (!this.modeHelper.isSwipeActive() && !this.modeHelper.isButtonClicked()) {
            cleanTouch();
        }
        // view.setBackgroundColor(Color.parseColor("#f9f9f9"));

    }

    @Override
    public void onClick(MotionEvent e) {

        if (!this.modeHelper.isSwipeActive())
            this.actionNotifier.singleClickAction(this.pos);

        if (this.modeHelper.isSwipeActive() && this.pos == this.modeHelper.getCurrentPosition())
            cleanTouch();
    }
}
