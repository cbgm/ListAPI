package cbgm.de.listapi.listener;

import java.util.List;


/**
 * Listener for a list items actions
 * @author Christian Bergmann
 */

public interface ICBActionNotifier {
    void deleteAction(final Object o);
    void editAction(final Object o);
    void sortAction(final List list);
    void swipeAction();
    void singleClickAction(final int position);
    void longClickAction(final int position);
}
