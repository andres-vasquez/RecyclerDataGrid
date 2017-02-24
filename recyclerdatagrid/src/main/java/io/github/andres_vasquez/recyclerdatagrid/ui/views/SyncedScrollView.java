package io.github.andres_vasquez.recyclerdatagrid.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.ScrollListener;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.ScrollNotifier;

/**
 * Created by avasquez on 7/8/2016.
 */

public class SyncedScrollView extends HorizontalScrollView implements ScrollNotifier {

    private ScrollListener scrollListener = null;

    public SyncedScrollView(Context context) {
        super(context);
    }

    public SyncedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SyncedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SyncedScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null)
            scrollListener.onScrollChanged(this, l, t, oldl, oldt);
    }
    @Override
    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }
    @Override
    public ScrollListener getScrollListener() {
        return scrollListener;
    }
}