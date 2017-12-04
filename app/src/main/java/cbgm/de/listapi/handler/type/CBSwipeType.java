package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBListViewItem;
import cbgm.de.listapi.basic.CBViewHolder;
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

    public CBSwipeType(List<CBListViewItem> sequenceList, CBAdapter baseAdapter, ListView listContainer, ICBActionNotifier actionNotifier, Context context) {
        super(sequenceList, baseAdapter, listContainer, actionNotifier, context);
    }

    @Override
    public void cleanTouch() {
        Log.d("LIST API", "Item swipe rollback");
        this.holder.item.bringToFront();
        doAnimation(-this.fromX, 0);
        this.fromX = -1;
        this.modeHelper.setSwipeActive(false);
        this.actionNotifier.swipeAction();
    }

    private void doAnimation(final float startAt, final float endAt) {
        TranslateAnimation animate = new TranslateAnimation(startAt, endAt, 0, 0);
        animate.setDuration(100);
        animate.setFillAfter(true);
        this.holder.item.startAnimation(animate);
    }

    @Override
    public void onLongClick(MotionEvent e) {
        Log.d("LIST API", "Item long clicked");

       if (!this.modeHelper.isSwipeActive()) {
           if (((ColorDrawable)holder.item.getBackground()).getColor() == Color.WHITE) {
               holder.item.setBackgroundColor(Color.LTGRAY);
           } else {
               holder.item.setBackgroundColor(Color.WHITE);
           }
           //cleanTouch();
           actionNotifier.longClickAction(pos);
       }
    }

    @Override
    public void onInitialDown(MotionEvent e) {
        this.pos = this.listContainer.pointToPosition((int) e.getX(), (int) e.getY());

        if (!this.modeHelper.isSwipeActive() && this.pos != -1) {
            this.fromX = 0;
            this.holder = ((CBListViewItem) this.listContainer.getAdapter().getItem(this.pos)).getHolder();
            this.modeHelper.setCurrentPosition(this.pos);
        }
    }


    @Override
    public void onSwipeLeft(MotionEvent start, MotionEvent end) {

        if (!this.modeHelper.isSwipeActive()) {
            float offset = 0;

            Log.e("test", fromX  + ", " + -holder.buttonContainer.getWidth() / 2);
            if (fromX > holder.buttonContainer.getWidth() / 2) {
                doAnimation(-fromX, -holder.buttonContainer.getWidth());
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
