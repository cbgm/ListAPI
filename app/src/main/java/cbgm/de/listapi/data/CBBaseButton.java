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

public class CBBaseButton {
    private int btnId = -1;
    private int colId = -1;
    private int imgId = -1;

    public CBBaseButton() {

    }

    public CBBaseButton(final int buttonId, final int colorId, final int imageId) {
        this.imgId = imageId;
        this.colId = colorId;
        this.btnId = buttonId;
    }

    public int getButtonId() {
        return btnId;
    }

    public void setButtonId(int btnId) {
        this.btnId = btnId;
    }

    public int getColorId() {
        return colId;
    }

    public void setColorId(int colId) {
        this.colId = colId;
    }

    public int getImageId() {
        return imgId;
    }

    public void setIamgeId(int imgId) {
        this.imgId = imgId;
    }

    public View getButton(final int buttonId, final Context context, final int colorId, final int imageId) {
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

    public View getCustomButton(final Context context) {
        LinearLayout button = new LinearLayout(context);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setOrientation(LinearLayout.HORIZONTAL);
        button.setBackgroundColor(context.getResources().getColor(colId));
        button.setId(btnId);
        button.addView(customButtonView(context));
        return button;
    }

    public View customButtonView(final Context context) {
        return null;
    }
}
