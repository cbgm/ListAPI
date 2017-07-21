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
        final int action = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_MOVE) {
            return this.shouldBlockScroll || super.dispatchTouchEvent(ev);
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
