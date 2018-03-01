package cbgm.de.listapi.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBListView;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.handler.type.CBTouchFactory;
import cbgm.de.listapi.handler.type.CBTouchType;
import cbgm.de.listapi.listener.ICBActionNotifier;


public class CBTouchHandler<I> implements View.OnTouchListener{

    //the list item data
    private List<I> data;
    //the notifier when a list action is triggered
    private ICBActionNotifier<I> actionNotifier;
    //the recycler view for the items
    private CBListView listContainer;
    //the recycler adapter
    private CBAdapter<I> adapter;
    //the current touch type (swipe, select, sort)
    private CBTouchType touchType;

    private CBModeHelper modeHelper;
    //the application context
    private Context context;

    public CBTouchHandler(final List<I> data, CBAdapter<I> adapter, CBListView listContainer, ICBActionNotifier<I> actionNotifier, Context context) {
        this.data = data;
        this.adapter = adapter;
        this.listContainer = listContainer;
        this.actionNotifier = actionNotifier;
        this.modeHelper = CBModeHelper.getInstance();
        this.context  = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (this.modeHelper.isModeChanged() || this.touchType == null) {
                this.touchType = CBTouchFactory.getTouchType(data, adapter, listContainer, actionNotifier, context);
                this.modeHelper.resetModeChanged();
        }
        return touchType != null && touchType.onTouch(view, motionEvent);
    }

    /**
     * Method to clean the touch type if needed.
     */
    public void cleanTouch() {
        this.touchType.cleanTouch();
    }

    /**
     * Method should be called when there was a button click on the list item menu (depending on swipe).
     */
    public void informButtonClick() {
        cleanTouch();
        this.modeHelper.setButtonClicked(true);
    }
}
