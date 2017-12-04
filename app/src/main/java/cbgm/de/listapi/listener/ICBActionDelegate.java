package cbgm.de.listapi.listener;

import java.util.List;

import cbgm.de.listapi.basic.CBListViewItem;

/**
 * Listener to delegate list item click events.
 * @author Christian Bergmann
 */

public interface ICBActionDelegate<E extends CBListViewItem> {
    void delegateDeleteAction(final Object o);
    void delegateEditAction(final Object o);
    void delegateSortAction(final List<E> list);
    void delegateSingleClickAction(final int position);
    void delegateLongClickAction(final int position);
    void delegateSwipeAction();
    void delegateSelectAction(final int position);
}
