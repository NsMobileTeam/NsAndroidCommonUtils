package com.nextsense.nsutils.uiBaseElements;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NsLoadingView extends FrameLayout {
    private static final int STATIC_ID = 1138312620;
    private static int viewResource;
    private final Context context;

    public static void setLayout(@LayoutRes int viewResource) {
        NsLoadingView.viewResource = viewResource;
    }


    public NsLoadingView(@NonNull Context context) {
        super(context);
        this.context = context;
        setup();
    }

    public NsLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setup();
    }

    public NsLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setup();
    }

    private void setup() {
        View contentView = LayoutInflater.from(context).inflate(viewResource, null);
        addView(contentView);
    }

    public static void show(ViewGroup content) {
        try {
            if (content.findViewById(STATIC_ID) != null) {
                return;
            }

            View loadingView = new NsLoadingView(content.getContext());
            loadingView.setId(STATIC_ID);
            content.addView(loadingView);
        } catch (Exception ignore) { }
    }

    public static void dismiss(ViewGroup content) {
        try {
            View loadingView = content.findViewById(STATIC_ID);
            ((ViewManager)loadingView.getParent()).removeView(loadingView);
            Log.d("","");
        } catch (Exception ignore) { }
    }
}
