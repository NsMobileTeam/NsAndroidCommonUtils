package com.nextsense.nsutils.uiBaseElements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.io.Serializable;

public abstract class NsFragment<T extends ViewBinding> extends Fragment {
    private static final String BUNDLE_EXTRA_OBJECT = "EXTRA_OBJECT";

    protected T binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = getBinding(inflater);
        onCreateView();
        return this.binding.getRoot();
    }

    public abstract void onCreateView();

    protected abstract T getBinding(LayoutInflater inflater);

    /**
     * Pass an extra serializable object to a newly created Fragment
     * @param extra serializable object
     */
    public void setExtra(@Nullable Object extra) {
        if(extra != null) {
            Bundle extraBundle = new Bundle();
            extraBundle.putSerializable(BUNDLE_EXTRA_OBJECT, (Serializable) extra);
            setArguments(extraBundle);
        }
    }

    /**
     * Gets the passed extra object
     * @param <S> class of the extra object
     * @return the passed extra object
     */
    @SuppressWarnings("unchecked")
    protected <S> S getExtra() {
        try {
            assert getArguments() != null;
            return (S) getArguments().getSerializable(BUNDLE_EXTRA_OBJECT);
        } catch (Exception e) {
            return null;
        }
    }
}
