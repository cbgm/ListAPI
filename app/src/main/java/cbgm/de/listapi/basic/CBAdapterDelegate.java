package cbgm.de.listapi.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.listener.ICBActionNotifier;

/**
 * Delegate used by the DelegateManager. Represents and sets a ViewHolder.
 * @author Christian Bergmann
 */

public abstract class CBAdapterDelegate<I> {

    protected int itemType = -1;
    protected Context context;
    protected CBAdapter<I> adapter;

    public CBAdapterDelegate(final Context context) {
        this.context = context;
    }

    protected void onBindViewHolder(final CBAdapterDelegate.CBViewHolder holder, final int position, final List<I> data, final ICBActionNotifier<I> actionNotifier) {
        final I item = data.get(position);
        final CBModeHelper modeHelper = CBModeHelper.getInstance();

        //toggle selected to react with drawable
        if (this.adapter.isItemSelected(position))
            holder.frontItem.setSelected(true);
        else
            holder.frontItem.setSelected(false);

        //cant use click listener here because it intercepts touch of listitem sometimes
        if (holder.addEdit) {
            holder.edit.setOnTouchListener(new View.OnTouchListener() {

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

        if (holder.addDelete) {
            holder.delete.setOnTouchListener(new View.OnTouchListener() {

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

        onBindDelegateViewHolder(holder, position, data);
    }

    protected abstract CBAdapterDelegate.CBViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType);

    protected abstract void onBindDelegateViewHolder(final CBAdapterDelegate.CBViewHolder holder, final int position, final List<I> data);

    protected abstract boolean isTypeOf(final List<I> data, final int position);

    public void setAdapter(CBAdapter<I> adapter) {
        this.adapter = adapter;
    }


    @SuppressWarnings("unused")
    public static abstract class CBViewHolder extends RecyclerView.ViewHolder {
        /* The background item which shows up on swipe */
        LinearLayout backItem;
        /* The list item menu container which is within the background item */
        LinearLayout buttonContainer;
        /* The foreground item  */
        GridLayout frontItem;
        /* The basic delete button */
        LinearLayout delete;
        /* The basic edit button */
        LinearLayout edit;
        //tells if the edit button should be added
        boolean addEdit;
        //tells if the delete button should be added
        boolean addDelete;
        //list of custom buttons
        List<CBBaseButton> customButtons;

        public CBViewHolder(final View itemView, final ViewGroup parent, final int itemResource, final boolean addEdit, final boolean addDelete) {
            super(itemView);
            this.addEdit = addEdit;
            this.addDelete = addDelete;
            this.customButtons = new ArrayList<>();
            this.frontItem = itemView.findViewById(CBLayoutID.ITEM_FOREGROUND_ID);
            this.buttonContainer = itemView.findViewById(CBLayoutID.BUTTON_CONTAINER_ID);

            if (addEdit) {
                this.buttonContainer.addView(new CBBaseButton().getButton(CBLayoutID.EDIT_BUTTON_ID, parent.getContext(), R.color.cb_edit_background_color, R.mipmap.edit_icon));
                this.edit = itemView.findViewById(CBLayoutID.EDIT_BUTTON_ID);
            }

            if (addDelete) {
                this.buttonContainer.addView(new CBBaseButton().getButton(CBLayoutID.DELETE_BUTTON_ID, parent.getContext(), R.color.cb_delete_background_color, R.mipmap.trash_icon));
                this.delete = itemView.findViewById(CBLayoutID.DELETE_BUTTON_ID);
            }
            this.backItem = itemView.findViewById(CBLayoutID.ITEM_BACKGROUND_ID);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View personalView = inflater.inflate(itemResource, parent, false);
            this.frontItem.addView(personalView);
        }

        @SuppressWarnings("unused")
        public void addCustomButton(final CBBaseButton customButton, final Context context) {
            this.buttonContainer.addView(customButton.getCustomButton(context));
        }

        public LinearLayout getBackItem() {
            return backItem;
        }

        @SuppressWarnings("unused")
        public void setBackItem(final LinearLayout backItem) {
            this.backItem = backItem;
        }

        public LinearLayout getButtonContainer() {
            return buttonContainer;
        }

        @SuppressWarnings("unused")
        public void setButtonContainer(final LinearLayout buttonContainer) {
            this.buttonContainer = buttonContainer;
        }

        public GridLayout getFrontItem() {
            return frontItem;
        }

        @SuppressWarnings("unused")
        public void setFrontItem(final GridLayout frontItem) {
            this.frontItem = frontItem;
        }

        public LinearLayout getDelete() {
            return delete;
        }

        public void setDelete(final LinearLayout delete) {
            this.delete = delete;
        }

        public LinearLayout getEdit() {
            return edit;
        }

        public void setEdit(final LinearLayout edit) {
            this.edit = edit;
        }
    }
}
