package io.github.andres_vasquez.recyclerdatagrid.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;

import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridProperties;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;

/**
 * Created by andresvasquez on 10/2/17.
 */

public class DataGridPropertiesFragment extends Fragment {

    /**
     * Set initial properties to DataGrid
     * @param dataGridProperties DataGrid properties object
     */
    public void setProperties(DataGridProperties dataGridProperties){
        Gson gson=new Gson();

        Bundle args=new Bundle();
        if(dataGridProperties.getColumns()!=null){
            args.putString(Constants.EXTRA_COLUMNS,
                    gson.toJson(dataGridProperties.getColumns()));
        }

        if(dataGridProperties.getUniqueColumn()!=null){
            args.putString(Constants.EXTRA_UNIQUE_COLUMN,dataGridProperties.getUniqueColumn());
        }

        args.putBoolean(Constants.EXTRA_SELECTABLE,dataGridProperties.isSelectable());

        if(dataGridProperties.getCustomSelector()!=null){
            args.putString(Constants.EXTRA_CUSTOM_SELECTOR,
                    gson.toJson(dataGridProperties.getCustomSelector()));
        }

        this.setArguments(args);
    }
}
