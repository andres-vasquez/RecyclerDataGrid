package io.github.andres_vasquez.recyclerdatagrid.models.interfaces;

import java.util.List;

import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;

/**
 * Created by andresvasquez on 24/2/17.
 */

public interface FilterColumnInterface {
    void onColumnsSelectedChanged(List<ColumnItem> lstColumns);
}
