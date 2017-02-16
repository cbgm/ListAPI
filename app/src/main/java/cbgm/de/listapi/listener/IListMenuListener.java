package cbgm.de.listapi.listener;

import java.util.List;

import cbgm.de.listapi.data.CBItem;
import cbgm.de.listapi.data.CBListViewItem;


/**
 * Listener for a list items menu when item was swiped to the left
 * @author Christian Bergmann
 */

public interface IListMenuListener<E extends CBListViewItem, M extends CBItem> {
    void handleDelete(final M o);
    void handleEdit(final M o);
    void handleShow(final M o);
    void handleSort(final List<E> list);
}
