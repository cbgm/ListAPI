package cbgm.de.listapi.listener;

import java.util.List;

import cbgm.de.listapi.data.CBListViewItem;

/**
 * Created by SA_Admin on 26.10.2017.
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
