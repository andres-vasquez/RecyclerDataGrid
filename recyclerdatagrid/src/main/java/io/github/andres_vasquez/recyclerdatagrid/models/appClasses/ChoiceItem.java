package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by avasquez on 7/1/2016.
 */

public class ChoiceItem {

    @SerializedName("id")
    public int id;

    @SerializedName("text")
    public String text;

    @SerializedName("icon")
    public int icon;

    @SerializedName("isChecked")
    public boolean isChecked;

    @SerializedName("filterValue")
    private String filterValue;

    public ChoiceItem() {
    }

    public ChoiceItem(int id, String text, int icon, String filterValue) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.filterValue=filterValue;
        this.isChecked=false;
    }

    public ChoiceItem(int id, String text, int icon, String filterKey, String filterValue, boolean isChecked) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.filterValue=filterValue;
        this.isChecked=isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
}
