package com.nextsense.nsutils.uiBaseElements;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.locale.LocaleUtil;

public abstract class NsActivity extends AppCompatActivity {
    private static final String BUNDLE_EXTRA_OBJECT = "EXTRA_OBJECT";
    private static final String BUNDLE_EXTRA_CLASS = "EXTRA_CLASS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LocaleUtil.applyCurrentLocale(this);
        super.onCreate(savedInstanceState);
    }

    /**
     * Loads fragment into container with optional(Nullable) animation (set of 4 animation resources)
     * @param fragment the fragment that needs to be loaded
     * @param containerId the id of the fragment container
     * @param addToBackStack fragment to be added in back stack or no
     * @param animationSet nullable set of 4 animation resources used to animate the fragment transaction
     */
    protected void loadFragment(Fragment fragment, @IdRes int containerId, boolean addToBackStack, @Nullable Integer[] animationSet) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(animationSet != null && animationSet.length >= 4) {
            fragmentTransaction.setCustomAnimations(animationSet[0], animationSet[1], animationSet[2], animationSet[3]);
        }

        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Starts another NsActivity with no flags or extras
     * @param activity the activity that needs to be loaded
     */
    protected void startActivity(Class<? extends NsActivity> activity) {
        startActivity(activity, null, null);
    }

    /**
     * Starts another NsActivity with an extra object passed as an argument with no flags
     * @param activity the activity that needs to be loaded
     * @param extra an extra serializable object to be passed to the new activity
     */
    protected void startActivity(Class<? extends NsActivity> activity, @Nullable Object extra) {
        startActivity(activity, extra, null);
    }

    /**
     * Starts another NsActivity with intent flags or with no extras
     * @param activity the activity that needs to be loaded
     * @param flags intent flags for the activity
     */
    protected void startActivity(Class<? extends NsActivity> activity, @Nullable Integer flags) {
        startActivity(activity, null, flags);
    }

    /**
     * Starts another NsActivity with intent flags and an extra object passed as an argument
     * @param activity the activity that needs to be loaded
     * @param extra an extra serializable object to be passed to the new activity
     * @param flags intent flags for the activity
     */
    protected void startActivity(Class<? extends NsActivity> activity, @Nullable Object extra, @Nullable Integer flags) {
        Intent intent = new Intent(UtilBase.getContext(), activity);
        if(extra != null) {
            intent.putExtra(String.format("%s.%s", activity.getCanonicalName(), BUNDLE_EXTRA_OBJECT), CommonUtils.toJson(extra));
            intent.putExtra(String.format("%s.%s", activity.getCanonicalName(), BUNDLE_EXTRA_CLASS), extra.getClass().getCanonicalName());
        }

        if(flags != null) {
            intent.setFlags(flags);
        }

        startActivity(intent);
    }

    /**
     * Gets the passed extra object
     * @param <T> class of the extra object
     * @return the passed extra object
     */
    @SuppressWarnings("unchecked")
    protected <T> T getExtra() {
        try {
            String objectJson = getIntent().getStringExtra(String.format("%s.%s", getClass().getCanonicalName(), BUNDLE_EXTRA_OBJECT));
            String classCanonicalName = getIntent().getStringExtra(String.format("%s.%s", getClass().getCanonicalName(), BUNDLE_EXTRA_CLASS));
            return (T) CommonUtils.fromJson(objectJson, Class.forName(classCanonicalName));
        } catch (Exception e) {
            return null;
        }
    }
}