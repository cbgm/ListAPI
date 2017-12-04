package cbgm.de.listapi.handler.type;

import android.content.Context;
import android.widget.ListView;

import java.util.List;

import cbgm.de.listapi.basic.CBAdapter;
import cbgm.de.listapi.data.CBListMode;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Factory for touch types (swipe, sort, select).
 * @author Christian Bergmann
 */

public class CBTouchFactory {

    /**
     * Method to get right touch type.
     * @param sequenceList the list of elements
     * @param baseAdapter the adapter
     * @param listContainer the listview
     * @param actionNotifier the listener to pass the touch events
     * @return the CBTouchType
     */
    public static CBTouchType getTouchType(final List sequenceList, final CBAdapter baseAdapter, final ListView listContainer, final ICBActionNotifier actionNotifier, Context context){
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
