package cbgm.de.listapi.listener;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import cbgm.de.listapi.data.CBViewHolder;

/**
 * Listener for handling touch events of list items
 * - handles swiping behaviour
 * @author Christian Bergmann
 */

public class CBSelectListener implements View.OnClickListener {
    /* The list items position */
    private int listPosition;
    /* The listener to handle a single click */
    private ICBActionNotifier iListMenuListener;
    /*Tells if item is selected*/
    private boolean isSelected = false;
    /* The list item view holder */
    private CBViewHolder holder;
    /*color for selected item*/
    private int highlightColor = Color.LTGRAY;//Color.rgb(219,235,226);
    /*Holder for the first selected position*/
    private int firstSelectedPosition = -1;

    /**
     * Constructor
     * @param position the items position
     * @param iListMenuListener the listener to handle a single click
     */
    public CBSelectListener(final CBViewHolder holder, final int position, final ICBActionNotifier iListMenuListener, final boolean isSelected, final int firstSelectedPosition) {
        this.iListMenuListener = iListMenuListener;
        this.listPosition = position;
        this.isSelected = isSelected;
        this.holder = holder;
        this.firstSelectedPosition = firstSelectedPosition;

        if (firstSelectedPosition == position)
            this.isSelected = true;

        holder.item.setBackgroundColor(this.isSelected? highlightColor : Color.WHITE);
    }

    @SuppressWarnings("unused")
    public void setClickListener(final ICBActionNotifier iListMenuListener) {
        this.iListMenuListener = iListMenuListener;
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
        iListMenuListener.handleSingleClick(listPosition);
    }
}

/*
package cbgm.de.listapi.listener;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import cbgm.de.listapi.data.CBViewHolder;

*/
/**
 * Listener for handling touch events of list items
 * - handles swiping behaviour
 * @author Christian Bergmann
 *//*


public class CBSelectListener implements View.OnTouchListener {
    */
/* The list items position *//*

    private int listPosition;
    */
/* The listener to handle a single click *//*

    private ICBActionNotifier iListMenuListener;
    */
/*Tells if item is selected*//*

    private boolean isSelected = false;
    */
/* The list item view holder *//*

    private CBViewHolder holder;
    */
/*color for selected item*//*

    private int highlightColor = Color.LTGRAY;//Color.rgb(219,235,226);
    */
/*Holder for the first selected position*//*

    private int firstSelectedPosition = -1;

    */
/**
     * Constructor
     * @param position the items position
     * @param iListMenuListener the listener to handle a single click
     *//*

    public CBSelectListener(final CBViewHolder holder, final int position, final ICBActionNotifier iListMenuListener, final boolean isSelected, final int firstSelectedPosition) {
        this.iListMenuListener = iListMenuListener;
        this.listPosition = position;
        this.isSelected = isSelected;
        this.holder = holder;
        this.firstSelectedPosition = firstSelectedPosition;
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                return true;
            }

            case MotionEvent.ACTION_UP: {
                if (((ColorDrawable)holder.item.getBackground()).getColor() == Color.WHITE) {
                    holder.item.setBackgroundColor(highlightColor);
                    isSelected = true;
                } else {
                    holder.item.setBackgroundColor(Color.WHITE);
                    isSelected = false;
                }
                holder.item.setBackgroundColor(isSelected? highlightColor : Color.WHITE);
                this.iListMenuListener.handleSingleClick(listPosition);
                return true;
            }

            case MotionEvent.ACTION_CANCEL: {
                if (((ColorDrawable)holder.item.getBackground()).getColor() == Color.WHITE) {
                    holder.item.setBackgroundColor(highlightColor);
                    isSelected = true;
                } else {
                    holder.item.setBackgroundColor(Color.WHITE);
                    isSelected = false;
                }
                holder.item.setBackgroundColor(isSelected? highlightColor : Color.WHITE);
                this.iListMenuListener.handleSingleClick(listPosition);
                return true;
            }
            default:
                break;
        }
        return true;
    }

    @SuppressWarnings("unused")
    public void setClickListener(final ICBActionNotifier iListMenuListener) {
        this.iListMenuListener = iListMenuListener;
    }
}*/
