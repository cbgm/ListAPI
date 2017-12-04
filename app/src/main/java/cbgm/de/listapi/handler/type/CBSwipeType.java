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
import cbgm.de.listapi.data.CBListItem;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Created by SA_Admin on 21.11.2017.
 */

public class CBSwipeType extends CBTouchType {
    //the current position which describes the item
    private int pos;
    //the old x coordinate of the move
    private float fromX = -1;
    //the view holder the list element relies on
    private CBViewHolder holder;

    public CBSwipeType(List<CBListItem> data, CBAdapter baseAdapter, RecyclerView listContainer, ICBActionNotifier actionNotifier, Context context) {
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
           if (((ColorDrawable)holder.getFrontItem().getBackground()).getColor() == Color.WHITE) {
               holder.getFrontItem().setBackgroundColor(Color.LTGRAY);
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
        if (!this.modeHelper.isSwipeActive() && !this.modeHelper.isButtonClicked()) {
            cleanTouch();
        }

    }

    @Override
    public void onClick(MotionEvent e) {
        if (!this.modeHelper.isSwipeActive())
            this.actionNotifier.singleClickAction(this.pos);

        if (this.modeHelper.isSwipeActive() && this.pos == this.modeHelper.getCurrentPosition())
            cleanTouch();
    }
}
