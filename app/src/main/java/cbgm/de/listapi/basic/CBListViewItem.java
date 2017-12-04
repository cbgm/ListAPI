package cbgm.de.listapi.basic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cbgm.de.listapi.R;
import cbgm.de.listapi.data.CBLayoutID;
import cbgm.de.listapi.data.CBListMode;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.listener.ICBActionNotifier;


/**
 * Class which defines a listview item
 * @author Christian Bergmann
 */

public abstract class CBListViewItem<V extends CBViewHolder, M> {
    /* The data element of the list view item */
    protected M item;
    /* The view holder */
    protected V holder;
    /* The id of the layout to inflate */
    protected int itemResource;
    /* Set if delete button should be added */
    protected boolean addDelete = false;
    /* Set if edit button should be added */
    protected boolean addEdit = false;
    /* Any other custom buttons (extend from CBBaseButton) */
    protected List<CBBaseButton> customButtons;
    /*color for selected item*/
    protected int highlightColor = Color.LTGRAY;//Color.rgb(219,235,226);

    protected CBModeHelper modeHelper;

    /**
     * Constructor
     * @param item the list item data
     * @param holder the view holder
     * @param itemResource the id of the layout to inflate which represents the foreground
     */
    public CBListViewItem(final M item, final V holder, final int itemResource) {
        this.item = item;
        this.itemResource = itemResource;
        this.holder = holder;
        this.customButtons = new ArrayList<>();
        this.modeHelper = CBModeHelper.getInstance();
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
     * @param actionNotifier the listener for clicks
     * @param inflater the inflater
     * @return the convert view
     */
    public View getConvertView(final int position, View convertView, final ViewGroup parent, final ICBActionNotifier actionNotifier, final LayoutInflater inflater, final Context context) {
        convertView = this.modeHelper.getListMode() == CBListMode.SORT ? null: convertView;

        if (convertView == null) {
            convertView = prepareView(parent, inflater, context);
        } else {
            if (!((convertView.getTag()).getClass().isInstance(this.holder))){
                convertView = prepareView(parent, inflater, context);
            }
        }
        this.holder = (V) convertView.getTag();

        CBListMode mode = this.modeHelper.getListMode();

        switch (mode) {
            case SWIPE:
                this.holder.item.setBackgroundColor(Color.WHITE);

                if (addEdit) {
                   this.holder.edit.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                       public boolean onTouch(View v, MotionEvent event) {

                           if (modeHelper.isItemTouchCurrentItem(position)) {
                               actionNotifier.editAction(item);
                               return true;
                           }
                           return false;
                       }
                   });
                }

                if (addDelete) {
                    this.holder.delete.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (modeHelper.isItemTouchCurrentItem(position)) {
                                actionNotifier.deleteAction(item);
                                return true;
                            }
                            return false;
                        }
                    });
                }
                break;
            case SELECT:

                break;
            case SORT:
                //highlight item with different color if dragged
                if (this.modeHelper.getSelectedPosition() == position) {
                    this.holder.item.setBackgroundColor(highlightColor);
                } else {
                    this.holder.item.setBackgroundColor(Color.WHITE);
                }
                break;
            default:
                break;
        }
        setUpView(position, convertView, parent, actionNotifier, inflater, context);
        return convertView;
    }

    /**
     * Method to prepare the current listview if necessary
     * @param parent the {@link ViewGroup}
     * @return the view
     */
    protected View prepareView(final ViewGroup parent, final LayoutInflater inflater, final Context context) {
        View itemView = CBBaseView.getView(context);
        this.holder.item = (GridLayout)itemView.findViewById(CBLayoutID.ITEM_FOREGROUND_ID);
        this.holder.buttonContainer = (LinearLayout) itemView.findViewById(CBLayoutID.BUTTON_CONTAINER_ID);

        if (addEdit) {
            this.holder.buttonContainer.addView(new CBBaseButton().getButton(CBLayoutID.EDIT_BUTTON_ID, context, R.color.cb_edit_background_color, R.mipmap.edit_icon));
            this.holder.edit = (LinearLayout) itemView.findViewById(CBLayoutID.EDIT_BUTTON_ID);
        }

        if (addDelete) {
            this.holder.buttonContainer.addView(new CBBaseButton().getButton(CBLayoutID.DELETE_BUTTON_ID, context, R.color.cb_delete_background_color, R.mipmap.trash_icon));
            this.holder.delete = (LinearLayout) itemView.findViewById(CBLayoutID.DELETE_BUTTON_ID);
        }

        for (CBBaseButton customButton : customButtons) {
            this.holder.buttonContainer.addView(customButton.getCustomButton(context));
        }
        this.holder.backItem = (LinearLayout) itemView.findViewById(CBLayoutID.ITEM_BACKGROUND_ID);
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
     * @param listMenuListener the listener for clicks
     * @param inflater the inflater
     * @param context the context
     */
    public abstract V setUpView(final int position, View convertView, final ViewGroup parent, final ICBActionNotifier listMenuListener, final LayoutInflater inflater, Context context);

    /**
     * Method for initializing the view.
     * @param itemView the convert view
     */
    public abstract V initView(View itemView, Context context);

}