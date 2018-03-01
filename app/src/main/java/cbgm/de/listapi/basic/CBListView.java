package cbgm.de.listapi.basic;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import cbgm.de.listapi.data.CBListMode;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.handler.CBTouchHandler;
import cbgm.de.listapi.listener.ICBActionDelegate;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Custom ListView to block scrolling action
 * @author Christian Bergmann
 */

@SuppressWarnings("unused")
public class CBListView<I> extends RecyclerView implements ICBActionNotifier<I> {
    /*Listener to forward list item click events*/
    protected ICBActionDelegate<I> deletegateListener;
    //touch handler for switching the possible touch types (swipe, sort, select)
    protected CBTouchHandler<I> touchHandler;
    //protected ArrayList<I> data = new ArrayList<>();
    protected CBModeHelper modeHelper = CBModeHelper.getInstance();
    //tells if TouchHandler should be enabled for (swipe ,select, sort)
    protected boolean isTouchable = true;

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
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        final int action = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_MOVE && modeHelper.getListMode() == CBListMode.SWIPE) {
            return this.modeHelper.isSwipeActive() || super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void deleteAction(final I o) {
        Log.d("LIST_API", "button delete clicked");
        this.touchHandler.informButtonClick();
        deletegateListener.delegateDeleteAction(o);
    }

    @Override
    public void editAction(final I o) {
        Log.d("LIST_API", "button edit clicked");
        this.touchHandler.informButtonClick();
        deletegateListener.delegateEditAction(o);
    }

    @Override
    public void sortAction(List<I> list) {
        Log.d("LIST_API", "item sorted");
        deletegateListener.delegateSortAction(list);
    }

    @Override
    public void swipeAction() {
        deletegateListener.delegateSwipeAction();
    }

    @Override
    public void singleClickAction(final int position) {

        if (this.modeHelper.getListMode() == CBListMode.SELECT) {
            deletegateListener.delegateSelectAction(position);
            if (this.getAdapter().getSelectedItemCount() == 0) {
                this.modeHelper.setListMode(CBListMode.SWIPE);
                Log.d("LIST API", "off select");
            }
            return;
        }
        deletegateListener.delegateSingleClickAction(position);

    }

    @Override
    public void longClickAction(final int position) {

        if (this.modeHelper.getListMode() != CBListMode.SELECT) {
            this.modeHelper.setListMode(CBListMode.SELECT);
            deletegateListener.delegateSelectAction(position);
        }
        deletegateListener.delegateLongClickAction(position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CBAdapter<I> getAdapter() {
        return (CBAdapter<I>) super.getAdapter();
    }

    public void setup(ArrayList<I> data, List<CBAdapterDelegate> delegates) {
        setHasFixedSize(true);
        setLayoutManager(new CBLayoutManager(getContext()));
        setAdapter(new CBAdapter<>(getContext(), data, this));
        if (this.isTouchable) {
            this.touchHandler = new CBTouchHandler<>(data, getAdapter(), this, this, getContext());
            this.setOnTouchListener(touchHandler);
        } else {
            this.setOnTouchListener(null);
        }
        for (CBAdapterDelegate delegate : delegates)
            (getAdapter()).addAdapterDelegate(delegate);
        this.modeHelper.setListMode(CBListMode.SWIPE);
        getAdapter().notifyDataSetChanged();
    }

    public void setup(List<I> data, List<CBAdapterDelegate> delegates, ICBActionDelegate<I> delegateListener) {
        this.deletegateListener = delegateListener;
        setHasFixedSize(true);
        setLayoutManager(new CBLayoutManager(getContext()));
        setAdapter(new CBAdapter<>(getContext(), data, this));
        if (this.isTouchable) {
            this.touchHandler = new CBTouchHandler<>(data, getAdapter(), this, this, getContext());
            this.setOnTouchListener(touchHandler);
        } else {
            this.setOnTouchListener(null);
        }
        for (CBAdapterDelegate delegate : delegates)
            getAdapter().addAdapterDelegate(delegate);
        this.modeHelper.setListMode(CBListMode.SWIPE);
        getAdapter().notifyDataSetChanged();
    }

    /**
     * Method to set the delegate listener for forwarding list item click events
     * @param delegateListener the listener
     */
    public void setDelegateListener(final ICBActionDelegate<I> delegateListener) {
        this.deletegateListener = delegateListener;
    }

    /**
     * Method to get the touch handler (can bes used to clean up touch events)
     * @return the CBTouchHandler
     */
    public CBTouchHandler<I> getTouchHandler() {
        return this.touchHandler;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * Method to disable the touch abilities of the list.
     * Should be called before the setup() method.
     * @param isTouchable the isTouchable
     */
    public void setActiveModes(final boolean isTouchable) {
        this.isTouchable = isTouchable;
    }

    public class CBLayoutManager extends LinearLayoutManager {
        private static final float MILLISECONDS_PER_INCH = 125f;
        public CBLayoutManager(Context context) {
            super(context);
        }

        public CBLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CBLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean canScrollVertically() {
            return modeHelper.isScrollingAllowed() && super.canScrollVertically();
        }

        @Override
        public void scrollToPosition(int position) {
            this.smoothScrollToPosition(CBListView.this, null, position);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

            final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                @Override
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return CBLayoutManager.this.computeScrollVectorForPosition(targetPosition);
                }

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                }
            };

            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

    public ICBActionNotifier<I> getActionNotifier() {
        return this;
    }

}