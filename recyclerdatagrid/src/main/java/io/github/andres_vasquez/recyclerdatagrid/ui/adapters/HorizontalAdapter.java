package io.github.andres_vasquez.recyclerdatagrid.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.controller.listener.ScrollManager;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.DataGridItem;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.RowSelectorStyle;
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

    private Map<String,ColumnItem> mMapFixedColumns;
    private Map<String,ColumnItem> mMapColumns;

    private List<DataGridItem> mItemsFiltered;
    private List<DataGridItem> mItems;

    private Map<Object, Integer> mMapItemsFiltered;
    private Map<Object, Integer> mMapItems;

    private OnItemClickListener onItemClickListener;
    private LayoutInflater mLayoutInflater;
    private ScrollManager mScrollManager;

    //Columns Order
    private String mColumnOrder="";
    private String mOrderType="";

    //Rows filter
    private boolean filterActive;
    private String filterValue;

    //Selectable
    private boolean mSelectable;
    private RowSelectorStyle mRowSelectorStyle;

    public boolean isFilterActive() {
        return filterActive;
    }

    public HorizontalAdapter(Activity activity, ScrollManager mScrollManager,
                             Map<String,ColumnItem> mapColumns,
                             Map<String,ColumnItem> mapFixedColumns){
        this.mActivity=activity;
        this.mContext=activity.getApplicationContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mItemsFiltered =new ArrayList<DataGridItem>();
        this.mItems=new ArrayList<>();
        this.mMapItemsFiltered =new LinkedHashMap<>();
        this.mMapItems =new LinkedHashMap<>();
        this.mScrollManager = mScrollManager;
        this.mMapColumns=mapColumns;
        this.mMapFixedColumns=mapFixedColumns;
    }

    public HorizontalAdapter(Activity activity, ScrollManager mScrollManager,
                             Map<String,ColumnItem> mapColumns, boolean selectable,
                             RowSelectorStyle rowSelectorStyle){
        this.mActivity=activity;
        this.mContext=activity.getApplicationContext();
        this.mSelectable=selectable;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mItemsFiltered =new ArrayList<DataGridItem>();
        this.mItems=new ArrayList<>();
        this.mMapItemsFiltered =new LinkedHashMap<>();
        this.mMapItems =new LinkedHashMap<>();
        this.mScrollManager = mScrollManager;
        this.mMapColumns=mapColumns;
        this.mRowSelectorStyle = rowSelectorStyle;
    }

    @Override
    public HorizontalAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.row_scroll_list, parent, false);
        return new ItemHolder(itemView, this, mScrollManager);
    }

    @Override
    public void onBindViewHolder(final HorizontalAdapter.ItemHolder holder, final int position) {
        final DataGridItem item = mItemsFiltered.get(position);

        //Selector
        //TODO play with selector state
        if(mSelectable && mRowSelectorStyle!=null){
            if(item.isSelected()){
                holder.viewParent.setBackgroundColor(mRowSelectorStyle.getBackgroundColorSelected());
                //holder.imgState.setImageResource(mRowSelectorStyle.getImageSelectorSelected());
            } else {
                holder.viewParent.setBackgroundColor(mRowSelectorStyle.getBackgroundColor());
                //holder.imgState.setImageResource(mRowSelectorStyle.getImageSelector());
            }
            //holder.imgState.setVisibility(View.VISIBLE);
        } else {
            //holder.imgState.setImageResource(0);
            //holder.imgState.setVisibility(View.INVISIBLE);
        }

        holder.lyColumns.removeAllViews();
        holder.lyFixedColumns.removeAllViews();
        CustomTemplates templates=new CustomTemplates(mActivity);

        int dynamicColumnCount=0;
        for(Map.Entry<String,Object> mEntry : item.getMapData().entrySet()){

            View dynamicView;

            //Fill cell proterties inside columns
            //Fill fixed columns
            if(mMapFixedColumns.containsKey(mEntry.getKey())){

                ColumnItem columnItem=mMapFixedColumns.get(mEntry.getKey());
                if(columnItem.isSelected()){
                    dynamicView=templates.dynamicView(columnItem,
                            mEntry.getValue(),false);

                    ViewGroup parent = (ViewGroup) dynamicView.getParent();
                    if (parent != null) {
                        parent.removeView(dynamicView);
                    }

                    // Add to another parent
                    holder.lyFixedColumns.addView(dynamicView);
                    holder.lyFixedColumns.addView(templates.separatorLine());
                }

            } else if(mMapColumns.containsKey(mEntry.getKey())){

                ColumnItem columnItem=mMapColumns.get(mEntry.getKey());
                if(columnItem.isSelected()){
                    dynamicView=templates.dynamicView(columnItem,
                            mEntry.getValue(),false);

                    ViewGroup parent = (ViewGroup) dynamicView.getParent();
                    if (parent != null) {
                        parent.removeView(dynamicView);
                    }

                    // Add to another parent
                    holder.lyColumns.addView(dynamicView);

                    if(dynamicColumnCount<item.getMapData().size()-1){
                        holder.lyColumns.addView(templates.separatorLine());
                    }
                    dynamicColumnCount++;
                }
            }
        }

        holder.lyColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change state for selector
                if(mSelectable){
                    item.setSelected(!item.isSelected());
                    notifyItemChanged(position);
                }

                final OnItemClickListener listener = holder.parent.getOnItemClickListener();
                if(listener != null){
                    listener.onItemClick(item, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mItemsFiltered.size();
    }

    public List<DataGridItem> getmItemsFiltered() {
        return mItemsFiltered;
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
            mItemsFiltered.add(item);
            notifyItemInserted(mItemsFiltered.size()-1);


            if(!mColumnOrder.isEmpty()){
                mapOrderedList(null);
            } else {
                mMapItemsFiltered.put(item.getMapData().get(item.getUniqueIdentificator()), mItemsFiltered.size()-1);
            }
        }
    }

    /**
     * Add item to Datagrid when filter is active.
     * @param item
     */
    private void addFiltered(DataGridItem item) {
        if(item.getFilterValue().equals(filterValue)){
            mItemsFiltered.add(item);
            notifyItemInserted(mItemsFiltered.size()-1);


            if(!mColumnOrder.isEmpty()){
                mapOrderedList(null);
            } else {
                mMapItemsFiltered.put(item.getMapData().get(item.getUniqueIdentificator())
                        , mItemsFiltered.size()-1);
            }
        }
    }

    /**
     * Update data from existing row
     * @param item
     */
    public void addOrUpdate(final DataGridItem item){
        //Update in complete list
        if(mMapItems.containsKey(item.getMapData().get(item.getUniqueIdentificator()))){
            int allPosition=mMapItems.get(item.getMapData().get(item.getUniqueIdentificator()));
            mItems.get(allPosition).setMapData(item.getMapData());

            if(isFilterActive()){
                if(item.getFilterValue().compareTo(filterValue)==0){
                    if(mMapItemsFiltered.containsKey(item.getMapData().get(item.getUniqueIdentificator()))){

                        //Update data by position
                        int position = mMapItemsFiltered.get(item.getMapData().get(item.getUniqueIdentificator()));
                        mItemsFiltered.get(position).setMapData(item.getMapData());
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
                if(mMapItemsFiltered.containsKey(item.getUniqueIdentificator())){
                    //Update data with inactive filter
                    int position = mMapItemsFiltered.get(item.getMapData().get(item.getUniqueIdentificator()));
                    mItemsFiltered.get(position).setMapData(item.getMapData());
                    notifyItemChanged(position);

                    if(!mColumnOrder.isEmpty()) {
                        mapOrderedList(null);
                    }
                }
            }
        } else {
            //Add new items
            mItems.add(item);
            mMapItems.put(item.getMapData().get(item.getUniqueIdentificator()), mItems.size()-1);

            add(item);
        }
    }

    /**
     * Remove a row from Datagrid
     * @param item
     */
    public void remove(DataGridItem item){
        //Remove from the grid
        if(mMapItemsFiltered.containsKey(item.getUniqueIdentificator())){
            int position= mMapItemsFiltered.get(item.getUniqueIdentificator());
            mItemsFiltered.remove(item);
            mMapItemsFiltered.remove(item.getUniqueIdentificator());
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
        Collections.sort(mItemsFiltered, new ComparatorItems(mColumnOrder, mOrderType));
        notifyItemRangeChanged(0, mItemsFiltered.size());

        mMapItemsFiltered.clear();
        int position=0;
        for (DataGridItem item : mItemsFiltered){
            mMapItemsFiltered.put(item.getMapData(),position);
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
        int aux= mItemsFiltered.size();
        mItemsFiltered.clear();
        mMapItemsFiltered.clear();
        notifyItemRangeRemoved(0,aux);

        mItems.clear();
        mMapItems.clear();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{

        private HorizontalAdapter parent;
        View viewParent;
        LinearLayout lyColumns;
        LinearLayout lyFixedColumns;

        public ItemHolder(View itemView, HorizontalAdapter parent, ScrollManager scrollManager) {
            super(itemView);
            viewParent=itemView;

            ScrollNotifier view;
            view = (ScrollNotifier) itemView.findViewById(R.id.scroll);
            scrollManager.addScrollClient(view);

            this.parent = parent;
            lyColumns=(LinearLayout) itemView.findViewById(R.id.lyColumns);
            lyFixedColumns=(LinearLayout) itemView.findViewById(R.id.lyFixedColumns);
        }
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

    /**
     * Set selectable enable/disable
     * @param isSelectable change selectable parameter
     */
    public void setSelectable(boolean isSelectable){
        this.mSelectable=isSelectable;
    }


    public void applyContentFilter(String filterValue) {
        //Clear items in View
        mMapItems.clear();
        mItemsFiltered.clear();
        notifyDataSetChanged();


        for(DataGridItem item : mItems){
            if(filterValue!=null){
                //TODO compare objects
                if(item.getFilterValue().compareTo(filterValue)==0){
                    mItemsFiltered.add(item);
                    mMapItems.put(item.getUniqueIdentificator(),mItemsFiltered.size()-1);
                    notifyDataSetChanged();
                }
            } else {
                mItemsFiltered.add(item);
                mMapItems.put(item.getUniqueIdentificator(),mItemsFiltered.size()-1);
                notifyDataSetChanged();
            }
        }

        if(filterValue!=null){
            enableFilter(filterValue);
        } else {
            disableFilter();
        }
    }

    public void enableFilter(String filterValue ){
        this.filterActive=true;
        this.filterValue=filterValue;
    }

    public void disableFilter(){
        this.filterActive=false;
        this.filterValue=null;
    }
}

