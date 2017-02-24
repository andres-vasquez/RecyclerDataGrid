package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

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

    public ColumnItem(int id, String title) {
        this.id = id;
        this.title = title;
        this.columnType= Constants.COLUMN_TYPE_DEFAULT;
        this.selected=true;
    }

    public ColumnItem(int id, String title, int columnType) {
        this.id = id;
        this.title = title;
        this.columnType = columnType;
        this.selected=true;
    }

    public ColumnItem(int id, String title, int columnType, boolean selected) {
        this.id = id;
        this.title = title;
        this.columnType = columnType;
        this.selected = selected;
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
}
