package cbgm.de.listapi.listener;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import cbgm.de.listapi.data.CBViewHolder;

/**
 * Listener for handling touch events of list items
 * - handles swiping behaviour
 * @author Christian Bergmann
 */

public class CBSwipeListener implements View.OnTouchListener {

    /* Tells if swipe is active */
    private boolean dragActive;
    /* Tells the original x position moving from */
    private float fromX;
    /* Tells the x position moving to */
    private float toX;
    /* Tells the changed original x position moving from */
    private float fromTempX;
    /* Used to identify if there is more than just an swipe in x direction */
    private float fromY;
    /* Tells if the list item menu is visible  */
    private boolean menuVisible;
    /* The list item view holder */
    private CBViewHolder holder;
    /* The list items position */
    private int listPosition;
    /* The listener to handle a single click */
    private ICBActionNotifier iListMenuListener;
    /* The handler which identifies a selection */
    private final Handler longPressHandler = new Handler();
    /*Tells if the long click was successful and therefore select is active*/
    private boolean isLongClick = false;

    private ICBSwipeNotifier iSwipeNotifier;

    /**
     * Constructor
     * @param holder the view holder
     * @param position the items position
     * @param iListMenuListener the listener to handle a single click
     */
    public CBSwipeListener(final CBViewHolder holder, final int position, final ICBActionNotifier iListMenuListener, final ICBSwipeNotifier iSwipeNotifier) {
        this.holder = holder;
        this.iListMenuListener = iListMenuListener;
        this.iSwipeNotifier = iSwipeNotifier;
        this.listPosition = position;
        this.dragActive = false;
        this.menuVisible = false;
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                this.longPressHandler.postDelayed(this.longPressedRunnable, 400);
                this.fromX = motionEvent.getX();
                this.fromY = motionEvent.getY();
                Log.d("LIST API", "Item is clicked for swiping from: " + this.fromX + " to: " + this.toX);
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                Log.d("LIST API", "Move action");
                if (this.toX == 0 && motionEvent.getX() < this.fromX) {
                    this.toX = motionEvent.getX();
                }

                //Adding offset because some devices are too fast
                if ((motionEvent.getX() + 50) < this.fromX) {
                    this.dragActive = true;
                }

                if (motionEvent.getX() < this.fromX && motionEvent.getX() < this.toX && (this.fromX - this.toX) < this.holder.buttonContainer.getWidth()) {
                    this.fromTempX = motionEvent.getX() - this.toX;
                    float move = this.fromTempX - 1;

                    //check if long click should be canceled (offset for real device)
                    if (move < -20) {
                        cleanLongClick();

                    }

                    if (move > -holder.buttonContainer.getWidth() / 2) {
                        Log.d("LIST API", "Swiping from: " + this.fromTempX + " to: " + move);
                        doAnimation(this.fromTempX, move);
                    } else {
                        this.menuVisible = true;
                    }
                }

                //cleanLongClick();
                return true;
            }

            case MotionEvent.ACTION_UP: {
                Log.d("LIST API", "Done: " + this.fromTempX + " container: " + -holder.buttonContainer.getWidth() / 2);
                if (-fromTempX < holder.buttonContainer.getWidth() / 2) {
                    rollback();
                }

                if (this.dragActive && this.menuVisible) {
                    Log.d("LIST API", "Item released");
                    doAnimation(this.fromTempX, -this.holder.buttonContainer.getWidth());
                    this.dragActive = false;
                    this.holder.backItem.bringToFront();
                    this.iSwipeNotifier.swipeActive(true);
                    Log.d("LIST API", "Item clicked");
                } else {

                    if (!this.dragActive && !this.menuVisible) {
                        Log.d("LIST API", "Item clicked");
                        //rollback();

                        if (!((motionEvent.getY() - this.fromY) > 50 || (this.fromY - motionEvent.getY()) > 50) && !isLongClick)
                            this.iListMenuListener.handleSingleClick(listPosition);
                    } else {
                        rollback();
                    }

                }
                cleanLongClick();
                return true;
            }

            case MotionEvent.ACTION_CANCEL: {

                if (this.dragActive && this.menuVisible) {
                    Log.d("LIST API", "Item released");
                    doAnimation(this.fromTempX, -this.holder.buttonContainer.getWidth());
                    this.dragActive = false;
                    this.holder.backItem.bringToFront();
                    this.iSwipeNotifier.swipeActive(true);
                    Log.d("LIST API", "Item clicked");
                    //rollback();
                } else {

                    if (!this.dragActive && !this.menuVisible) {
                        Log.d("LIST API", "Item clicked");
                        //rollback();rollback();
                        rollback();
                        if (!((motionEvent.getY() - this.fromY) > 50 || (this.fromY - motionEvent.getY()) > 50) && !isLongClick)
                            this.iListMenuListener.handleSingleClick(listPosition);
                    } else {
                        rollback();
                    }

                }
                cleanLongClick();
                return true;
            }
            default:
                break;
        }
        return true;
    }

    @SuppressWarnings("unused")
    public void setClickListener(final ICBActionNotifier iListMenuListener) {
        this.iListMenuListener = iListMenuListener;
    }

    /**
     * Method to roll back the swiping action
     */
    public void rollback() {
        Log.d("LIST API", "Item swipe rollback");
        this.holder.item.bringToFront();
        doAnimation(this.fromTempX, 0);
        this.fromX = 0;
        this.toX = 0;
        this.dragActive = false;
        this.menuVisible = false;
    }

    public void cleanLongClick() {
        this.longPressHandler.removeCallbacks(this.longPressedRunnable);
        this.isLongClick = false;
    }

    /**
     * Method to do the animation for the swiping
     * @param startAt the starting x point
     * @param endAt the ending x point
     */
    private void doAnimation(final float startAt, final float endAt) {
        TranslateAnimation animate = new TranslateAnimation(startAt, endAt, 0, 0);
        animate.setDuration(100);
        animate.setFillAfter(true);
        this.holder.item.startAnimation(animate);
    }

    /**
     * Runnable which is used to identify a selection of an item.
     */
    @SuppressWarnings("unchecked")
    private Runnable longPressedRunnable = new Runnable() {
        @SuppressWarnings("unchecked")
        public void run() {
            isLongClick = true;
            iListMenuListener.handleLongClick(listPosition);
        }
    };
}