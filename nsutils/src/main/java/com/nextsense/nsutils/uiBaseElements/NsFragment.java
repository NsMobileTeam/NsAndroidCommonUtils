package com.nextsense.nsutils.uiBaseElements;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class NsFragment<T extends ViewBinding> extends Fragment {
    private static final String BUNDLE_EXTRA_OBJECT = "EXTRA_OBJECT";

    protected T binding;
    private NsActivity<?> activity;
    private Activity parentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = getBinding(inflater);
        parentActivity = getActivity();
        if(parentActivity instanceof NsActivity) {
            activity = (NsActivity<?>) parentActivity;
        }

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

    /**
     * Loads fragment into container within the parent activity with optional(Nullable) animation (set of 4 animation resources)
     * @param fragment the fragment that needs to be loaded
     * @param containerId the id of the fragment container
     * @param addToBackStack fragment to be added in back stack or no
     * @param animationSet nullable set of 4 animation resources used to animate the fragment transaction
     */
    public void loadFragment(Fragment fragment, @IdRes int containerId, boolean addToBackStack, @Nullable Integer[] animationSet) {
        if (getNsActivity() != null) {
            getNsActivity().loadFragment(fragment, containerId, addToBackStack, animationSet);
        } else if(getActivity() != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if(animationSet != null && animationSet.length >= 4) {
                fragmentTransaction.setCustomAnimations(animationSet[0], animationSet[1], animationSet[2], animationSet[3]);
            }

            if(addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }

            fragmentTransaction.replace(containerId, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * Get a NsActivity if fragment is loaded from a NsActivity
     * @return an instance of NsActivity
     */
    @Nullable
    public NsActivity<?> getNsActivity() {
        return activity;
    }

    /**
     * Get the parent activity
     * @return NonNull instance of an activity
     */
    public Activity getParent() {
        return parentActivity;
    }

    /**
     * Pop the main backstack
     */
    public void onBack() {
        parentActivity.onBackPressed();
    }

    /**
     * Pop the entire fragment backstack
     */
    public void popBackstack() {
        getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
