package cbgm.de.listapi.handler;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.basic.CBListViewItem;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.handler.type.CBTouchFactory;
import cbgm.de.listapi.handler.type.CBTouchType;
import cbgm.de.listapi.listener.ICBActionNotifier;


public class CBTouchHandler<E extends CBListViewItem, T extends CBAdapter> implements View.OnTouchListener{

    private List<E> sequenceList;

    private ICBActionNotifier actionNotifier;

    private ListView listContainer;

    private T adapter;

    private CBTouchType touchType;

    private CBModeHelper modeHelper;

    private Context context;

    public CBTouchHandler(final List<E> sequenceList, T baseAdapter, ListView listContainer, ICBActionNotifier actionNotifier, Context context) {
        this.sequenceList = sequenceList;
        this.adapter = baseAdapter;
        this.listContainer = listContainer;
        this.actionNotifier = actionNotifier;
        this.modeHelper = CBModeHelper.getInstance();
        this.context  = context;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (this.modeHelper.isModeChanged() || this.touchType == null) {
                this.touchType = CBTouchFactory.getTouchType(sequenceList, adapter, listContainer, actionNotifier, context);
                this.modeHelper.resetModeChanged();
        }
        return touchType.onTouch(view, motionEvent);
    }


    public void cleanTouch() {
        this.touchType.cleanTouch();
    }

    public void informButtonClick() {
        cleanTouch();
        this.modeHelper.setButtonClicked(true);
    }
}
