package io.github.andres_vasquez.recyclerdatagrid.models.interfaces;

import java.util.List;

import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ChoiceItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;

/**
 * Created by andresvasquez on 4/3/17.
 */

public interface FilterRowInterface {
    void onRowFilterApplied(ChoiceItem filterItem);
}
