package cbgm.de.listapi.data;

import android.content.Context;
import android.graphics.Color;
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
import cbgm.de.listapi.listener.CBListMode;
import cbgm.de.listapi.listener.CBSelectListener;
import cbgm.de.listapi.listener.CBSwipeListener;
import cbgm.de.listapi.listener.ICBActionNotifier;
import cbgm.de.listapi.listener.ICBSwipeNotifier;


/**
 * Class which defines a listview item
 * @author Christian Bergmann
 */

public abstract class CBListViewItem<V extends CBViewHolder, M> implements ICBSwipeNotifier {
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
    /*The listener for click events*/
    private ICBActionNotifier listMenuListener;

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
     * @param mode tells the mode of the list
     * @param listMenuListener the listener for clicks
     * @param highlightPos the position to highlight
     * @param inflater the inflater
     * @return the convert view
     */
    public View getConvertView(final int position, View convertView, final ViewGroup parent, final CBListMode mode, final ICBActionNotifier listMenuListener, final int highlightPos, final LayoutInflater inflater, final Context context) {
        convertView = mode == CBListMode.SORT ? null: convertView;
        this.context = context;
        this.listMenuListener = listMenuListener;

        if (convertView == null) {
            convertView = prepareView(parent, inflater, context);
        } else {
            if (!((convertView.getTag()).getClass().isInstance(this.holder))){
                convertView = prepareView(parent, inflater, context);
            }
        }
        this.holder = (V) convertView.getTag();

        final CBSwipeListener swipeListener = new CBSwipeListener(holder, position, listMenuListener, this);
        final CBSelectListener selectListener = new CBSelectListener(holder, position, listMenuListener , isSelected, firstSelectedPosition);
        this.holder.item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        switch (mode) {
            case SWIPE:
                this.holder.item.setOnTouchListener(swipeListener);
                this.holder.item.setBackgroundColor(Color.WHITE);

                if (addEdit) {
                    this.holder.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            swipeListener.rollback();
                            listMenuListener.handleEdit(item);
                            swipeActive(false);
                        }
                    });
                }

                if (addDelete) {
                    this.holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            swipeListener.rollback();
                            listMenuListener.handleDelete(item);
                            swipeActive(false);
                        }
                    });
                }

                this.holder.backItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeListener.rollback();
                        swipeActive(false);
                    }
                });
                break;
            case SELECT:
                this.holder.item.setOnClickListener(selectListener);
                break;
            case SORT:
                //highlight item with different color if dragged
                if (highlightPos == position && highlightPos != -1) {
                    this.holder.item.setBackgroundColor(highlightColor);
                } else {
                    this.holder.item.setBackgroundColor(Color.WHITE);
                }
                break;
            default:
                break;
        }
        setUpView(position, convertView, parent, mode, listMenuListener, highlightPos, inflater, swipeListener, context);
        return convertView;
    }

    /**
     * Method to prepare the current listview if necessary
     * @param parent the {@link ViewGroup}
     * @return the view
     */
    protected View prepareView(final ViewGroup parent, final LayoutInflater inflater, final Context context) {
        View itemView = BaseView.getView(context);
        this.holder.item = (GridLayout)itemView.findViewById(LayoutID.ITEM_FOREGROUND_ID);
        this.holder.buttonContainer = (LinearLayout) itemView.findViewById(LayoutID.BUTTON_CONTAINER_ID);

        if (addEdit) {
            this.holder.buttonContainer.addView(new CBBaseButton().getButton(LayoutID.EDIT_BUTTON_ID, context, R.color.cb_edit_background_color, R.mipmap.edit_icon));
            this.holder.edit = (LinearLayout) itemView.findViewById(LayoutID.EDIT_BUTTON_ID);
        }

        if (addDelete) {
            this.holder.buttonContainer.addView(new CBBaseButton().getButton(LayoutID.DELETE_BUTTON_ID, context, R.color.cb_delete_background_color, R.mipmap.trash_icon));
            this.holder.delete = (LinearLayout) itemView.findViewById(LayoutID.DELETE_BUTTON_ID);
        }

        for (CBBaseButton customButton : customButtons) {
            this.holder.buttonContainer.addView(customButton.getCustomButton(context));
        }
        this.holder.backItem = (LinearLayout) itemView.findViewById(LayoutID.ITEM_BACKGROUND_ID);
        View personalView = inflater.inflate(this.itemResource, parent, false);
        this.holder.item.addView(personalView);
        this.holder = initView(itemView, context);
        itemView.setTag(this.holder);
        return itemView;
    }

    /**
     * Method for setting up the view functionality (values, listeners).
     * @param position the current postion of the item
     * @param convertView the convert view
     * @param parent the parent view
     * @param mode tells the mode of the list
     * @param listMenuListener the listener for clicks
     * @param highlightPos the position to highlight
     * @param inflater the inflater
     * @param swipeListener the swipe listener
     * @param context the context
     */
    public abstract V setUpView(final int position, View convertView, final ViewGroup parent, final CBListMode mode, final ICBActionNotifier listMenuListener, final int highlightPos, final LayoutInflater inflater, final CBSwipeListener swipeListener, Context context);

    /**
     * Method for initializing the view.
     * @param itemView the convert view
     */
    public abstract V initView(View itemView, Context context);

    public void swipeActive(final boolean isActive) {
        this.listMenuListener.toggleListViewScrolling(isActive);
        Log.d("LIST API", "isactive" + isActive);
    }

    /**
     * Method to set the highlight color.
     * (necessary for sort and select mode)
     * @param highlightColor the color
     */
    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    /**
     * Method to set the first selected position when select is activated and listeners are switched.
     * @param pos the position
     */
    public void setFirstSelectedPosition(int pos) {
        this.firstSelectedPosition = pos;
    }
}