package com.nextsense.nsutils.uiBaseElements;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

@SuppressWarnings("unused")
public class NsLoadingScreen extends DialogFragment {
    protected static final String TAG = "NsLoadingScreen";
    private static NsLoadingScreen currentInstance;
    private static @LayoutRes Integer defaultLayoutId;
    private @LayoutRes Integer layoutId;

    private NsLoadingScreen() {}

    private NsLoadingScreen(@Nullable @LayoutRes Integer layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(layoutId != null ? layoutId : defaultLayoutId, container, false);
        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        return mainView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    /**
     * Starts a single fullscreen topmost instance of a loader
     * @param transaction a FragmentTransaction used to display the layout
     */
    public static void show(FragmentTransaction transaction) {
        NsLoadingScreen.show(transaction, (Integer) null);
    }

    /**
     * Starts a single fullscreen topmost instance of a loader
     * @param manager a FragmentManager used to display the layout
     */
    public static void show(FragmentManager manager) {
        NsLoadingScreen.show(manager, (Integer) null);
    }

    /**
     * Starts a single fullscreen topmost instance of a loader
     * @param manager a FragmentManager used to display the layout
     * @param layoutId special layoutId to be displayed instead of the defaultLayout
     */
    public static void show(FragmentManager manager, @Nullable Integer layoutId) {
        NsLoadingScreen.show(manager.beginTransaction(), layoutId);
    }

    /**
     * Starts a single fullscreen topmost instance of a loader
     * @param transaction a FragmentTransaction used to display the layout
     * @param layoutId special layoutId to be displayed instead of the defaultLayout
     */
    public static void show(FragmentTransaction transaction, @Nullable Integer layoutId) {
        if(currentInstance == null && (defaultLayoutId != null || layoutId != null)) {
            currentInstance = new NsLoadingScreen(layoutId);
            currentInstance.show(transaction, TAG);
        }
    }

    /**
     * Close and destroy the currently running instance of the loader
     */
    public static void close() {
        if(currentInstance != null) {
            currentInstance.dismiss();
            currentInstance = null;
        }
    }

    /**
     * Sets a default layout to be used in case a special layout is defined
     * @param layoutId default layout resource id
     */
    public static void setDefaultLayout(@LayoutRes int layoutId) {
        NsLoadingScreen.defaultLayoutId = layoutId;
    }
}