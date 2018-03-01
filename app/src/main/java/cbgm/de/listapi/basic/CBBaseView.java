package cbgm.de.listapi.basic;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import cbgm.de.listapi.data.CBLayoutID;
import cbgm.de.listapi.data.CBModeHelper;


/**
 * Class which represent to basic view which later contains the foreground
 * @author Christian Bergmann
 */

public class CBBaseView {

    public static View getView(Context context) {

        FrameLayout mainContainer = new FrameLayout(context);
        mainContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout backItem = new LinearLayout(context);
        backItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        backItem.setEnabled(true);
        backItem.setId(CBLayoutID.ITEM_BACKGROUND_ID);

        View space = new View(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));

        backItem.addView(space);

        LinearLayout buttonContainer = new LinearLayout(context);
        buttonContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
        buttonContainer.setId(CBLayoutID.BUTTON_CONTAINER_ID);

        backItem.addView(buttonContainer);

        mainContainer.addView(backItem);

        GridLayout frontItem = new GridLayout(context);
        frontItem.setId(CBLayoutID.ITEM_FOREGROUND_ID);
        //add different color states to the list item (pressed, hold, base, selected)
        StateListDrawable states = new StateListDrawable();
        ColorDrawable dPressed, dBase, dSelected;
        CBModeHelper modeHelper = CBModeHelper.getInstance();
        dPressed = new ColorDrawable(ContextCompat.getColor(context, modeHelper.getHightlightColor()));
        dBase = new ColorDrawable(ContextCompat.getColor(context, modeHelper.getBaseColor()));
        dSelected = new ColorDrawable(ContextCompat.getColor(context, modeHelper.getSelectColor()));
        states.addState (new int[]{ android.R.attr.state_activated}, dPressed);
        states.addState (new int[]{ android.R.attr.state_selected}, dSelected);
        states.addState (new int[]{ }, dBase);
        frontItem.setBackground(states);

        mainContainer.addView(frontItem);

        return mainContainer;
    }


}
