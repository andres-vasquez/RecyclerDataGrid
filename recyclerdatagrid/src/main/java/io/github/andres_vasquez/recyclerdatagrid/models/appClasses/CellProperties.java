package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

/**
 * Created by andresvasquez on 24/2/17.
 */

public class CellProperties {
    private int width;
    private int textColor;
    private int textSize;
    private int gravity;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
}
