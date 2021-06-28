package com.nextsense.nsutils.uiBaseElements.fragmentPager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class NsFragmentPagerAdapter extends PagerAdapter {
    private final NsFragmentPager layoutPager;

    NsFragmentPagerAdapter(NsFragmentPager layoutPager) {
        this.layoutPager = layoutPager;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        return layoutPager.getChildAt(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0 == arg1;
    }
}