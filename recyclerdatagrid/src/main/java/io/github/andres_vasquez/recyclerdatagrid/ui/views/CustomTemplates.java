package io.github.andres_vasquez.recyclerdatagrid.ui.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.andres_vasquez.recyclerdatagrid.R;
import io.github.andres_vasquez.recyclerdatagrid.utils.Constants;
import io.github.andres_vasquez.recyclerdatagrid.utils.GlobalFunctions;

/**
 * Created by avasquez on 6/27/2016.
 */

public class CustomTemplates {
    private Context mContext;

    public CustomTemplates(Context mContext) {
        this.mContext = mContext;
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
}
