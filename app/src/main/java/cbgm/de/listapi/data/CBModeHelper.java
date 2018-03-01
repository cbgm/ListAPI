package cbgm.de.listapi.data;

import cbgm.de.listapi.R;

/**
 * Singleton for vars that need global availability concerning the mode switching.
 * @author Christian Bergmann
 */

@SuppressWarnings({"SameParameterValue", "unused"})
public class CBModeHelper {
    //tells if an item is in active swiping
    private boolean isSwipeActive = false;
    //the list mode
    private CBListMode listMode = CBListMode.NULL;
    //tells if list mode was changed (for reload)
    private boolean modeChanged = false;
    //tells the swiping if the swipe is still on the same item
    private int currentPosition = -1;
    //tells if a button of the list item (delete, edit) was clicked
    private boolean buttonClicked = false;
    //the color for highlighting a touch
    private int hightlightColor = R.color.cb_item_highlighted;
    //the color for highlighting a selection
    private int selectColor = R.color.cb_item_selected;
    //the base color for each item
    private int baseColor = R.color.cb_item_base;

    private boolean isSelectEnabled = true;

    private boolean isSwipeEnabled = true;

    private boolean isScrollingAllowed = true;

    private static CBModeHelper modeHelper;

    public static CBModeHelper getInstance() {

        if (modeHelper == null) {
            modeHelper = new CBModeHelper();
        }
        return modeHelper;
    }

    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public boolean isSwipeEnabled() {
        return isSwipeEnabled;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }

    public boolean isSelectEnabled() {
        return isSelectEnabled;
    }

    public void setSelectEnabled(boolean selectEnabled) {
        isSelectEnabled = selectEnabled;
    }

    public void resetModeChanged() {
        this.modeChanged = false;
    }

    public boolean isSwipeActive() {
        return isSwipeActive;
    }

    public void setSwipeActive(boolean swipeActive) {
        isSwipeActive = swipeActive;
    }

    public CBListMode getListMode() {
        return listMode;
    }

    public void setListMode(CBListMode listMode) {
        this.modeChanged = this.listMode != listMode;
        this.listMode = listMode;
    }

    public void reset() {
        this.isSwipeActive = false;
        this.listMode = CBListMode.NULL;
        this.modeChanged = false;
        this.currentPosition = -1;
    }

    public boolean isModeChanged() {
        return modeChanged;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isButtonClicked() {
        return buttonClicked;
    }

    public void setButtonClicked(final boolean buttonClicked) {
        this.buttonClicked = buttonClicked;
    }

    /**
     * MEthod to check if touch is still on the item which raised the event.
     * @param position the item position
     * @return boolean
     */
    public boolean isItemTouchCurrentItem(final int position) {

        return modeHelper.isSwipeActive() && modeHelper.getCurrentPosition() == position;
    }

    public int getHightlightColor() {
        return hightlightColor;
    }

    public void setHightlightColor(int hightlightColor) {
        this.hightlightColor = hightlightColor;
    }

    public int getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public boolean isScrollingAllowed() {
        return isScrollingAllowed;
    }

    public void setScrollingAllowed(boolean scrollingAllowed) {
        isScrollingAllowed = scrollingAllowed;
    }

}
