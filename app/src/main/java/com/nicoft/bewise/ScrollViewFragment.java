package com.nicoft.bewise;

import android.os.Handler;

public class ScrollViewFragment
        extends com.blunderer.materialdesignlibrary.fragments.ScrollViewFragment {

    public int getContentView() {
        return R.layout.fragment_scrollview;
    }

    public boolean pullToRefreshEnabled() {
        return true;
    }

    public int[] getPullToRefreshColorResources() {
        return new int[]{R.color.primary};
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setRefreshing(false);
            }

        }, 2000);
    }

}
