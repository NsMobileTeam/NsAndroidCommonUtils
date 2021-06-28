package com.nextsense.nsutils.uiBaseElements.fragmentPager;

import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

public class NsPageScroller extends Scroller {
    private final int durationScrollMillis;

    public NsPageScroller(Context context, int durationScroll) {
        super(context, new DecelerateInterpolator());
        this.durationScrollMillis = durationScroll;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, durationScrollMillis);
    }
}
