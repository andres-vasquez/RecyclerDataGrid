package io.github.andres_vasquez.recyclerdatagrid.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.controller.listener.ScrollManager;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.CellProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridItem;
import io.github.andres_vasquez.recyclerdatagrid.models.events.HeaderOrderEvent;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.FilterColumnInterface;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.LoadInterface;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.ScrollNotifier;
import io.github.andres_vasquez.recyclerdatagrid.ui.adapters.HorizontalAdapter;
import io.github.andres_vasquez.recyclerdatagrid.ui.views.CustomTemplates;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;
import io.github.andres_vasquez.recyclerdatagrid.utils.GlobalFunctions;

/**
 * Created by andresvasquez on 7/2/17.
 */

public class DataGridFragment extends DataGridPropertiesFragment
        implements HorizontalAdapter.OnItemClickListener, LoadInterface {

    private Activity mActivity;
    private Context mContext;

    //Adapter variables
    private HorizontalAdapter horizontalAdapter;
    private ScrollManager scrollManager;

    //List variables
    private List<DataGridItem> lstItemsInAdapter;
    private LinearLayoutManager linearLayoutManager;

    //Columns and order
    private Map<String,ColumnItem> mapColumns;
    private List<ColumnItem> columns;
    private String uniqueColumnKey;

    private String columnOrder;
    private String orderType;
    private List<HeaderOrderEvent> lstHeaderOrderEvent;

    //Filters
    private Object filter;

    // Dynamic Columns */
    private LinearLayout headerDynamic;
    private LinearLayout lyColumns;
    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();

        columns=new ArrayList<>();
        lstHeaderOrderEvent =new ArrayList<HeaderOrderEvent>();

        if (getArguments() != null) {
            Gson gson=new Gson();
            Bundle args=getArguments();

            if(args.containsKey(Constants.EXTRA_COLUMNS)){
                try
                {
                    /*Type listType = new TypeToken<Map<String,Integer>>(){}.getType();
                    lstColumnsSelected= new Gson()
                            .fromJson(args.getString(Constants.EXTRA_SELECTED_COLUMNS), listType);*/

                    Type listType = new TypeToken<ArrayList<ColumnItem>>(){}.getType();
                    columns = gson.fromJson(args.getString(Constants.EXTRA_COLUMNS), listType);

                    //Change to map
                    mapColumns=getColumnsInMap();
                }
                catch (Exception e){
                    Log.e("Exception",""+e.getMessage());
                }
            }

            //Unique column
            if(args.containsKey(Constants.EXTRA_UNIQUE_COLUMN)){
                uniqueColumnKey=args.getString(Constants.EXTRA_UNIQUE_COLUMN);
            }

            //Column order
            if(args.containsKey(Constants.EXTRA_COLUMN_ORDER)){
                columnOrder=args.getString(Constants.EXTRA_COLUMN_ORDER);
            } else {
                columnOrder="";
            }

            if(args.containsKey(Constants.EXTRA_ORDER_TYPE)){
                orderType=args.getString(Constants.EXTRA_ORDER_TYPE);
            } else {
                orderType=Constants.NO_ORDER;
            }

            //Filter
            filter=gson.fromJson(args.getString(Constants.EXTRA_FILTER),Object.class);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_list, container, false);
        initViews(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        scrollManager=new ScrollManager();
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        lstItemsInAdapter =new ArrayList<DataGridItem>();

        ScrollNotifier headerView = (ScrollNotifier) headerDynamic.findViewById(R.id.scroll);
        scrollManager.addScrollClient(headerView);

        horizontalAdapter=new HorizontalAdapter(mActivity,scrollManager,mapColumns);
        horizontalAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(horizontalAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Fill headers and base List from report
        fillColumnsHeaders();

        return view;
    }

    /**
     * Creates map of columns with unique identifier (best performance)
     * @return map of columns
     */
    private Map<String,ColumnItem> getColumnsInMap() {
        Map<String, ColumnItem> columnItemMap=new LinkedHashMap<>();
        if(columns!=null){
            for(ColumnItem columnItem : columns){
                columnItemMap.put(columnItem.getTitle(),columnItem);
            }
        }
        return columnItemMap;
    }

    /**
     * Init Visual components
     * @param view
     */
    private void initViews(View view){
        //Columns
        headerDynamic=(LinearLayout)view.findViewById(R.id.headerDynamic);
        lyColumns=(LinearLayout)view.findViewById(R.id.lyColumns);

        //List
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerView);
    }

    /**
     * Fill column headers
     */
    private void fillColumnsHeaders() {
        CustomTemplates templates=new CustomTemplates(mContext);
        lyColumns.removeAllViews();
        int count=0;

        List<ColumnItem> lstColumnsSelected=new ArrayList<>();
        for(ColumnItem column : columns){
            if(column.isSelected()){
                lstColumnsSelected.add(column);
            }
        }

        for(ColumnItem column : lstColumnsSelected){
            String defaultOrder=Constants.NO_ORDER;
            if(column.getTitle().compareTo(columnOrder)==0){
                defaultOrder=orderType;
            }

            CellProperties cellProperties=column.getCellProperties();
            if(cellProperties==null){
                cellProperties=horizontalAdapter.getDefaultCellProperties();
            }

            LinearLayout ly;
            ly=templates.textFieldOrder(column.getTitle(),
                    cellProperties.getWidth(),column.getTitle(),defaultOrder);
            lyColumns.addView(ly);
            lstHeaderOrderEvent.add(new HeaderOrderEvent(lstHeaderOrderEvent.size()+1,
                    column.getTitle(),defaultOrder,ly));

            if(count<lstColumnsSelected.size()-1){
                lyColumns.addView(templates.separatorLine());
            }
            count++;
        }

        //Adding click event to header
        for(final HeaderOrderEvent headerOrderEvent : lstHeaderOrderEvent){
            headerOrderEvent.getHeader().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HeaderOrderEvent(new HeaderOrderEvent(
                            headerOrderEvent.getPosition(),
                            headerOrderEvent.getColumn(),
                            headerOrderEvent.getOrderType()));

                    /*EventBus.getDefault().post(new HeaderOrderEvent(
                            headerOrderEvent.getPosition(),
                            headerOrderEvent.getColumn(),
                            headerOrderEvent.getOrderType()));*/
                }
            });
        }
    }


    /**
     * Add row to adapter
     */
    public void addRow(Map<String, Object> rowData){
        DataGridItem dataGridItem=new DataGridItem();
        dataGridItem.setMapData(rowData);
        dataGridItem.setFiltered(false);
        dataGridItem.setFilterValue(null);

        if(uniqueColumnKey!=null){
            dataGridItem.setUniqueIdentificator(uniqueColumnKey);
        }
        horizontalAdapter.add(dataGridItem);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(DataGridItem globalTagItem, int position) {

    }


    public void HeaderOrderEvent(HeaderOrderEvent event) {
        columnOrder=event.getColumn();

        switch (event.getOrderType()){
            case Constants.ORDER_ASC:
                orderType=Constants.ORDER_DESC;
                break;
            case Constants.ORDER_DESC:
                orderType=Constants.ORDER_ASC;
                break;
            case Constants.NO_ORDER:
                orderType=Constants.ORDER_ASC;
                break;
            default:
                orderType=Constants.ORDER_ASC;
                break;
        }

        //Show loading dialog before apply changes
        showLoadingDialog();

        //Update Column headers only
        fillColumnsHeaders();
        horizontalAdapter.applyOrderFilter(columnOrder,orderType,this);
    }

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

    public void applyColumnChanges(List<ColumnItem> newColumnsConfiguration) {
        //Save in local variabless
        columns=newColumnsConfiguration;
        mapColumns=getColumnsInMap();

        //Show loading diaglog before apply changes
        showLoadingDialog();

        //Verify ordered column is present in the columns filters
        if(!columnOrder.isEmpty()){
            if(mapColumns.containsKey(columnOrder)){
                if(!mapColumns.get(columnOrder).isSelected()){
                    //If ordered column is not present in columns remove filter
                    columnOrder="";
                    orderType=Constants.NO_ORDER;
                    horizontalAdapter.removeOrderFilter();
                }
            }
        }

        //Change columns headers
        fillColumnsHeaders();

        //Update adapter with new info
        horizontalAdapter.applyColumnsFilter(mapColumns,this);
    }

    @Override
    public void onDataLoaded() {
        hideLoadingDialog();
    }

    /**
     * Show Progress Dialog
     */
    private void showLoadingDialog() {
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
    private void hideLoadingDialog(){
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
}