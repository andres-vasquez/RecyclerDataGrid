package io.github.andres_vasquez.recyclerdatagrid.ui.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.CellProperties;
import io.github.andres_vasquez.recyclerdatagrid.models.appClasses.ColumnItem;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;
import io.github.andres_vasquez.recyclerdatagrid.utils.GlobalFunctions;

/**
 * Created by avasquez on 6/27/2016.
 */

public class CustomTemplates {
    private Activity mActivity;
    private Context mContext;

    public CustomTemplates(Activity mActivity) {
        this.mActivity=mActivity;
        this.mContext = mActivity;
    }

    /**
     * Add separator line
     * @return
     */
    public View separatorLine(){
        View view=new View(mContext);
        view.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setBackgroundColor(mContext.getResources().getColor(R.color.hint));
        return view;
    }

    /**
     * Add textfield
     * @param strText
     * @param width
     * @return
     */
    public TextView textField(String strText, int width){
        TextView textView=new TextView(mContext);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setPadding(8,4,8,4);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        if(strText!=null && strText.toLowerCase().compareTo("null")!=0){
            textView.setText(strText);
        } else {
            textView.setText("");
        }
        return textView;
    }

    /**
     * Add Text field timestamp
     * @param strText
     * @param width
     * @return
     */
    public TextView textFieldTimestamp(String strText, int width){
        TextView textView=new TextView(mContext);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setPadding(8,4,8,4);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        try
        {
            textView.setText(GlobalFunctions.getCustomDateFromTimestamp(Long.parseLong(strText), GlobalFunctions.DEFAULT_USA_DATE_FORMAT));
        }
        catch (NumberFormatException ex){
            try {
                textView.setText(GlobalFunctions.getCustomDateFromTimestamp(Double.valueOf(strText).longValue(), GlobalFunctions.DEFAULT_USA_DATE_FORMAT));
            }
            catch (Exception exe){
                textView.setText("");
            }
        }
        return textView;
    }


    public LinearLayout textFieldOrder(String strText, int width, String filterName, String order){
        LinearLayout ly=new LinearLayout(mContext);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        ly.setLayoutParams(params);
        ly.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView imageView=new ImageView(mContext);
        LinearLayout.LayoutParams paramsImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(paramsImg);
        imageView.setPadding(8,4,8,4);
        imageView.setAdjustViewBounds(true);

        switch (order){
            case Constants.ORDER_ASC:
                imageView.setImageResource(R.drawable.arrow_up);
                break;
            case Constants.ORDER_DESC:
                imageView.setImageResource(R.drawable.arrow_down);
                break;
            case Constants.NO_ORDER:
                imageView.setImageResource(0);
                break;
            default:
                imageView.setImageResource(0);
                break;
        }


        TextView textView=new TextView(mContext);
        LinearLayout.LayoutParams paramsTxt=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(paramsTxt);
        textView.setPadding(8,4,8,4);
        textView.setText(strText);

        ly.addView(textView);
        ly.addView(imageView);
        return ly;
    }

    /**
     * Create dynamic cell View
     * @param columnItem column Item
     * @param value value to fill the text
     * @return
     */
    public View dynamicView(ColumnItem columnItem, Object value, boolean isHeader){
        View view=new View(mContext);
        CellProperties cellProperties=columnItem.getCellProperties();
        if(cellProperties==null){
            cellProperties=getDefaultCellProperties();
        }

        if(isHeader){
            if(columnItem.isFixed()){
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(cellProperties.getWidth(),
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView textView=new TextView(mContext);
                textView.setLayoutParams(params);
                textView.setGravity(cellProperties.getGravity());
                textView.setTextColor(cellProperties.getTextColor());
                textView.setTextSize(cellProperties.getTextSize());
                if(value!=null){
                    textView.setText(value.toString());
                } else {
                    textView.setText("");
                }
                view=textView;
            }
        } else {
            if(columnItem.getColumnType()!=Constants.COLUMN_TYPE_IMAGE ){
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(cellProperties.getWidth(),
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView textView=new TextView(mContext);
                textView.setLayoutParams(params);
                textView.setGravity(cellProperties.getGravity());
                textView.setTextColor(cellProperties.getTextColor());
                textView.setTextSize(cellProperties.getTextSize());
                if(value!=null){
                    textView.setText(value.toString());
                } else {
                    textView.setText("");
                }
                view=textView;
            } else {
                //Image container
                LinearLayout lyImageParent=new LinearLayout(mContext);
                LinearLayout.LayoutParams imageParentParams=new LinearLayout.LayoutParams(
                        cellProperties.getWidth(),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                lyImageParent.setLayoutParams(imageParentParams);
                lyImageParent.setGravity(Gravity.CENTER);
                lyImageParent.setPadding(0,2,0,2);

                LinearLayout.LayoutParams imageParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView imageView=new ImageView(mContext);
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(imageParams);

                int imageResource=0;
                try {
                    imageResource=Integer.parseInt(value.toString());
                    imageView.setImageResource(imageResource);
                }catch (NumberFormatException ex){

                }

                lyImageParent.addView(imageView);
                view=lyImageParent;
            }
        }
        return view;
    }

    /**
     * Get width of cell with percentage
     * @return
     */
    public int getCellWidth(){
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        int width=0;
        width=(int)(displayWidth/3); // Each column has 33% of width
        return width;
    }

    /**
     * Get default cellproperties
     * @return
     */
    public CellProperties getDefaultCellProperties(){
        return new CellProperties(getCellWidth(),
                Color.BLACK,
                16,
                Gravity.CENTER);
    }

}
