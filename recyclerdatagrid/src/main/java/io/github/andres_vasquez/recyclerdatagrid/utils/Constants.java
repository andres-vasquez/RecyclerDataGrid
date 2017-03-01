package io.github.andres_vasquez.recyclerdatagrid.utils;

/**
 * Created by andresvasquez on 7/2/17.
 */

public class Constants {

    /**
     * Order data by ASC or DESC
     */

    //Order datagrid ascendent
    public static final String ORDER_ASC="asc";

    //Order datagrid descendent
    public static final String ORDER_DESC="desc";

    //No order to datagrid
    public static final String NO_ORDER="no_order";


    /**
     * TAGs to pass variables to Activities/Fragments
     */
    //Unique column attrib
    public static final String EXTRA_UNIQUE_COLUMN = "uniqueColumn";

    //Pass columns to fragment
    public static final String EXTRA_COLUMNS = "setColumns";

    //Pass column order value from Map to order data
    public static final String EXTRA_COLUMN_ORDER="columnOrder";

    //Pass orderType: ASC, DESC or NONE
    public static final String EXTRA_ORDER_TYPE = "orderType";

    //Pass filter object to DataGridFragment
    public static final String EXTRA_FILTER = "filter";

    //Pass selectable tag
    public static final String EXTRA_SELECTABLE = "selectable";


    /**
     * Variable Type
     */
    //Default column tyoe String
    public static final int COLUMN_TYPE_DEFAULT = 1;

    //Column type number
    public static final int COLUMN_TYPE_NUMBER = 2;

    //Column type number
    public static final int COLUMN_TYPE_DATE = 3;
}
