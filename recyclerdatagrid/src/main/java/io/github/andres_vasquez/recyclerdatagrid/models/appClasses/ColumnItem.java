package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

import android.graphics.drawable.Drawable;

import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;

/**
 * Created by andresvasquez on 23/2/17.
 */

public class ColumnItem {
    private int id;
    private String title;
    private int columnType;
    private boolean selected;
    private CellProperties cellProperties;
    private boolean fixed;
    private int imageRes;

    public ColumnItem(int id, String title) {
        this.id = id;
        this.title = title;
        this.columnType= Constants.COLUMN_TYPE_DEFAULT;
        this.selected=true;
        this.fixed=false;
    }

    public ColumnItem(int id, String title, int columnType) {
        this.id = id;
        this.title = title;
        this.columnType = columnType;
        this.selected=true;
        this.fixed=false;
    }

    public ColumnItem(int id, String title, int columnType, boolean selected) {
        this.id = id;
        this.title = title;
        this.columnType = columnType;
        this.selected = selected;
        this.fixed=false;
    }

    public ColumnItem(int id, int imgRes, String title, boolean selected){
        this.id=id;
        this.title=title;
        this.imageRes=imgRes;
        this.title=title;
        this.columnType=Constants.COLUMN_TYPE_IMAGE;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getColumnType() {

        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public CellProperties getCellProperties() {
        return cellProperties;
    }

    public void setCellProperties(CellProperties cellProperties) {
        this.cellProperties = cellProperties;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
