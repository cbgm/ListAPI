package cbgm.de.listapi.data;

import android.widget.GridLayout;
import android.widget.LinearLayout;


/**
 * Class which defines the basic view holder.
 * @author Christian Bergmann
 */


public abstract class CBViewHolder {
    /* The background item which shows up on swipe */
    public LinearLayout backItem;
    /* The list item menu container which is within the background item */
    public LinearLayout buttonContainer;
    /* The foreground item  */
    public GridLayout item;
    /* The basic delete button */
    public LinearLayout delete;
    /* The basic edit button */
    public LinearLayout edit;
}
