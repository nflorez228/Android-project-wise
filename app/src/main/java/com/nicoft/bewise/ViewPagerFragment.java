package com.nicoft.bewise;


import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;

public class ViewPagerFragment
        extends com.blunderer.materialdesignlibrary.fragments.ViewPagerFragment {

    public ViewPagerHandler getViewPagerHandler() {
        return new ViewPagerHandler(getActivity())
                .addPage(R.string.title_item1, MainFragment.newInstance("Material Design Fragment ViewPager"))
                .addPage(R.string.title_item2, MainFragment.newInstance("Material Design Fragment ViewPager"));
    }

    public int defaultViewPagerPageSelectedPosition() {
        return 0;
    }

    @Override
    public boolean showViewPagerIndicator() {
        return true;
    }

    @Override
    public boolean replaceActionBarTitleByViewPagerPageTitle() {
        return true;
    }

}
