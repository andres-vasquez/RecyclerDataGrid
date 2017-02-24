package io.github.andres_vasquez.recyclerdatagrid.models.interfaces;

/**
 * Created by avasquez on 7/8/2016.
 */

public interface ScrollNotifier {

    public void setScrollListener(ScrollListener scrollListener);
    public ScrollListener getScrollListener();
}