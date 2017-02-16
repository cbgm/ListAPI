package cbgm.de.listapi.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import cbgm.de.listapi.data.CBViewHolder;

/**
 * Listener for handling touch events of list items
 * - handles swiping behaviour
 * - click behaviour (uses {@link IOneClickListener})
 * @author Christian Bergmann
 */

public class SwipeListener implements View.OnTouchListener {

    private boolean dragActive;
    private float fromX;
    private float toX;
    private float fromTempX;
    private float fromY;
    private boolean deleteItem;
    private boolean menuVisible;
    private CBViewHolder holder;
    private int listPosition;
    private IOneClickListener IOneClickListener;

    public SwipeListener(final CBViewHolder holder, final int position, final IOneClickListener IOneClickListener) {
        this.holder = holder;
        this.IOneClickListener = IOneClickListener;
        this.deleteItem = false;
        this.listPosition = position;
        this.dragActive = false;
        this.menuVisible = false;
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                this.fromX = motionEvent.getX();
                this.fromY = motionEvent.getY();
                Log.d("test", "start drag: from (" + this.fromX + ") to (" + this.toX + ")");
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                if (!deleteItem) {

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

                        Log.e("test", "move: " + move + " - Width: " + -this.holder.buttonContainer.getWidth());
                        if (move > -holder.buttonContainer.getWidth() / 2) {
                            Log.d("test", "move drag: from (" + this.fromTempX + ") to (" + move + ")");
                            doAnimation(this.fromTempX, move);
                        } else {
                            this.menuVisible = true;
                        }
                    }
                }
                return true;

            }

            case MotionEvent.ACTION_UP: {
                Log.d("test", "up called");
                if (this.dragActive && this.menuVisible) {
                    Log.d("test", "move end up");
                    doAnimation(this.fromTempX, -this.holder.buttonContainer.getWidth());
                    this.dragActive = false;
                    this.holder.backItem.bringToFront();
                } else {

                    if (!this.dragActive && !this.menuVisible) {
                        Log.d("test", "single click");
                        //rollback();

                        if (!((motionEvent.getY() - this.fromY) > 50 || (this.fromY - motionEvent.getY()) > 50))
                            this.IOneClickListener.handleSingleClick(listPosition);
                    } else {
                        rollback();
                    }

                }
                return true;
            }

            case MotionEvent.ACTION_CANCEL: {
                Log.d("test", "cancel called");

                if (this.dragActive && this.menuVisible) {
                    Log.d("test", "move end cancel");
                    doAnimation(this.fromTempX, -this.holder.buttonContainer.getWidth());
                    this.dragActive = false;
                    this.holder.backItem.bringToFront();
                } else {

                    if (!this.dragActive && !this.menuVisible) {
                        Log.d("test", "single click");
                        //rollback();

                        if (!((motionEvent.getY() - this.fromY) > 50 || (this.fromY - motionEvent.getY()) > 50))
                            this.IOneClickListener.handleSingleClick(listPosition);
                    } else {
                        rollback();
                    }

                }
                return true;
            }

            case MotionEvent.ACTION_HOVER_MOVE:
                Log.d("test", "hover");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("test", "pointer");
                break;
            default:
                break;
        }
        return true;
    }

    public void setIOneClickListener(final IOneClickListener IOneClickListener) {
        this.IOneClickListener = IOneClickListener;
    }

    public void rollback() {
        this.holder.item.bringToFront();
        doAnimation(this.fromTempX, 0);

        this.fromX = 0;
        this.toX = 0;
        this.dragActive = false;
        this.menuVisible = false;
    }

    private void doAnimation(final float startAt, final float endAt) {
        TranslateAnimation animate = new TranslateAnimation(startAt, endAt, 0, 0);
        animate.setDuration(100);
        animate.setFillAfter(true);
        this.holder.item.startAnimation(animate);
    }
}
