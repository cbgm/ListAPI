package cbgm.de.listapi.listener;


/**
 * Listener for passing a click event (single or long press)
 * @author Christian Bergmann
 */


public interface IOneClickListener {
    void handleSingleClick(final int position);
    void handleLongClick(final int position);
}
