package cbgm.de.listapi.listener;

import java.util.List;

import cbgm.de.listapi.basic.CBListViewItem;


/**
 * Listener for a list items actions
 * @author Christian Bergmann
 */

public interface ICBActionNotifier<E extends CBListViewItem> {
    void deleteAction(final Object o);
    void editAction(final Object o);
    void sortAction(final List<E> list);
    void swipeAction();
    void singleClickAction(final int position);
    void longClickAction(final int position);
}
