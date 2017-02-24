package io.github.andres_vasquez.recyclerdatagrid.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.controller.listener.ScrollManager;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.CellProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridItem;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.LoadInterface;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.ScrollNotifier;
import io.github.andres_vasquez.recyclerdatagrid.ui.views.CustomTemplates;
import io.github.andres_vasquez.recyclerdatagrid.utils.ComparatorItems;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;

/**
 * Created by avasquez on 6/23/2016.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ItemHolder>{
    private Activity mActivity;
    private Context mContext;

    private Map<String,ColumnItem> mMapColumns;
    private List<DataGridItem> mItems;
    private Map<Object, Integer> mMapItems;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater mLayoutInflater;
    private ScrollManager mScrollManager;

    //Columns Order
    private String mColumnOrder="";
    private String mOrderType="";

    //Rows filter
    private boolean filterActive;
    private Object filterValue;

    public boolean isFilterActive() {
        return filterActive;
    }

    public HorizontalAdapter(Activity activity, ScrollManager mScrollManager,
                             Map<String,ColumnItem> mapColumns){
        this.mActivity=activity;
        this.mContext=activity.getApplicationContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mItems =new ArrayList<DataGridItem>();
        this.mMapItems =new LinkedHashMap<>();
        this.mScrollManager = mScrollManager;
        this.mMapColumns=mapColumns;
    }

    public HorizontalAdapter(Activity activity,String mColumnOrder, String mOrderType,
                             ScrollManager mScrollManager){
        this.mActivity=activity;
        this.mContext=activity.getApplicationContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mItems =new ArrayList<DataGridItem>();
        this.mMapItems =new LinkedHashMap<Object, Integer>();
        this.mScrollManager = mScrollManager;
        this.mColumnOrder = mColumnOrder;
        this.mOrderType = mOrderType;
    }


    @Override
    public HorizontalAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.row_scroll_list, parent, false);
        return new ItemHolder(itemView, this, mScrollManager);
    }

    @Override
    public void onBindViewHolder(final HorizontalAdapter.ItemHolder holder, final int position) {
        final DataGridItem item = mItems.get(position);

        holder.lyColumns.removeAllViews();
        CustomTemplates templates=new CustomTemplates(mContext);

        int count=0;
        for(Map.Entry<String,Object> mEntry : item.getMapData().entrySet()){

            //TODO implement different types
            TextView textView;

            //Fill cell proterties inside columns
            if(mMapColumns.containsKey(mEntry.getKey())){
                if(mMapColumns.get(mEntry.getKey()).isSelected()){
                    CellProperties cellProperties=mMapColumns.get(
                            mEntry.getKey()).getCellProperties();
                    if(cellProperties==null){
                        cellProperties=getDefaultCellProperties();
                    }

                    try {
                        textView=templates.textField(mEntry.getValue().toString(),
                                cellProperties.getWidth());
                        textView.setGravity(cellProperties.getGravity());
                        textView.setTextColor(cellProperties.getTextColor());
                        textView.setTextSize(cellProperties.getTextSize());
                    }catch (NullPointerException ex){
                        textView=templates.textField(mEntry.getValue().toString(),
                                cellProperties.getWidth());
                    }


                    ViewGroup parent = (ViewGroup) textView.getParent();
                    if (parent != null) {
                        parent.removeView(textView);
                    }

                    // Add to another parent
                    holder.lyColumns.addView(textView);

                    if(count<item.getMapData().size()-1){
                        holder.lyColumns.addView(templates.separatorLine());
                    }
                    count++;
                }
            }
        }

        holder.lyColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OnItemClickListener listener = holder.parent.getOnItemClickListener();
                if(listener != null){
                    listener.onItemClick(item, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<DataGridItem> getmItems() {
        return mItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }


    public interface OnItemClickListener{
        public void onItemClick(DataGridItem globalTagItem, int position);
    }

    /**
     * Add item to Datagrid
     * @param item datagrid item
     */
    public void add(DataGridItem item){
        if(isFilterActive()){
            addFiltered(item);
        } else {
            mItems.add(item);
            notifyItemInserted(mItems.size()-1);

            if(!mColumnOrder.isEmpty()){
                mapOrderedList(null);
            } else {
                mMapItems.put(item.getUniqueIdentificator(), mItems.size()-1);
            }
        }
    }

    /**
     * Add item to Datagrid when filter is active.
     * @param item
     */
    private void addFiltered(DataGridItem item) {
        mItems.add(item);
        notifyItemInserted(mItems.size()-1);

        if(!mColumnOrder.isEmpty()){
            mapOrderedList(null);
        } else {
            mMapItems.put(item.getUniqueIdentificator(), mItems.size()-1);
        }
    }

    /**
     * Update data from existing row
     * @param item
     */
    public void update(final DataGridItem item){
        //Update data in List
        if(isFilterActive()){
            if(item.getFilterValue().equals(filterValue)){
                if(mMapItems.containsKey(item.getUniqueIdentificator())){

                    //Update data by position
                    int position = mMapItems.get(item.getUniqueIdentificator());
                    mItems.get(position).setMapData(item.getMapData());
                    notifyItemChanged(position);
                    if(!mColumnOrder.isEmpty()) {
                        mapOrderedList(null);
                    }
                } else {
                    //Add to List if doesn't exists
                    addFiltered(item);
                }
            } else {
                //If doesn't have the value as Filter remove it
                remove(item);
            }
        } else {
            //Update or add without filters
            if(mMapItems.containsKey(item.getUniqueIdentificator())){
                //Update data with inactive filter
                int position = mMapItems.get(item.getUniqueIdentificator());
                mItems.get(position).setMapData(item.getMapData());
                notifyItemChanged(position);
                if(!mColumnOrder.isEmpty()) {
                    mapOrderedList(null);
                }
            } else {
                //Add to List if doesn't exists
                add(item);
            }
        }
    }

    /**
     * Remove a row from Datagrid
     * @param item
     */
    public void remove(DataGridItem item){
        //Remove from the grid
        if(mMapItems.containsKey(item.getUniqueIdentificator())){
            int position= mMapItems.get(item.getUniqueIdentificator());
            mItems.remove(item);
            mMapItems.remove(item.getUniqueIdentificator());
            notifyItemRemoved(position);
            if(!mColumnOrder.isEmpty()) {
                mapOrderedList(null);
            }
        }
    }

    /**
     * Order items from datagrid
     */
    private void mapOrderedList(LoadInterface callback){
        Collections.sort(mItems, new ComparatorItems(mColumnOrder, mOrderType));
        notifyItemRangeChanged(0, mItems.size());

        mMapItems.clear();
        int position=0;
        for (DataGridItem item : mItems){
            mMapItems.put(item.getMapData(),position);
            position++;
        }

        if(callback!=null){
            callback.onDataLoaded();
        }
    }

    /**
     * Clear items in Datagrid
     */
    public void clear(){
        int aux= mItems.size();
        mItems.clear();
        mMapItems.clear();
        notifyItemRangeRemoved(0,aux);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{

        private HorizontalAdapter parent;
        View viewParent;
        LinearLayout lyColumns;
        ImageView imgState;

        public ItemHolder(View itemView, HorizontalAdapter parent, ScrollManager scrollManager) {
            super(itemView);
            viewParent=itemView;

            ScrollNotifier view;
            view = (ScrollNotifier) itemView.findViewById(R.id.scroll);
            scrollManager.addScrollClient(view);

            this.parent = parent;
            lyColumns=(LinearLayout) itemView.findViewById(R.id.lyColumns);
        }
    }


    private int getCellWidth(){
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        int width=0;
        width=(int)(displayWidth/3); // Each column has 33% of width
        return width;
    }

    public CellProperties getDefaultCellProperties(){
        return new CellProperties(getCellWidth(),
                Color.BLACK,
                16,
                Gravity.CENTER);
    }

    /**
     * Apply column order
     * @param columnOrder Key of column to order
     * @param orderType Tyoe of order ASC, DESC or NONE
     * @param callback Callback after data loaded
     */
    public void applyOrderFilter(String columnOrder, String orderType, LoadInterface callback) {
        this.mColumnOrder=columnOrder;
        this.mOrderType=orderType;
        mapOrderedList(callback);
    }

    /**
     * Remove order filter for adapter
     */
    public void removeOrderFilter(){
        this.mColumnOrder="";
        this.mOrderType= Constants.NO_ORDER;
    }


    /**
     * Apply new columns configuration
     * @param mapColumnsUpdated Columns Map
     * @param callback Callback after data loaded
     */
    public void applyColumnsFilter(Map<String, ColumnItem> mapColumnsUpdated,LoadInterface callback) {
        mMapColumns=mapColumnsUpdated;
        notifyDataSetChanged();
        callback.onDataLoaded();
    }
}

