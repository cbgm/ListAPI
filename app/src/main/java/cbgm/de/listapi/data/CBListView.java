package cbgm.de.listapi.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Custom ListView to block scrolling action
 * @author Christian Bergmann
 */

public class CBListView extends ListView {
    private int mPosition;
    private boolean shouldBlockScroll = false;

    public CBListView(Context context) {
        super(context);
    }

    public CBListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CBListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            return super.dispatchTouchEvent(ev);
        }

        if (actionMasked == MotionEvent.ACTION_MOVE) {
            return this.shouldBlockScroll || super.dispatchTouchEvent(ev);
        }

        if (actionMasked == MotionEvent.ACTION_UP) {

            if (pointToPosition((int) ev.getX(), (int) ev.getY()) == mPosition) {
                super.dispatchTouchEvent(ev);
            } else {
                setPressed(false);
                invalidate();
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * Set true to block scrolling
     * @param shouldBlockScroll true blocks
     */
    public void setShouldScroll(final boolean shouldBlockScroll) {
        this.shouldBlockScroll = shouldBlockScroll;
    }
}
