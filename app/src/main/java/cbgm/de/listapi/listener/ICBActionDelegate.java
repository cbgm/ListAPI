package cbgm.de.listapi.listener;

import java.util.List;


/**
 * Listener to delegate list item click events.
 * @author Christian Bergmann
 */

public interface ICBActionDelegate<I> {
    void delegateDeleteAction(final I o);
    void delegateEditAction(final I o);
    void delegateSortAction(final List<I> list);
    void delegateSingleClickAction(final int position);
    void delegateLongClickAction(final int position);
    void delegateSwipeAction();
    void delegateSelectAction(final int position);
}
