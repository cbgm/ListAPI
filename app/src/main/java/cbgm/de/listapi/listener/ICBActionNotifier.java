package cbgm.de.listapi.listener;

import java.util.List;


/**
 * Listener for a list items actions
 * @author Christian Bergmann
 */

public interface ICBActionNotifier<I> {
    void deleteAction(final I o);
    void editAction(final I o);
    void sortAction(final List<I> list);
    void swipeAction();
    void singleClickAction(final int position);
    void longClickAction(final int position);
}
