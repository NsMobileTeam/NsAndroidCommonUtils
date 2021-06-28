package com.nextsense.nsutils.uiBaseElements.fragmentPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nextsense.nsutils.R;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;

@SuppressWarnings("unused")
public class NsFragmentPager extends ViewPager implements BottomNavigationView.OnNavigationItemSelectedListener {

    private int currentPagePosition = 0;
    private NsFragmentPagerAdapter layoutPagerAdapter;
    private Menu menu;

    public NsFragmentPager(Context context) {
        super(context);
        setup();
    }

    public NsFragmentPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setAttributes(AttributeSet attrs) {
        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.NsFragmentPager, 0, 0);
        setMenu(attributes.getResourceId(R.styleable.NsFragmentPager_menuId, -1));
    }

    private void setup() {
        this.addOnPageChangeListener(onPageChangeListener);
        this.layoutPagerAdapter = new NsFragmentPagerAdapter(this);
        this.setAdapter(layoutPagerAdapter);
        setOffscreenPageLimit(100);
        this.setScrollDuration(400);
    }

    @SuppressLint("ResourceType")
    public void setMenu(@MenuRes int menuId) {
        try {
            PopupMenu p = new PopupMenu(getContext(), null);
            menu = p.getMenu();
            ((Activity) getContext()).getMenuInflater().inflate(menuId, menu);
            for (int i = 0; i < menu.size(); i++) {
                FragmentContainerView view = new FragmentContainerView(getContext());
                view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setId((int) (Math.random() * Integer.MAX_VALUE));
                addView(view);
            }
        } catch (Exception e) {
            throw new InvalidParameterException("No or invalid menuId set for the Pager");
        }
    }

    public void loadFragments(FragmentManager fragmentManager, Fragment... fragments) {
        if(menu != null && menu.size() > 0 && fragments.length > 0) {
            int n = Math.min(fragments.length, menu.size());
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (int i = 0; i < n; i++) {
                transaction.add(getChildAt(i).getId(), fragments[i]);
            }

            transaction.commit();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;
            if (wrapHeight) {
                View child = getChildAt(currentPagePosition);
                if (child != null) {
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void reMeasureCurrentPage(int position) {
        currentPagePosition = position;
        requestLayout();
    }

    public int nextPage() {
        int currentItem = NsFragmentPager.this.getCurrentItem();
        if(currentItem < layoutPagerAdapter.getCount()-1) {
            setCurrentItem(currentItem + 1);
        }
        return currentItem + 1;
    }

    public int previousPage() {
        int currentItem = NsFragmentPager.this.getCurrentItem();
        if(currentItem > 0) {
            setCurrentItem(currentItem - 1);
        }
        return currentItem - 1;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == SCROLL_STATE_IDLE) {
                reMeasureCurrentPage(NsFragmentPager.this.getCurrentItem());
            }
        }
    };

    public void setScrollDuration(int millis) {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new NsPageScroller(getContext(), millis));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyDataSetChanged() {
        layoutPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        for(int i = 0; i < menu.size(); i++) {
            if(item.getItemId() == menu.getItem(i).getItemId()) {
                setCurrentItem(i);
                return true;
            }
        }
        return false;
    }
}
