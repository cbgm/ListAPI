package cbgm.de.listapi.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import cbgm.de.listapi.R;


/**
 * Class which represent to basic view which later contains the foreground
 * @author Christian Bergmann
 */

public class BaseView {

    public static View getView(Context context) {

        FrameLayout mainContainer = new FrameLayout(context);
        mainContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout backItem = new LinearLayout(context);
        backItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        backItem.setEnabled(true);
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
        frontItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cb_item_foreground_color));

        mainContainer.addView(frontItem);

        return mainContainer;
    }


}
