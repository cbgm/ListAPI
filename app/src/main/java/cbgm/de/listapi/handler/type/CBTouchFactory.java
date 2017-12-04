package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.data.CBListItem;
import cbgm.de.listapi.data.CBListMode;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Factory for touch types (swipe, sort, select).
 * @author Christian Bergmann
 */

public class CBTouchFactory {

    /**
     * Method to get right touch type (swipe, select, sort).
     * @param sequenceList the list of elements
     * @param baseAdapter the adapter
     * @param listContainer the listview
     * @param actionNotifier the listener to pass the touch events
     * @return the CBTouchType
     */
    public static CBTouchType getTouchType(final List<CBListItem> sequenceList, final CBAdapter baseAdapter, final RecyclerView listContainer, final ICBActionNotifier actionNotifier, Context context){
        CBListMode mode = CBModeHelper.getInstance().getListMode();

        switch(mode) {
            case SWIPE:
                return new CBSwipeType(sequenceList, baseAdapter, listContainer, actionNotifier, context);
            case SELECT:
                return new CBSelectType(sequenceList, baseAdapter, listContainer, actionNotifier, context);
            case SORT:
                return new CBSortType(sequenceList, baseAdapter, listContainer, actionNotifier, context);
            default:
                return null;
        }
    }
}
