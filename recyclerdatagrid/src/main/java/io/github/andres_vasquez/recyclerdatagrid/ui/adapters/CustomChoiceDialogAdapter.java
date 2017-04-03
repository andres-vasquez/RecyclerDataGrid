package io.github.andres_vasquez.recyclerdatagrid.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ChoiceItem;

/**
 * Created by avasquez on 7/1/2016.
 */

public class CustomChoiceDialogAdapter extends BaseAdapter implements Filterable {
    public Context context;
    public List<ChoiceItem> items;
    public List<ChoiceItem> itemsFiltered = null;

    private ItemFilter mFilter = new ItemFilter();


    public CustomChoiceDialogAdapter(Context context, List<ChoiceItem> items) {
        this.context = context;
        this.items = items;
        this.itemsFiltered = items;
    }

    @Override
    public int getCount() {
        return itemsFiltered.size();
    }

    @Override
    public ChoiceItem getItem(int position) {
        return itemsFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemsFiltered.get(position).getId();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    static class ViewHolder{
        /* UI components of adapter */
        LinearLayout lyIcon;
        ImageView imgIcon;
        LinearLayout lyChecked;
        ImageView imgChecked;
        TextView txtText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            holder=new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_choice_dialog, null);

            holder.lyIcon = (LinearLayout)convertView.findViewById(R.id.lyIcon);
            holder.lyChecked=(LinearLayout)convertView.findViewById(R.id.lyChecked);
            holder.txtText = (TextView)convertView.findViewById(R.id.txtText);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            holder.imgChecked = (ImageView) convertView.findViewById(R.id.imgChecked);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }

        ChoiceItem item=itemsFiltered.get(position);
        holder.txtText.setText(item.getText());

        if(item.getIcon()==0){
            holder.lyIcon.setVisibility(View.GONE);
            holder.imgIcon.setImageDrawable(null);
            holder.txtText.setPadding(56,0,0,0);
        }else{
            holder.lyIcon.setVisibility(View.VISIBLE);
            holder.imgIcon.setImageResource(item.getIcon());
        }

        if(item.isChecked){
            holder.imgChecked.setImageResource(R.drawable.check);
        }
        else{
            holder.imgChecked.setImageResource(0);
        }

        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final List<ChoiceItem> list = items;

            int count = list.size();
            final List<ChoiceItem> nlist = new ArrayList<ChoiceItem>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getText();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemsFiltered = (ArrayList<ChoiceItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
