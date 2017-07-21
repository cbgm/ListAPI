package cbgm.de.listapi.listener;

import java.util.List;

import cbgm.de.listapi.data.CBListViewItem;


/**
 * Listener for a list items menu when item was swiped to the left
 * @author Christian Bergmann
 */

public interface IListMenuListener<E extends CBListViewItem> {
    void handleDelete(final Object o);
    void handleEdit(final Object o);
    @SuppressWarnings("unused")
    void handleShow(final Object o);
    void handleSort(final List<E> list);
    void toggleListViewScrolling(final boolean isActive);
}
