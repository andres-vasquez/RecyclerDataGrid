package io.github.andres_vasquez.recyclerdatagrid.utils;

import java.util.Comparator;

import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridItem;

/**
 * Created by avasquez on 8/17/2016.
 */

public class ComparatorItems implements Comparator<DataGridItem> {

    private String mColumnOrder;
    private String mOrderType;

    public ComparatorItems(String columnOrder, String orderType) {
        this.mColumnOrder = columnOrder;
        this.mOrderType = orderType;
    }

    @Override
    public int compare(DataGridItem o1, DataGridItem o2) {

        String first=o1.getMapData().get(mColumnOrder).toString();
        String second=o2.getMapData().get(mColumnOrder).toString();

        if(mOrderType.compareTo(Constants.ORDER_ASC)==0){
            return first.compareTo(second);
        } else {
            return second.compareTo(first);
        }
    }
}