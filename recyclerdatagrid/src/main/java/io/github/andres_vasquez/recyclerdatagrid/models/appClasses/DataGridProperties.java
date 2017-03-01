package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

import java.util.List;

/**
 * Created by andresvasquez on 10/2/17.
 */

public class DataGridProperties{
    private String uniqueColumn;
    private List<ColumnItem> columns;
    private boolean selectable;
    private RowSelectorStyle customSelector;

    public String getUniqueColumn() {
        return uniqueColumn;
    }

    public void setUniqueColumn(String uniqueColumn) {
        this.uniqueColumn = uniqueColumn;
    }

    public List<ColumnItem> getColumns() {

        return columns;
    }

    public void setColumns(List<ColumnItem> columns) {
        this.columns = columns;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public RowSelectorStyle getCustomSelector() {
        return customSelector;
    }

    public void setCustomSelector(RowSelectorStyle customSelector) {
        this.customSelector = customSelector;
    }
}
