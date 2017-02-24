package io.github.andres_vasquez.recyclerdatagrid.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.controller.listener.ScrollManager;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridItem;
import io.github.andres_vasquez.recyclerdatagrid.models.events.HeaderOrderEvent;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.ScrollNotifier;
import io.github.andres_vasquez.recyclerdatagrid.ui.adapters.HorizontalAdapter;
import io.github.andres_vasquez.recyclerdatagrid.ui.views.CustomTemplates;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;

/**
 * Created by andresvasquez on 7/2/17.
 */

public class DataGridFragmentFragment extends DataGridPropertiesFragment
        implements HorizontalAdapter.OnItemClickListener {

    private Activity mActivity;
    private Context mContext;

    //Adapter variables
    private HorizontalAdapter horizontalAdapter;
    private ScrollManager scrollManager;

    //List variables
    private List<DataGridItem> lstItemsInAdapter;
    private LinearLayoutManager linearLayoutManager;

    //Columns and order
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

        horizontalAdapter=new HorizontalAdapter(mActivity,scrollManager,getColumnsInMap());
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
                if(columnItem.getTitle().compareTo(uniqueColumnKey)==0){
                    columnItemMap.put(columnItem.getTitle(),columnItem);
                }
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

            LinearLayout ly;
            ly=templates.textFieldOrder(column.getTitle(),
                    horizontalAdapter.getCellWidth(),column.getTitle(),defaultOrder);
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
                    EventBus.getDefault().post(new HeaderOrderEvent(
                            headerOrderEvent.getPosition(),
                            headerOrderEvent.getColumn(),
                            headerOrderEvent.getOrderType()));
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
}
