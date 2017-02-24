package io.github.andres_vasquez.recyclerdatagrid.models.appClasses;

import java.util.Map;

/**
 * Created by andresvasquez on 7/2/17.
 */

public class DataGridItem {

    private Object uniqueIdentificator;
    private Map<String,Object> mapData;
    private boolean filtered;
    private Object filterValue;

    public Object getUniqueIdentificator() {
        return uniqueIdentificator;
    }

    public void setUniqueIdentificator(Object uniqueIdentificator) {
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

    public Object getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(Object filterValue) {
        this.filterValue = filterValue;
    }
}
