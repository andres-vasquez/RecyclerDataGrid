package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

/**
 * Created by andresvasquez on 1/3/17.
 */

public class RowSelectorStyle {
    private int backgroundColorSelected;
    private int backgroundColor;
    private int imageSelectorSelected;
    private int imageSelector;

    public int getBackgroundColorSelected() {
        return backgroundColorSelected;
    }

    public void setBackgroundColorSelected(int backgroundColorSelected) {
        this.backgroundColorSelected = backgroundColorSelected;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getImageSelectorSelected() {
        return imageSelectorSelected;
    }

    public void setImageSelectorSelected(int imageSelectorSelected) {
        this.imageSelectorSelected = imageSelectorSelected;
    }

    public int getImageSelector() {
        return imageSelector;
    }

    public void setImageSelector(int imageSelector) {
        this.imageSelector = imageSelector;
    }
}
