package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBViewHolder;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Abstract class for the variations of touch types.
 * @author Christian Bergmann
 */

@SuppressWarnings("unused")
public abstract class CBTouchType<H extends CBViewHolder<I>, I> implements View.OnTouchListener {
    //the list item data
    protected List<I> data;
    //the recycler adapter
    protected CBAdapter<H, I> adapter;
    //the recycler view
    protected RecyclerView listContainer;
    //the notifier when a list action is triggered
    protected ICBActionNotifier<I> actionNotifier;
    //handler for a long press event (long click of gesture detector is not always usable)
    final Handler longPressHandler;
    final CBModeHelper modeHelper;
    //detector for different gestures
    private GestureDetector gestureDetector;

    public CBTouchType(final List<I> data, CBAdapter<H, I> baseAdapter, RecyclerView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        this.data = data;
        this.adapter = baseAdapter;
        this.listContainer = listContainer;
        this.actionNotifier = actionNotifier;
        this.longPressHandler = new Handler();
        this.modeHelper = CBModeHelper.getInstance();
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    /**
     * Method is called when a MotionEvent is fired (specific onTouch triggered by CBTouchHandler).
     * @param view the which the MotionEvent is triggered from
     * @param motionEvent the MotionEvent
     * @return boolean (please see View.OnTouchListener)
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            onUp(motionEvent);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            onMove(motionEvent);
        }
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    /**
     * Method to clean a specific touch event.
     */
    public abstract void cleanTouch();

    /**
     * the gesture detector
     */
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_OFFSET = 20;

        @Override
        public boolean onDown(MotionEvent e) {
            onInitialDown(e);
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick(e);
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick(e);
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick(e);
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_OFFSET) {
                        if (diffX > 0) {
                            onSwipeRight(e1, e2);
                        } else {
                            onSwipeLeft(e1, e2);
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_OFFSET) {
                        if (diffY > 0) {
                            onSwipeDown(e1, e2);
                            super.onScroll(e1, e2, distanceX, distanceY);
                        } else {
                            onSwipeUp(e1, e2);
                            super.onScroll(e1, e2, distanceX, distanceY);
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Method called when swiped right.
     * @param start the starting event
     * @param end the end event
     */
    protected void onSwipeRight(MotionEvent start, MotionEvent end) {
    }
    /**
     * Method called when swiped left.
     * @param start the starting event
     * @param end the end event
     */
    protected void onSwipeLeft(MotionEvent start, MotionEvent end) {
    }
    /**
     * Method called when swiped down.
     * @param start the starting event
     * @param end the end event
     */
    protected void onSwipeDown(MotionEvent start, MotionEvent end) {
    }
    /**
     * Method called when swiped up.
     * @param start the starting event
     * @param end the end event
     */
    protected void onSwipeUp(MotionEvent start, MotionEvent end) {
    }
    /**
     * Method called when clicked.
     * @param e the motion event
     */
    protected void onClick(MotionEvent e) {
    }
    /**
     * Method called when swiped right.
     * @param e the motion event
     */
    protected void onDoubleClick(MotionEvent e) {
    }
    /**
     * Method called when double clicked.
     * @param e the motion event
     */
    protected void onLongClick(MotionEvent e) {
    }
    /**
     * Method called when pushed down.
     * @param e the motion event
     */
    protected void onInitialDown(MotionEvent e) {
    }
    /**
     * Method called when released.
     * @param e the motion event
     */
    protected void onUp(MotionEvent e) {
    }
    /**
     * Method called when moved.
     * @param e the motion event
     */
    protected void onMove(MotionEvent e) {
    }

}

