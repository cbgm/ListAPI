package cbgm.de.listapi.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cbgm.de.listapi.R;
import cbgm.de.listapi.listener.IListMenuListener;
import cbgm.de.listapi.listener.ISwipeNotifier;
import cbgm.de.listapi.listener.SwipeListener;


/**
 * Class which defines a listview item
 * @author Christian Bergmann
 */

public abstract class CBListViewItem<V extends CBViewHolder, M> implements ISwipeNotifier{
    /* The data element of the list view item */
    protected M item;
    /* The view holder */
    protected V holder;
    /* The id of the layout to inflate */
    protected int itemResource;
    /* The application context */
    private Context context;
    /* Set if delete button should be added */
    protected boolean addDelete = false;
    /* Set if edit button should be added */
    protected boolean addEdit = false;
    /* Any other custom buttons (extend from CBBaseButton) */
    protected List<CBBaseButton> customButtons;
    /*Holder for the first selected position*/
    private int firstSelectedPosition = -1;
    /*Tells if item is selected*/
    private boolean isSelected = false;
    /*color for selected item*/
    private int highlightColor = Color.LTGRAY;//Color.rgb(219,235,226);

    private IListMenuListener listMenuListener;

    /**
     * Constructor
     * @param item the list item data
     * @param holder the view holder
     * @param itemResource the id of the layout to inflate which represents the foreground
     */
    public CBListViewItem(final M item, final V holder, final int itemResource, final int firstSelectedPosition) {
        this.item = item;
        this.itemResource = itemResource;
        this.firstSelectedPosition = firstSelectedPosition;
        this.holder = holder;
        this.customButtons = new ArrayList<>();
    }

    public V getHolder() {
        return holder;
    }

    public void setHolder(V holder) {
        this.holder = holder;
    }

    public int getItemResource() {
        return itemResource;
    }

    public void setItemResource(int itemResource) {
        this.itemResource = itemResource;
    }

    public M getItem() {
        return item;
    }

    public void setItem(final M item) {
        this.item = item;
    }

    /**
     * Method to get the expected view.
     * @param position the current postion of the item
     * @param convertView the convert view
     * @param parent the parent view
     * @param isSortMode tells if in sort mode
     * @param listMenuListener the listener for clicks
     * @param highlightPos the position to highlight
     * @param inflater the inflater
     * @return the convert view
     */
    public View getConvertView(final int position, View convertView, final ViewGroup parent, final boolean isSortMode, final boolean isSelectMode, final IListMenuListener listMenuListener, final int highlightPos, final LayoutInflater inflater, final Context context) {
        convertView = isSortMode || isSelectMode ? null: convertView;
        this.context = context;
        this.listMenuListener = listMenuListener;

        if (convertView == null) {
            convertView = prepareView(parent, inflater, context);
        } else {
            if (!((convertView.getTag()).getClass().isInstance(holder))){
                convertView = prepareView(parent, inflater, context);
            }
        }
        holder = (V) convertView.getTag();
        final SwipeListener swipeListener = new SwipeListener(holder, position, listMenuListener, this);

        if (!isSortMode && !isSelectMode) {
            holder.item.setOnTouchListener(swipeListener);
            holder.item.setBackgroundColor(Color.WHITE);

            if (addEdit) {
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        swipeListener.rollback();
                        listMenuListener.handleEdit(item);
                        swipeActive(false);
                    }
                });
            }

            if (addDelete) {
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        swipeListener.rollback();
                        listMenuListener.handleDelete(item);
                        swipeActive(false);
                    }
                });
            }

            holder.backItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeListener.rollback();
                    swipeActive(false);
                }
            });

        } else {
            holder.item.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            if (isSortMode) {

                if (highlightPos == position && highlightPos != -1) {
                    holder.item.setBackgroundColor(highlightColor);
                } else {
                    holder.item.setBackgroundColor(Color.WHITE);
                }
            } else {
                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((ColorDrawable)holder.item.getBackground()).getColor() == Color.WHITE) {
                            holder.item.setBackgroundColor(highlightColor);
                            isSelected = true;
                        } else {
                            holder.item.setBackgroundColor(Color.WHITE);
                            isSelected = false;
                        }
                        listMenuListener.handleSingleClick(position);
                    }
                });

                if (firstSelectedPosition == position) {
                    firstSelectedPosition = -1;
                    isSelected = true;
                    holder.item.setBackgroundColor(highlightColor);
                }
                holder.item.setBackgroundColor(isSelected? highlightColor : Color.WHITE);
            }
        }
        setUpView(position, convertView, parent, isSortMode, isSelectMode, listMenuListener, highlightPos, inflater, swipeListener, context);
        return convertView;
    }

    /**
     * Method to prepare the current listview if necessary
     * @param parent the {@link ViewGroup}
     * @return the view
     */
    protected View prepareView(final ViewGroup parent, final LayoutInflater inflater, final Context context) {
        View itemView = BaseView.getView(context);
        holder.item = (GridLayout)itemView.findViewById(LayoutID.ITEM_FOREGROUND_ID);
        holder.buttonContainer = (LinearLayout) itemView.findViewById(LayoutID.BUTTON_CONTAINER_ID);

        if (addEdit) {
            holder.buttonContainer.addView(new CBBaseButton().getButton(LayoutID.EDIT_BUTTON_ID, context, R.color.cb_edit_background_color, R.mipmap.edit_icon));
            holder.edit = (LinearLayout) itemView.findViewById(LayoutID.EDIT_BUTTON_ID);
        }

        if (addDelete) {
            holder.buttonContainer.addView(new CBBaseButton().getButton(LayoutID.DELETE_BUTTON_ID, context, R.color.cb_delete_background_color, R.mipmap.trash_icon));
            holder.delete = (LinearLayout) itemView.findViewById(LayoutID.DELETE_BUTTON_ID);
        }

        for (CBBaseButton customButton : customButtons) {
            holder.buttonContainer.addView(customButton.getCustomButton(context));
        }
        holder.backItem = (LinearLayout) itemView.findViewById(LayoutID.ITEM_BACKGROUND_ID);
        View personalView = inflater.inflate(this.itemResource, parent, false);
        holder.item.addView(personalView);
        holder = initView(itemView, context);
        itemView.setTag(holder);
        return itemView;
    }

    /**
     * Method for setting up the view functionality (values, listeners).
     * @param position the current postion of the item
     * @param convertView the convert view
     * @param parent the parent view
     * @param isSortMode tells if in sort mode
     * @param listMenuListener the listener for clicks
     * @param highlightPos the position to highlight
     * @param inflater the inflater
     * @param swipeListener the swipe listener
     * @param context the context
     */
    public abstract V setUpView(final int position, View convertView, final ViewGroup parent, final boolean isSortMode, final boolean isSelectMode, final IListMenuListener listMenuListener, final int highlightPos, final LayoutInflater inflater, final SwipeListener swipeListener, Context context);

    /**
     * Method for initializing the view.
     * @param itemView the convert view
     */
    public abstract V initView(View itemView, Context context);

    public void swipeActive(final boolean isActive) {
        this.listMenuListener.toggleListViewScrolling(isActive);
        Log.d("LIST API", "isactive" + isActive);
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }
}