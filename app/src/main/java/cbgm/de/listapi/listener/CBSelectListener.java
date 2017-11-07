package cbgm.de.listapi.listener;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import cbgm.de.listapi.data.CBViewHolder;

/**
 * Listener for handling the selection of list items.
 * @author Christian Bergmann
 */

public class CBSelectListener implements View.OnClickListener {
    /* The list items position */
    private int listPosition;
    /* The listener to handle a single click */
    private ICBActionNotifier actionNotifier;
    /*Tells if item is selected*/
    private boolean isSelected = false;
    /* The list item view holder */
    private CBViewHolder holder;
    /*color for selected item*/
    private int highlightColor = Color.LTGRAY;//Color.rgb(219,235,226);

    /**
     * Constructor
     * @param position the items position
     * @param actionNotifier the listener to handle a single click
     */
    public CBSelectListener(final CBViewHolder holder, final int position, final ICBActionNotifier actionNotifier, final boolean isSelected, final int firstSelectedPosition) {
        this.actionNotifier = actionNotifier;
        this.listPosition = position;
        this.isSelected = isSelected;
        this.holder = holder;

        if (firstSelectedPosition == position)
            this.isSelected = true;

        holder.item.setBackgroundColor(this.isSelected? highlightColor : Color.WHITE);
    }

    @SuppressWarnings("unused")
    public void setClickListener(final ICBActionNotifier actionNotifier) {
        this.actionNotifier = actionNotifier;
    }

    @Override
    public void onClick(View view) {

        if (((ColorDrawable)holder.item.getBackground()).getColor() == Color.WHITE) {
            holder.item.setBackgroundColor(highlightColor);
            isSelected = true;
        } else {
            holder.item.setBackgroundColor(Color.WHITE);
            isSelected = false;
        }
        actionNotifier.handleSingleClick(listPosition);
    }
}