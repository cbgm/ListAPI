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

import cbgm.de.listapi.R;
import cbgm.de.listapi.listener.IListMenuListener;
import cbgm.de.listapi.listener.IOneClickListener;
import cbgm.de.listapi.listener.SwipeListener;


/**
 * The definition of a listview items data
 * @author Christian Bergmann
 */

public abstract class CBListViewItem<V extends CBViewHolder, M extends CBItem>{
    protected M item;
    protected V holder;
    protected int itemResource;
    private Context context;

    public CBListViewItem(final M item) {
        this.item = item;
        this.itemResource = item.itemResource;
        this.holder = (V) item.getHolder();
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
     * @param position
     * @param convertView
     * @param parent
     * @param isSortMode
     * @param listMenuListener
     * @param highlightPos
     * @param oneClickListener
     * @param inflater
     * @return
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
          /*  holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeListener.rollback();
                    listMenuListener.handleEdit(item);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeListener.rollback();
                    listMenuListener.handleDelete(item);
                }
            });*/

        } else {
            holder.item.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
/*
            if (highlightPos == position && highlightPos != -1) {
                holder.item.setBackgroundColor(Color.rgb(219,235,226));
            } else {
                holder.item.setBackgroundColor(Color.WHITE);
            }*/
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
        //View itemView = inflater.inflate(R.layout.list_item_standard, parent, false);

        holder.item = (GridLayout)itemView.findViewById(LayoutID.ITEM_FOREGROUND_ID);
        holder.buttonContainer = (LinearLayout) itemView.findViewById(LayoutID.BUTTON_CONTAINER_ID);
        /*holder.delete = (LinearLayout) itemView.findViewById(LayoutID.DELETE_BUTTON_ID);
        holder.edit = (LinearLayout) itemView.findViewById(LayoutID.EDIT_BUTTON_ID);*/
        holder.backItem = (LinearLayout) itemView.findViewById(LayoutID.ITEM_BACKGROUND_ID);
        View personalView = inflater.inflate(this.itemResource, parent, false);
        holder.item.addView(personalView);
        holder = initView(itemView, context);
        itemView.setTag(holder);
        return itemView;
    }

    /**
     * Method for setting up the view (values, listeners).
     * @param position
     * @param convertView
     * @param parent
     * @param isSortMode
     * @param listMenuListener
     * @param highlightPos
     * @param oneClickListener
     * @param inflater
     * @param swipeListener
     */
    public abstract V setUpView(final int position, View convertView, final ViewGroup parent, final boolean isSortMode, final IListMenuListener listMenuListener, final int highlightPos, final IOneClickListener oneClickListener, final LayoutInflater inflater, final SwipeListener swipeListener);

    /**
     * Method for initializing the view.
     * @param itemView
     */
    public abstract V initView(View itemView, Context context);
}