package io.github.andres_vasquez.recyclerdatagrid.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ChoiceItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.FilterColumnInterface;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.FilterRowInterface;
import io.github.andres_vasquez.recyclerdatagrid.ui.dialog.ListChoiceDialog;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;
import io.github.andres_vasquez.recyclerdatagrid.utils.GlobalFunctions;

/**
 * Created by andresvasquez on 24/2/17.
 */

public class DataGridUISupportFragment extends DataGridPropertiesFragment{

    public AppCompatActivity mActivity;
    public Context mContext;

    //Columns
    public Map<String,ColumnItem> mapFixedColumns;
    public Map<String,ColumnItem> mapColumns;
    public List<ColumnItem> columns;

    //Filters
    public List<ChoiceItem> lstChoiceItems;

    private ProgressDialog progressDialog;

    /**
     * Show Progress Dialog
     */
    public void showLoadingDialog() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog==null){
                    progressDialog=new ProgressDialog(mContext);
                    progressDialog.setMessage(mContext.getString(R.string.loading));
                }

                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
        });
    }

    /**
     * Show Progress Dialog
     */
    public void hideLoadingDialog(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.hide();
                    }
                }
            }
        });
    }


    /**
     * Show pop up for row Filter picker
     */
    public void showRowPickerDialog(final FilterRowInterface callback)
    {
        if(lstChoiceItems==null){
            lstChoiceItems=new ArrayList<>();
        }

        //TODO apply multiple column filters
        Bundle args=new Bundle();
        args.putString(Constants.EXTRA_LIST_CHOICE_ITEMS,new Gson().toJson(lstChoiceItems));

        FragmentManager fm = mActivity.getSupportFragmentManager();
        ListChoiceDialog listChoiceDialog=new ListChoiceDialog().newInstance(
                Constants.LIST_DIALOG_SINGLE_CHOICE, new ListChoiceDialog.NoticeDialogListener() {
                    @Override
                    public void onDialogPositiveClick(List<ChoiceItem> lstItemsSelected) {
                    }
                    @Override
                    public void onDialogNegativeClick() {
                    }
                    @Override
                    public void onDialogItemClick(ChoiceItem itemSelected) {
                        //Change filter state
                        //Change
                        //Update the item selected
                        for(ChoiceItem item : lstChoiceItems){
                            if(itemSelected.getId()==item.getId()){
                                item.setChecked(true);
                            } else {
                                item.setChecked(false);
                            }
                        }

                        //Send callback to MainActivity
                        callback.onRowFilterApplied(itemSelected);
                    }
                });
        listChoiceDialog.setArguments(args);
        listChoiceDialog.show(fm, "listChoiceDialog");
    }


    /**
     * Show columns Pickup Dialog
     * @param callback
     */
    public void showColumnsPickUpDialog(final FilterColumnInterface callback) {
        final String[] arrPickerItems=new String[mapColumns.size()];
        final boolean[] checked=new boolean[mapColumns.size()];

        //Check items from list
        int count=0;
        for(Map.Entry<String,ColumnItem> column : mapColumns.entrySet()){
            arrPickerItems[count]=column.getKey();
            checked[count]=column.getValue().isSelected();
            count++;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setMultiChoiceItems(arrPickerItems, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //Save changes in Map
                if(mapColumns.containsKey(arrPickerItems[which])){
                    ColumnItem columnItem=mapColumns.get(arrPickerItems[which]);
                    columnItem.setSelected(isChecked);
                }
            }
        });
        alert.setPositiveButton(mContext.getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Change map to list and execute callback
                List<ColumnItem> lstColumnItems=new ArrayList<ColumnItem>();
                for(Map.Entry<String,ColumnItem> columnItemEntry : mapColumns.entrySet()){
                    lstColumnItems.add(columnItemEntry.getValue());
                }

                if(callback!=null){
                    callback.onColumnsSelectedChanged(columns);
                }
            }
        });
        alert.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog=alert.create();
        GlobalFunctions.fixDialogMarshmellow(dialog);
        dialog.show();
    }
}
