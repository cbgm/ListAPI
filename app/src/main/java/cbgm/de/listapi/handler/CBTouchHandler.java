package cbgm.de.listapi.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.data.CBListItem;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.handler.type.CBTouchFactory;
import cbgm.de.listapi.handler.type.CBTouchType;
import cbgm.de.listapi.listener.ICBActionNotifier;


public class CBTouchHandler<A extends CBAdapter> implements View.OnTouchListener{

    //the list item data
    private List<CBListItem> data;
    //the notifier when a list action is triggered
    private ICBActionNotifier actionNotifier;
    //the recycler view for the items
    private RecyclerView listContainer;
    //the recycler adapter
    private A adapter;
    //the current touch type (swipe, select, sort)
    private CBTouchType touchType;

    private CBModeHelper modeHelper;
    //the application context
    private Context context;

    public CBTouchHandler(final List<CBListItem> data, A adapter, RecyclerView listContainer, ICBActionNotifier actionNotifier, Context context) {
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
        return touchType.onTouch(view, motionEvent);
    }

    /**
     * Method to clean the touch type if needed.
     */
    public void cleanTouch() {
        this.touchType.cleanTouch();
    }

    /**
     * Method should be called when there was a button click on the list item menu.
     */
    public void informButtonClick() {
        cleanTouch();
        this.modeHelper.setButtonClicked(true);
    }
}
