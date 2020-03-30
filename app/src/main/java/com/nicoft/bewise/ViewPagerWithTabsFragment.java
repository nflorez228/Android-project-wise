package com.nicoft.bewise;


import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;

public class ViewPagerWithTabsFragment
        extends com.blunderer.materialdesignlibrary.fragments.ViewPagerWithTabsFragment {

    public ViewPagerHandler getViewPagerHandler() {
        return new ViewPagerHandler(getActivity())
                .addPage(R.string.title_item1,
                        MainFragment.newInstance("1"))
                .addPage(R.string.title_item2,
                        MainFragment.newInstance("2"));
    }

    @Override
    public boolean expandTabs() {
        return true;
    }

    public int defaultViewPagerPageSelectedPosition() {
        return 0;
    }

}
