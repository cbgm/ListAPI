package cbgm.de.listapi.listener;

/**
 * Listener to notify when swipe of listitem was successful
 * @author Christian Bergmann
 */

public interface ICBSwipeNotifier {
    void swipeActive(final boolean isActive);
}
