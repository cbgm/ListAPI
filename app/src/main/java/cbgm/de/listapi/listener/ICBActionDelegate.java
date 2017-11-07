package cbgm.de.listapi.listener;

import java.util.List;

import cbgm.de.listapi.data.CBListViewItem;

/**
 * Listener to delegate list item click events.
 * @author Christian Bergmann
 */

public interface ICBActionDelegate<E extends CBListViewItem> {
    void delegateDelete(final Object o);
    void delegateEdit(final Object o);
    void delegateShow(final Object o);
    void delegateSort(final List<E> list);
    void delegateSingleClick(final int position);
    void delegateLongClick(final int position);
    void delegateHandleSelect(final int position);
}
