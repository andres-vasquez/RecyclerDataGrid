package io.github.andres_vasquez.recyclerdatagrid.controller.listener;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;

import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.ScrollListener;
import io.github.andres_vasquez.recyclerdatagrid.models.interfaces.ScrollNotifier;

/**
 * Created by avasquez on 7/8/2016.
 */



public class ScrollManager implements ScrollListener {

    private static final int SCROLL_HORIZONTAL = 1;
    private static final int SCROLL_VERTICAL = 2;

    private ArrayList clients = new ArrayList(4);

    private volatile boolean isSyncing = false;
    private int scrollType = SCROLL_HORIZONTAL;

    public void addScrollClient(ScrollNotifier client) {
        clients.add(client);
        client.setScrollListener(this);
    }

    // TODO fix dependency on all views being of equal horizontal/ vertical
    // dimensions
    @Override
    public void onScrollChanged(View sender, int l, int t, int oldl, int oldt) {
        // avoid notifications while scroll bars are being synchronized
        if (isSyncing)
            return;

        isSyncing = true;

        // remember scroll type
        if (l != oldl)
            scrollType = SCROLL_HORIZONTAL;
        else if (t != oldt)
            scrollType = SCROLL_VERTICAL;
        else {
            // not sure why this should happen
            isSyncing = false;
            return;
        }

        // addOrUpdate clients
        for (Object client : clients) {
            View view = (View) client;
            // don't addOrUpdate sender
            if (view == sender)
                continue;

            // scroll relevant views only
            // TODO Add support for horizontal ListViews - currently weird things happen when ListView is being scrolled horizontally
            if ((scrollType == SCROLL_HORIZONTAL && view instanceof HorizontalScrollView)
                    || (scrollType == SCROLL_VERTICAL && view instanceof ScrollView)
                    || (scrollType == SCROLL_VERTICAL && view instanceof ListView))
                view.scrollTo(l, t);
        }

        isSyncing = false;
    }
}