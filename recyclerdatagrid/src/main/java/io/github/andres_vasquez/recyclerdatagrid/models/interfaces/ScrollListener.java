package io.github.andres_vasquez.recyclerdatagrid.models.interfaces;

import android.view.View;

/**
 * Created by avasquez on 7/8/2016.
 */

public interface ScrollListener {
    void onScrollChanged(View syncedScrollView, int l, int t, int oldl, int oldt);
}