package cbgm.de.listapi.data;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cbgm.de.listapi.R;
import cbgm.de.listapi.data.LayoutID;

/**
 * Created by SA_Admin on 14.02.2017.
 */

public class CBBaseView {


    public static View getView(Context context) {

        FrameLayout mainContainer = new FrameLayout(context);
        mainContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout backItem = new LinearLayout(context);
        backItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        backItem.setClickable(false);
        backItem.setEnabled(false);
        backItem.setFocusable(false);
        backItem.setFocusableInTouchMode(false);
        backItem.setBackgroundColor(context.getResources().getColor(R.color.cb_item_foreground_color));
        backItem.setId(LayoutID.ITEM_BACKGROUND_ID);

        View space = new View(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));

        backItem.addView(space);

        LinearLayout buttonContainer = new LinearLayout(context);
        buttonContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
        buttonContainer.setId(LayoutID.BUTTON_CONTAINER_ID);

        backItem.addView(buttonContainer);

        mainContainer.addView(backItem);

        GridLayout frontItem = new GridLayout(context);
        frontItem.setId(LayoutID.ITEM_FOREGROUND_ID);
        frontItem.setBackgroundColor(context.getResources().getColor(R.color.cb_item_foreground_color));

        mainContainer.addView(frontItem);

        return mainContainer;
    }


}
