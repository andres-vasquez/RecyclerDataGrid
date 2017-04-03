package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

import java.util.Map;

/**
 * Created by andresvasquez on 7/2/17.
 */

public class DataGridItem {

    private String uniqueIdentificator;
    private Map<String,Object> mapData;
    private boolean filtered;
    private String filterValue;
    private boolean selected;

    public String getUniqueIdentificator() {
        return uniqueIdentificator;
    }

    public void setUniqueIdentificator(String uniqueIdentificator) {
        this.uniqueIdentificator = uniqueIdentificator;
    }

    public Map<String, Object> getMapData() {
        return mapData;
    }

    public void setMapData(Map<String, Object> mapData) {
        this.mapData = mapData;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
