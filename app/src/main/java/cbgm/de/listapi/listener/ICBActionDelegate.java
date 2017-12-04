package cbgm.de.listapi.listener;

import java.util.List;


/**
 * Listener to delegate list item click events.
 * @author Christian Bergmann
 */

public interface ICBActionDelegate {
    void delegateDeleteAction(final Object o);
    void delegateEditAction(final Object o);
    void delegateSortAction(final List list);
    void delegateSingleClickAction(final int position);
    void delegateLongClickAction(final int position);
    void delegateSwipeAction();
    void delegateSelectAction(final int position);
}
