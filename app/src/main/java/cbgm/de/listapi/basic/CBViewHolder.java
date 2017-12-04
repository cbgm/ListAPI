package cbgm.de.listapi.basic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import cbgm.de.listapi.data.CBListItem;
import cbgm.de.listapi.data.CBListMode;
import cbgm.de.listapi.data.CBModeHelper;
import cbgm.de.listapi.listener.ICBActionNotifier;

public abstract class CBViewHolder<I extends CBListItem> extends RecyclerView.ViewHolder {
    /* The background item which shows up on swipe */
    protected LinearLayout backItem;
    /* The list item menu container which is within the background item */
    protected LinearLayout buttonContainer;
    /* The foreground item  */
    protected GridLayout frontItem;
    /* The basic delete button */
    protected LinearLayout delete;
    /* The basic edit button */
    protected LinearLayout edit;
    //tells if the edeit button should be added
    private boolean addEdit;
    //tells if the delete button should be added
    private boolean addDelete;
    //list of custom buttons
    protected List<CBBaseButton> customButtons;

    public CBViewHolder(final View itemView, final Context context, final ViewGroup parent, final int itemResource, final boolean addEdit, final boolean addDelete) {
        super(itemView);
        initView(itemView, context, parent, itemResource, addEdit, addDelete);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addFunctionalityOnView(final I listObject, final int position, final ICBActionNotifier actionNotifier, final Context context) {
        CBListMode mode = CBModeHelper.getInstance().getListMode();

        switch (mode) {
            case SWIPE:
                this.frontItem.setBackgroundColor(Color.WHITE);

                if (addEdit) {
                    this.edit.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (CBModeHelper.getInstance().isItemTouchCurrentItem(position)) {
                                actionNotifier.editAction(position);
                                return true;
                            }
                            return false;
                        }
                    });
                }

                if (addDelete) {
                    this.delete.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (CBModeHelper.getInstance().isItemTouchCurrentItem(position)) {
                                actionNotifier.deleteAction(listObject);
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
                if (CBModeHelper.getInstance().getSelectedPosition() == position) {
                    this.frontItem.setBackgroundColor(Color.LTGRAY);
                } else {
                    this.frontItem.setBackgroundColor(Color.WHITE);
                }
                break;
            case NULL:
                break;
            default:
                break;
        }
        setUpPersonalView(listObject, position, actionNotifier, context);
    }

    /**
     * Method for initializing the base view
     * @param itemView the base view
     * @param context the context
     * @param parent the view group
     * @param itemResource the layout resource of the front item
     * @param addEdit the add button option
     * @param addDelete the delete button option
     */
    private void initView(final View itemView, final Context context, final ViewGroup parent, final int itemResource, final boolean addEdit, final boolean addDelete) {
        this.addEdit = addEdit;
        this.addDelete = addDelete;
        this.customButtons = new ArrayList<>();
        initCustomButtons();

        this.frontItem = (GridLayout)itemView.findViewById(CBLayoutID.ITEM_FOREGROUND_ID);
        this.buttonContainer = (LinearLayout) itemView.findViewById(CBLayoutID.BUTTON_CONTAINER_ID);

        if (addEdit) {
            this.buttonContainer.addView(new CBBaseButton().getButton(CBLayoutID.EDIT_BUTTON_ID, context, R.color.cb_edit_background_color, R.mipmap.edit_icon));
            this.edit = (LinearLayout) itemView.findViewById(CBLayoutID.EDIT_BUTTON_ID);
        }

        if (addDelete) {
            this.buttonContainer.addView(new CBBaseButton().getButton(CBLayoutID.DELETE_BUTTON_ID, context, R.color.cb_delete_background_color, R.mipmap.trash_icon));
            this.delete = (LinearLayout) itemView.findViewById(CBLayoutID.DELETE_BUTTON_ID);
        }

        for (CBBaseButton customButton : this.customButtons) {
            this.buttonContainer.addView(customButton.getCustomButton(context));
        }
        this.backItem = (LinearLayout) itemView.findViewById(CBLayoutID.ITEM_BACKGROUND_ID);
        LayoutInflater inflater = LayoutInflater.from(context);
        View personalView = inflater.inflate(itemResource, parent, false);
        this.frontItem.addView(personalView);
        initPersonalView(itemView, context);
        itemView.setTag(this);
    }

    protected abstract void initCustomButtons();

    /**
     * Method for setting up the view functionality (values, listeners).
     * @param listObject the current list element
     * @param actionNotifier the listener for clicks
     * @param context the context
     */
    protected abstract void setUpPersonalView(final I listObject, final int position, final ICBActionNotifier actionNotifier, final Context context);

    /**
     * Method for initializing the view.
     * @param itemView the convert view
     */
    protected abstract void initPersonalView(final View itemView, final Context context);

    public LinearLayout getBackItem() {
        return backItem;
    }

    public void setBackItem(final LinearLayout backItem) {
        this.backItem = backItem;
    }

    public LinearLayout getButtonContainer() {
        return buttonContainer;
    }

    public void setButtonContainer(final LinearLayout buttonContainer) {
        this.buttonContainer = buttonContainer;
    }

    public GridLayout getFrontItem() {
        return frontItem;
    }

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
