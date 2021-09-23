package com.nextsense.nsutils.uiBaseElements;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.commons.NsActivityContract;
import com.nextsense.nsutils.listeners.IUniversalListener;
import com.nextsense.nsutils.locale.LocaleUtil;
import com.nextsense.nsutils.storage.NsPrefs;

import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class NsActivity<T extends ViewBinding> extends AppCompatActivity {
    private static final String BUNDLE_EXTRA_OBJECT = "EXTRA_OBJECT";
    private static final String BUNDLE_EXTRA_CLASS = "EXTRA_CLASS";

    private ActivityResultLauncher<String[]> permissionLauncher;
    private IUniversalListener<Map<String, Boolean>> permissionListener;
    private ActivityResultLauncher<Object> resultLauncher;
    private NsActivityContract<Object, Object> resultContract;
    protected T binding;

    private Intent defaultIntent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        LocaleUtil.applyCurrentLocale(this);
        super.onCreate(savedInstanceState);
        setupBinding();
    }

    private void setupBinding() {
        binding = getBinding();
        setContentView(binding.getRoot());
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::reportPermissionStatus);
        resultContract = new NsActivityContract<>();
        resultLauncher = registerForActivityResult(resultContract, resultContract);
        onCreate();
    }

    protected abstract void onCreate();

    protected abstract T getBinding();

    /**
     * Start request dialogs for each of the permissions
     * @param permissionListener for handling the result of the request
     * @param permissions list of permissions to be requested
     */
    public void requestPermissions(@NonNull IUniversalListener<Map<String, Boolean>> permissionListener, String... permissions) {
        if(permissions != null && permissions.length > 0) {
            this.permissionListener = permissionListener;
            this.permissionLauncher.launch(permissions);
        }
    }

    /**
     * Determine whether all the listed permissions are granted
     * @param permissionResult Map of the permissions requested
     * @return True if all the listed permissions are granted, otherwise returns false
     */
    public boolean arePermissionsGranted(Map<String, Boolean> permissionResult) {
        return arePermissionsGranted((String[]) permissionResult.keySet().toArray());
    }

    /**
     * Determine whether all the listed permissions are granted
     * @param permissions Array of any of the permissions defined in the Manifest.permission class
     * @return True if all the listed permissions are granted, otherwise returns false
     */
    public boolean arePermissionsGranted(String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean arePermissionsGranted = true;
            for (String permission : permissions) {
                arePermissionsGranted &= (UtilBase.getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
            return arePermissionsGranted;
        }
        return true;
    }

    /**
     * Loads fragment into container with optional(Nullable) animation (set of 4 animation resources)
     * @param fragment the fragment that needs to be loaded
     * @param containerId the id of the fragment container
     * @param addToBackStack fragment to be added in back stack or no
     * @param animationSet nullable set of 4 animation resources used to animate the fragment transaction
     */
    public void loadFragment(Fragment fragment, @IdRes int containerId, boolean addToBackStack, @Nullable Integer[] animationSet) {
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
    public void startActivity(Class<? extends NsActivity<?>> activity) {
        startActivity(activity, null, null);
    }

    /**
     * Starts another NsActivity with an extra object passed as an argument with no flags
     * @param activity the activity that needs to be loaded
     * @param extra an extra serializable object to be passed to the new activity
     */
    public void startActivity(Class<? extends NsActivity<?>> activity, @Nullable Object extra) {
        startActivity(activity, extra, null);
    }

    /**
     * Starts another NsActivity with intent flags or with no extras
     * @param activity the activity that needs to be loaded
     * @param flags intent flags for the activity
     */
    public void startActivity(Class<? extends NsActivity<?>> activity, @Nullable Integer flags) {
        startActivity(activity, null, flags);
    }

    /**
     * Starts another NsActivity with intent flags and an extra object passed as an argument
     * @param activity the activity that needs to be loaded
     * @param extra an extra serializable object to be passed to the new activity
     * @param flags intent flags for the activity
     */
    public void startActivity(Class<? extends NsActivity<?>> activity, @Nullable Object extra, @Nullable Integer flags) {
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
     * Launch a universal ActivityResultLauncher bound by a modifiable contract
     * @param input object for the contract of the launcher
     * @param listener for handling the contract's requirements
     */
    public void startLauncher(Object input, @NonNull NsPrefs.IContractInterface<Object,Object> listener) {
        resultContract.setListener(listener);
        resultLauncher.launch(input);
    }

    /**
     * Gets the passed extra object
     * @param <S> class of the extra object
     * @return the passed extra object
     */
    @SuppressWarnings("unchecked")
    protected <S> S getExtra() {
        try {
            String objectJson = getIntent().getStringExtra(String.format("%s.%s", getClass().getCanonicalName(), BUNDLE_EXTRA_OBJECT));
            String classCanonicalName = getIntent().getStringExtra(String.format("%s.%s", getClass().getCanonicalName(), BUNDLE_EXTRA_CLASS));
            return (S) CommonUtils.fromJson(objectJson, Class.forName(classCanonicalName));
        } catch (Exception e) {
            return null;
        }
    }

    private void reportPermissionStatus(Map<String, Boolean> result) {
        if(this.permissionListener != null) {
            this.permissionListener.onSuccess(result);
            this.permissionListener = null;
        }
    }
}