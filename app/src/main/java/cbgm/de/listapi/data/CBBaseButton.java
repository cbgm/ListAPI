package cbgm.de.listapi.data;

/**
 * Created by SA_Admin on 16.02.2017.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cbgm.de.listapi.R;

public abstract class CBBaseButton {

    public static View getButton(final int buttonId, final Context context, final int colorId, final int imageId) {
        LinearLayout button = new LinearLayout(context);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setOrientation(LinearLayout.HORIZONTAL);
        button.setBackgroundColor(context.getResources().getColor(colorId));
        button.setId(buttonId);

        ImageView image = new ImageView(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = 30;
        layoutParams.rightMargin = 30;
        image.setLayoutParams(layoutParams);
        image.setImageDrawable(context.getResources().getDrawable(imageId));

        button.addView(image);

        return button;
    }

    //public View setUpButtonContent(final Context context);


}
