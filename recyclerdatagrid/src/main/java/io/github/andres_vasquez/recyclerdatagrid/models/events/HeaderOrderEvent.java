package io.github.andres_vasquez.recyclerdatagrid.models.events;

import android.widget.LinearLayout;

/**
 * Created by avasquez on 8/16/2016.
 */

public class HeaderOrderEvent {
    private int position;
    private String column;
    private String orderType;
    private LinearLayout header;

    //Use as Event
    public HeaderOrderEvent(int position, String column, String orderType) {
        this.position=position;
        this.column = column;
        this.orderType = orderType;
    }

    //Use as List
    public HeaderOrderEvent(int position, String column, String orderType, LinearLayout header) {
        this.position=position;
        this.column = column;
        this.orderType = orderType;
        this.header = header;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public LinearLayout getHeader() {
        return header;
    }

    public void setHeader(LinearLayout header) {
        this.header = header;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
