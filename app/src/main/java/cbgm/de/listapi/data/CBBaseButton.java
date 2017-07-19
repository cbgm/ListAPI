package cbgm.de.listapi.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cbgm.de.listapi.R;


/**
 * Class which represents a button to add in the background when swiping
 * Extend from if custom button is desired.
 * @author Christian Bergmann
 */
public class CBBaseButton {
    /* The buttons id */
    private int btnId = -1;
    /* The buttons color */
    private int colId = -1;
    /* The buttons image */
    private int imgId = -1;

    public CBBaseButton() {

    }

    /**
     * Constructor
     * @param buttonId the buttons id
     * @param colorId the buttons color
     * @param imageId the buttons image
     */
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

    public void setImageId(int imgId) {
        this.imgId = imgId;
    }

    /**
     * Method to set up a basic button with an image in center (e.g. delete, edit)
     * @param buttonId the buttons id
     * @param context the applications context
     * @param colorId the color of the button
     * @param imageId the image id in resources
     * @return the buttons view ({@link LinearLayout})
     */
    public View getButton(final int buttonId, final Context context, final int colorId, final int imageId) {
        // button is clickable as LinearLayout and contains an image in center
        LinearLayout button = new LinearLayout(context);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setOrientation(LinearLayout.HORIZONTAL);
        button.setBackgroundColor(ContextCompat.getColor(context, colorId));
        button.setId(buttonId);

        ImageView image = new ImageView(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        layoutParams.setMargins((int) context.getResources().getDimension(R.dimen.image_margin_left_right), 0, (int) context.getResources().getDimension(R.dimen.image_margin_left_right), 0);
        image.setLayoutParams(layoutParams);
        image.setImageDrawable(ContextCompat.getDrawable(context, imageId));

        button.addView(image);

        return button;
    }

    /**
     * Method to to set up custom button instead having an image in center.
     * @param context the application context
     * @return the buttons view ({@link LinearLayout})
     */
    public View getCustomButton(final Context context) {
        LinearLayout button = new LinearLayout(context);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setOrientation(LinearLayout.HORIZONTAL);
        button.setBackgroundColor(ContextCompat.getColor(context, colId));
        button.setId(btnId);
        button.addView(customButtonView(context));
        return button;
    }

    /**
     * Method has to be overridden if a custom button is set up.
     * Here should go the content of the buttons {@link LinearLayout}
     * @param context the application context
     * @return the buttons content
     */
    public View customButtonView(final Context context) {
        return null;
    }
}
