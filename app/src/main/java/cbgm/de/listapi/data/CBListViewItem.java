package cbgm.de.listapi.data;

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
import cbgm.de.listapi.listener.IListMenuListener;
import cbgm.de.listapi.listener.IOneClickListener;
import cbgm.de.listapi.listener.SwipeListener;


/**
 * The definition of a listview items data
 * @author Christian Bergmann
 */

public abstract class CBListViewItem<V extends CBViewHolder, M>{
    protected M item;
    protected V holder;
    protected int itemResource;
    private Context context;
    protected boolean addDelete = false;
    protected boolean addEdit = false;
    protected List<CBBaseButton> customButtons;

    public CBListViewItem(final M item, final V holder, final int itemResource) {
        this.item = item;
        this.itemResource = itemResource;
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
     * @param parent
     * @param isSortMode tells if in sort mode
     * @param listMenuListener the listener for clicks
     * @param highlightPos the position to highlight
     * @param oneClickListener the single click listener
     * @param inflater the inflater
     * @return the convert view
     */
    public View getConvertView(final int position, View convertView, final ViewGroup parent, final boolean isSortMode, final IListMenuListener listMenuListener, final int highlightPos, final IOneClickListener oneClickListener, final LayoutInflater inflater, final Context context) {
        convertView = isSortMode ? null: convertView;
        this.context = context;

        if (convertView == null) {
            convertView = prepareView(parent, inflater, context);
        }
        holder = (V) convertView.getTag();
        final SwipeListener swipeListener = new SwipeListener(holder, position, oneClickListener);

        if (!isSortMode) {
            holder.item.setOnTouchListener(swipeListener);

            if (addEdit) {
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        swipeListener.rollback();
                        listMenuListener.handleEdit(item);
                    }
                });
            }

            if (addDelete) {
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        swipeListener.rollback();
                        listMenuListener.handleDelete(item);
                    }
                });
            }
        } else {
            holder.item.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            if (highlightPos == position && highlightPos != -1) {
                holder.item.setBackgroundColor(Color.rgb(219,235,226));
            } else {
                holder.item.setBackgroundColor(Color.WHITE);
            }
        }
        setUpView(position, convertView, parent, isSortMode, listMenuListener, highlightPos, oneClickListener, inflater, swipeListener);
        return convertView;
    }

    /**
     * Method to prepare the current listview item if necessary
     * @param parent the {@link ViewGroup}
     * @return the view
     */
    private View prepareView(final ViewGroup parent, final LayoutInflater inflater, final Context context) {
        View itemView = CBBaseView.getView(context);
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
     * Method for setting up the view (values, listeners).
     * @param position the current postion of the item
     * @param convertView the convert view
     * @param parent
     * @param isSortMode tells if in sort mode
     * @param listMenuListener the listener for clicks
     * @param highlightPos the position to highlight
     * @param oneClickListener the single click listener
     * @param inflater the inflater
     * @param swipeListener the swipe listener
     */
    public abstract V setUpView(final int position, View convertView, final ViewGroup parent, final boolean isSortMode, final IListMenuListener listMenuListener, final int highlightPos, final IOneClickListener oneClickListener, final LayoutInflater inflater, final SwipeListener swipeListener);

    /**
     * Method for initializing the view.
     * @param itemView the convert view
     */
    public abstract V initView(View itemView, Context context);

}